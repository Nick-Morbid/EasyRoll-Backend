package com.system.roll.operationLog.aspect;

import com.system.roll.operationLog.annotation.Operation;
import com.system.roll.utils.IdUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

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
        /*从切面织入点处通过反射机制获取织入点处的方法*/
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        /*获取切入点所在的方法*/
        Method method = signature.getMethod();
        /*获取注解中的内容（操作类型）*/
        Operation operation = method.getAnnotation(Operation.class);
        /*生成操作的概要*/
        String content = "";
        if (operation!=null){
            String value = operation.value();

        }
    }
}
