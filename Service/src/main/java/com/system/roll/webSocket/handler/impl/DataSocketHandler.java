package com.system.roll.webSocket.handler.impl;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.entity.constant.impl.TimeUnit;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.properites.CommonProperties;
import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.mapper.CourseMapper;
import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.redis.StudentRedis;
import com.system.roll.security.interceptor.LoginInterceptor;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.JsonUtil;
import com.system.roll.utils.SpringContextUtil;
import com.system.roll.webSocket.context.SocketContext;
import com.system.roll.webSocket.context.SocketContextHandler;
import com.system.roll.webSocket.handler.CustomHttpServletRequest;
import com.system.roll.webSocket.handler.SocketHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Date;

/**
 * 教师端获取实时考勤数据的长连接
 * */
@Slf4j
@Component
@ServerEndpoint(value = "/professor/roll/data/{courseId}/{token}")
public class DataSocketHandler implements SocketHandler {

    private RollDataListener listener;

    @Override
    public void open(Session session, String token) {

    }

    @OnOpen
    @Override
    public void open(Session session,@PathParam(value = "courseId")String courseId,@PathParam(value = "token")String token) throws IOException {
        /*进行token验证*/
        LoginInterceptor loginInterceptor = SpringContextUtil.getBean("LoginInterceptor");
        try {loginInterceptor.preHandle(new CustomHttpServletRequest().setHeader("Authorization",token).setSession(session),new Response(),token);}
        catch (ServiceException e){
            session.getBasicRemote().sendText(JsonUtil.toJson(Result.error(e)));
            session.close();
        }
        /*将自己注册到context中*/
        SocketContextHandler.addContext("data:"+courseId,new SocketContext(this,session));
        /*从redis中读取最近一次的考勤统计数据*/
//        RollDataRedis rollDataRedis = SpringContextUtil.getBean("RollDataRedis");
//        SingleRollStatisticVo statistics = rollDataRedis.getRollDataStatistics(courseId);
//        if (statistics==null){
//            /*若无考勤数据，发送消息*/
//            session.getBasicRemote().sendText(JsonUtil.toJson(Result.error(ResultCode.RESOURCE_NOT_FOUND)));
//        }else {
//            /*发送最近一次考勤统计数据*/
//            session.getBasicRemote().sendText(JsonUtil.toJson(Result.success(statistics)));
//        }
        /*创建队列*/
        RabbitUtil rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
        RabbitProperties rabbitProperties = SpringContextUtil.getBean("RabbitProperties");
        CommonProperties commonProperties = SpringContextUtil.getBean("CommonProperties");
        rabbitUtil.createTTLQueue(rabbitProperties.getRollDataQueuePrefix()+courseId,rabbitProperties.getRollDataExchange(),courseId,false,commonProperties.RollDataTTL(TimeUnit.MINUTE));
        /*注册监听器，循环读取内容*/
        this.listener = new RollDataListener(courseId, rabbitProperties.getRollDataQueuePrefix() + courseId);
        this.listener.run();
    }

    @Override
    public void open(Session session, String param1, String param2, String token) {

    }

    @OnMessage
    @Override
    public void onMessage(String data) {

    }

    @OnClose
    @OnError
    @Override
    public void onClose(@PathParam(value = "courseId")String courseId) throws IOException {
        SocketHandler.super.onClose("data:"+courseId);
        this.listener.stop();
    }

    @Data
    public static class RollDataListener implements Runnable{
        private String courseId;
        private String queueName;
        private boolean isRunning;

        public RollDataListener(String courseId,String queueName){
            this.courseId = courseId;
            this.queueName = queueName;
            this.isRunning = true;
        }

        public void stop(){
            this.isRunning = false;
        }

        @Override
        @SuppressWarnings("all")
        public void run() {
            RabbitUtil rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
            EnumUtil enumUtil = SpringContextUtil.getBean("EnumUtil");
            CourseMapper courseMapper = SpringContextUtil.getBean("CourseMapper");
            StudentRedis studentRedis = SpringContextUtil.getBean("StudentRedis");
            SocketContext context = SocketContextHandler.getContext("data:" + courseId);
            Course course = courseMapper.selectById(courseId);
            RollDataVo statistic = new RollDataVo().setDate(new Date(System.currentTimeMillis())).setEnrollNum(course.getEnrollNum()).setFlag(0);

            boolean flag = true;//标记是否为刚刚开始接收信号
            while (this.isRunning){
                String data = rabbitUtil.consume(queueName, 1000);
                /*收到考勤数据，向前端发送*/
                try {
                    if (data!=null){
                        RollData rollData = JsonUtil.toObject(data,RollData.class).setFlag(1);
                        log.info("收到数据：{}",rollData);
                        if (rollData.getEnrollNum()!=null) statistic.setEnrollNum(rollData.getEnrollNum());
                        rollData.setStudentName(studentRedis.getName(rollData.getStudentId()));
                        if (flag){
                            /*生成累积统计数据*/
                            switch (enumUtil.getEnumByCode(RollState.class,rollData.getState())){
                                case ATTENDANCE:
                                    statistic.incrAttendanceNum();
                                    break;
                                case ABSENCE:
                                    statistic.addAbsence(rollData.getStudentId(),rollData.getStudentName());
                                    break;
                                case LATE:
                                    statistic.addLate(rollData.getStudentId(),rollData.getStudentName());
                                    break;
                                case LEAVE:
                                    statistic.addLeave(rollData.getStudentId(),rollData.getStudentName());
                            }
                        }else {
                            /*发送最近一次的考勤数据*/
                            context.sendMessage(ResultCode.SUCCESS,rollData);
                        }
                    }else {
                        /*刚刚开始接收信号完毕，一口气发送累积的结果*/
                        if (flag){
                            flag = false;
                            context.sendMessage(ResultCode.SUCCESS,statistic);
                        }
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    try {
                        context.sendMessage(ResultCode.SERVER_ERROR,null);
                    } catch (IOException | EncodeException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
