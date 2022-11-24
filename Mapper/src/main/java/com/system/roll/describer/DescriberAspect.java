package com.system.roll.describer;

import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 备用器的切面处理类
 * 用于生成快照以及生成对应的日志信息
 * 可以将快照持久化到redis中，将日志信息保存到消息队列中
 * */
@Slf4j
@Aspect
@Component
public class DescriberAspect {

    @Resource
    private IdUtil idUtil;
    @Resource
    private DescriberPoll describerPoll;

    /**
     * 切面通知
     * 保存获取到的操作的日志信息，并保存到数据库中
     * */
    @Around(value = "@annotation(com.system.roll.describer.annotation.Operation)")
    public Object saveOperationLog(ProceedingJoinPoint pjp) throws Throwable {
        /*用户的信息*/
        System.out.println("3:"+Thread.currentThread().getId());
        String id = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        String name = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "name");
        Role role = SecurityContextHolder.getContext().getAuthorization().getInfo(Role.class, "role");
        /*从切面织入点处通过反射机制获取织入点处的方法*/
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        /*获取切入点所在的方法*/
        Method method = signature.getMethod();
        /*获取注解中的内容（操作类型）*/
        Operation operation = method.getAnnotation(Operation.class);
        /*获取方法的参数*/
        Object[] args = pjp.getArgs();
        log.info("执行环绕切面，被执行方法名为：{}",method.getName());
        /*分配一个操作标识id*/
        String operationId = idUtil.getId();
        /*获取备份器*/
        Describer describer = describerPoll.getDescriber(operation.type());
        /*生成并保存快照*/
        Describer.Snapshot<?> snapshot = describer.saveSnapshot(operationId,args);
        log.info("生成的快照内容：{}",snapshot);
        /*执行目标方法*/
        Object proceed = pjp.proceed();
        /*生成并保存日志信息*/
        Describer.LogInfo<?> logContent = describer.saveLog(operationId,args);
        log.info("生成的日志信息内容：{}",logContent);
        log.info("结束环绕切面，被执行方法名为：{}",method.getName());
        /*返回目标方法的返回值（这样调用目标方法处才能拿到返回结果）*/
        return proceed;
    }
}
