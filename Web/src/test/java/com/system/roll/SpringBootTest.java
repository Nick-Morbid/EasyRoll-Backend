package com.system.roll;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.system.roll.constant.impl.MsgType;
import com.system.roll.constant.impl.OperationType;
import com.system.roll.describer.Describer;
import com.system.roll.describer.DescriberPoll;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.pojo.User;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.message.Message;
import com.system.roll.entity.vo.message.supervisor.builder.MessageBuilder;
import com.system.roll.excel.annotation.Excel;
import com.system.roll.excel.uitl.ExcelUtil;
import com.system.roll.excel.uitl.impl.ExcelUtilImpl;
import com.system.roll.properites.AppletProperties;
import com.system.roll.properites.CommonProperties;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.service.WxApiService;
import com.system.roll.uitls.HttpRequestUtil;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.IdUtil;
import com.system.roll.utils.JsonUtil;
import com.system.roll.utils.PinyinUtil;
import lombok.Data;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.boot.test.context.SpringBootTest(webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTest {
    @Resource
    private JwtSecurityHandler jwtSecurityHandler;

    @Test
    public void testGetToken(){
//        System.out.println(jwtSecurityHandler.getExpiredTime());
//        System.out.println(jwtSecurityHandler.getTokenSecret());
//        System.out.println(jwtSecurityHandler.getToken(Map.of("id", 1L, "role", 0, "departmentId", 1L)));
    }

    @Test
    public void testVerify(){
        String token = jwtSecurityHandler.getToken("1","nick",0,"1");
        DecodedJWT verify = jwtSecurityHandler.verify(token);
        System.out.println(verify.getClaim("id").asInt());
        System.out.println(verify.getClaim("role").asInt());
        System.out.println(verify.getClaim("departmentId").asInt());
    }
    @Resource
    private HttpRequestUtil httpRequestUtil;
    @Resource
    private AppletProperties appletProperties;
    @Test
    public void testHttpGet() throws IOException {
        String appid = appletProperties.getAppId();
        String secret = appletProperties.getSecret();
        String grant_type = "authorization_code";
        String js_code = "083iF7Ga1hsAfE0zeaJa1zYkOt3iF7Gc";
        Result<?> result = httpRequestUtil.httpGet("https://api.weixin.qq.com/sns/jscode2session", Map.of("appid", appid, "secret", secret, "grant_type", grant_type, "js_code", js_code));
        System.out.println(result.getStatus());
        System.out.println(result.getMessage());
        Map<String,Object> data = (Map<String, Object>) result.getData();
        System.out.println(JsonUtil.toJson(result.getData()));
        System.out.println(data.get("errcode"));
        System.out.println(data.get("errmsg"));
        System.out.println(data.get("session_key"));
        System.out.println(data.get("openid"));
    }
    @Resource
    private WxApiService wxApiService;
    @Test
    public void testAccessToken(){
        System.out.println(wxApiService.accessToken());
    }
    @Resource
    private IdUtil idUtil;
    @Test
    public void testPostHttp() throws IOException {
        Map<String,Object> param = new HashMap<>();
        param.put("access_token",wxApiService.accessToken());
        StringEntity body = httpRequestUtil.getStringEntityBuilder()
                .add("scene", idUtil.getWebSocketId())
                .add("page", "pages/login/login")
                .add("check_path", false)
                .add("env_version", "trial")
                .build();
        Result<?> result = httpRequestUtil.httpPost(appletProperties.getGetWXACodeUnLimit(),param,body);
        System.out.println(result.toString());


    }
    @Test
    public void testGetGetWXACodeUnLimit() throws IOException {
//        wxApiService.getGetWXACodeUnLimit();
//        InputStream inputStream = wxApiService.getGetWXACodeUnLimit();
//        String path = System.getProperty("user.dir") + "/text.jpg";
//        File file = new File(path);
//        if (!file.exists()) file.createNewFile();
//        FileImageOutputStream outputStream = new FileImageOutputStream(file);
//        byte[] buffer =new byte[1024*1024];
//        int res;
//        while ((res = inputStream.read(buffer))>0){
//            System.out.println(res);
//            outputStream.write(buffer);
//        }
//        inputStream.close();
//        outputStream.close();
    }
    @Resource
    private DateUtil dateUtil;
    @Resource
    private CommonProperties commonProperties;
    @Test
    public void testGetFirstWeek() throws ParseException {
        System.out.println(commonProperties.FirstWeek());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(new Date(format.parse(dateUtil.getFirstWeek()).getTime()));
    }
    @Test
    public void testGetWeek(){
        Date date = commonProperties.FirstWeek();
        Long time = date.getTime();
        while (time<System.currentTimeMillis()){
            Date date1 = new Date(time);
            Integer week = dateUtil.getWeek(date1);
            System.out.println("日期 "+date1+"是第"+week+"周.");
            time+=DateUtil.DAY;
        }
//        dateUtil.getWeek(dateUtil.stringToDate("2020-08-30"));
    }
    @Test
    public void testMsgBuilder(){
        Message message1 = MessageBuilder.getBuilder(MsgType.OPERATION_NOTICE)
                .setContent("督导人员tom删除了<<软件工程>>的课程信息")
                .setTime(new Timestamp(System.currentTimeMillis()))
                .setData("operationId", "123456789")
                .setData("operationLog", "督导人员tom删除了<<软件工程>>的课程信息")
                .setData("isRejected", 1)
                .setData("rejectors", new String[]{"庄洁洁", "王涛涛"})
                .build();

        Message message2 = MessageBuilder.getBuilder(MsgType.LEAVE_APPLICATION)
                .setContent("学生胡彬彬(100000005)的请假申请（2022-11-11:08:20到2022-11-12:08:20）")
                .setTime(new Timestamp(System.currentTimeMillis()))
                .setData("leaveId", "123456789")
                .setData("startTime", new Timestamp(System.currentTimeMillis()))
                .setData("endTime", new Timestamp(System.currentTimeMillis()))
                .setData("excuse", "请假理由")
                .setData("attachment", "https:nicklorry/resources/images?id=123456789")
                .build();

        System.out.println(message1);
        System.out.println(message2);
    }

    @Resource(name = "DescriberPoll")
    private DescriberPoll describerPoll;
    @Test
    public void testDescriberPoll(){
        Describer describer1 = describerPoll.getDescriber(OperationType.UPLOAD_COURSE);
        Describer describer2 = describerPoll.getDescriber(OperationType.UPDATE_COURSE);
        Describer describer3 = describerPoll.getDescriber(OperationType.DELETE_COURSE);
        Describer describer4 = describerPoll.getDescriber(OperationType.TAKE_A_ROLL);
        Describer describer5 = describerPoll.getDescriber(OperationType.SOLVE_LEAVE_APPLICATION);
        System.out.println(describer1.getClass());
        System.out.println(describer2.getClass());
        System.out.println(describer3.getClass());
        System.out.println(describer4.getClass());
        System.out.println(describer5.getClass());
    }
    @Test
    public void test(){
        String data1 = "123456789";
        String data2 = data1+",";
        System.out.println(data1.matches("\\d+"));
        System.out.println(data2.matches("\\d+"));
    }
    @Resource(name = "RollDataRedis")
    private RollDataRedis rollDataRedis;
    @Test
    public void testRollDataRedis(){
        String courseId = "123456789";
        List<RollData> rollDataList =  new ArrayList<>();
        rollDataList.add(new RollData().setCourseId(courseId).setStudentId("1").setState(1).setTime(new Timestamp(System.currentTimeMillis())));
        rollDataList.add(new RollData().setCourseId(courseId).setStudentId("2").setState(1).setTime(new Timestamp(System.currentTimeMillis())));
        rollDataList.add(new RollData().setCourseId(courseId).setStudentId("3").setState(1).setTime(new Timestamp(System.currentTimeMillis())));
        rollDataList.add(new RollData().setCourseId(courseId).setStudentId("4").setState(1).setTime(new Timestamp(System.currentTimeMillis())));
        rollDataList.add(new RollData().setCourseId(courseId).setStudentId("5").setState(1).setTime(new Timestamp(System.currentTimeMillis())));
        rollDataRedis.saveRollDataList("123456789",rollDataList);
        List<RollData> rollDataList1 = rollDataRedis.getRollDataList(courseId);
        for (int i = 0; i < rollDataList1.size(); i++) {
            System.out.println(rollDataList1.get(i));
            System.out.println(rollDataList1.get(i).getClass());
        }
        rollDataList1.forEach(System.out::println);
//        System.out.println(JsonUtil.toJson(rollDataList));
//        List<RollData> rollDataList1 = new Gson().fromJson(JsonUtil.toJson(rollDataList), new TypeToken<List<RollData>>() {
//        List<RollData> rollDataList1 = JsonUtil.toList(JsonUtil.toJson(rollDataList));
//        rollDataList1.forEach(System.out::println);
    }

    @Test
    public void testGson(){
        List<User> users = new ArrayList<>();
        users.add(new User().setName("nick").setId("123"));
        users.add(new User().setName("nick").setId("123"));
        users.add(new User().setName("nick").setId("123"));
        System.out.println(new Gson().toJson(users));
        List<User> users1 = new Gson().fromJson(new Gson().toJson(users), new TypeToken<List<User>>() {
        }.getType());
        users1.forEach(System.out::println);
    }

    @Resource
    private PinyinUtil pinyinUtil;
    @Test
    public void testPinyinUtil() throws BadHanyuPinyinOutputFormatCombination {
        for (String str : pinyinUtil.toPinyin("陈宏侨")) {
            System.out.println(str);
        }
    }
    @Resource(name = "ExcelUtil")
    private ExcelUtil excelUtil;
    @Test
    public void testImportExcel() throws IOException {
        String filePath = "F:\\软工大作业\\项目代码\\EasyRoll-Backend\\resource\\2022K结对编程分数汇总（个人得分）.xlsx";
        System.out.println(filePath);
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        List<ExcelUtilImpl.Test> tests = excelUtil.importExcel(ExcelUtilImpl.Test.class, fileInputStream, ExcelUtil.ExcelType.XLSX);
        tests.forEach(System.out::println);
        fileInputStream.close();
    }

    @Test
    public void test01() throws IOException {
        String filePath = "F:\\软工大作业\\项目代码\\EasyRoll-Backend\\resource\\2022K结对编程分数汇总（个人得分）.xlsx";
        System.out.println(filePath);
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        List<Student> students = excelUtil.importExcel(Student.class, fileInputStream, ExcelUtil.ExcelType.XLSX);
        students.forEach(System.out::println);
    }

    @Data
    public static class Student{
        @Excel(value = "学号")
        private String id;
        @Excel(value = "姓名")
        private String name;
    }
}
