package com.system.roll.operationLog.aspect;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.constant.impl.Role;
import com.system.roll.entity.pojo.OperationLog;
import com.system.roll.operationLog.annotation.Operation;
import com.system.roll.operationLog.util.LoggerUtil;
import com.system.roll.security.context.SecurityContextHolder;
import com.system.roll.utils.IdUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.Timestamp;

/**
 * 业务操作的切面处理类
 * 搭配业务操作的日志工具类一起使用
 * 用于获取对应操作的日志信息，并保存到数据库中
 * */
@Aspect
@Component
public class OperationAspect {

    @Resource
    private IdUtil idUtil;
    @Resource
    private LoggerUtil loggerUtil;

    /**
     * 定义切点 @Pointcut
     * */
    @Pointcut("@annotation(com.system.roll.operationLog.annotation.Operation)")
    public void operationLog(){}

    /**
     * 切面通知
     * 保存获取到的操作的日志信息，并保存到数据库中
     * */
    @AfterReturning("operationLog()")
    public void saveOperationLog(JoinPoint joinPoint){
        /*用户的信息*/
        String id = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        String name = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "name");
        Role role = SecurityContextHolder.getContext().getAuthorization().getInfo(Role.class, "role");
        /*从切面织入点处通过反射机制获取织入点处的方法*/
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        /*获取切入点所在的方法*/
        Method method = signature.getMethod();
        /*获取注解中的内容（操作类型）*/
        Operation operation = method.getAnnotation(Operation.class);
        /*生成操作的概要*/
        StringBuilder outlineBuilder = new StringBuilder()
                .append("[").append(role.getMsg()).append("]")
                .append(name).append("(").append(id).append("):");
        OperationType operationType = operation.type();
        outlineBuilder.append(operationType.getMsg());
        String outline = outlineBuilder.toString();

        /*提取操作的主要内容*/
        String operationLog = loggerUtil.getOperationLog();
        /*生成操作日志对象*/
        String operationId = idUtil.getId();

        OperationLog operationLog1 = new OperationLog().setId(operationId).setOutline(outline).setOperationLog(operationLog).setOperationType(operationType).setTime(new Timestamp(System.currentTimeMillis())).setAttachmentId(null);

        System.out.println(operationLog1);

    }
}
