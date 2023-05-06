package com.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    /**
     * AOP通知
     * 1、前置通知
     * 2、后置通知
     * 3、环绕通知
     * 4、异常通知
     * 5、最终通知
     */

    /**
     * 切面表达式
     * execution 代表所要执行的表达式主体
     * 第一处 *代表方法返回值类型 *代表所有类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 ..代表该包以及其子包所有的方法
     * 第四处 *代表类名
     * 第五处 *(..)代表类中的方法名  ..表示方法中任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.demo.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        //记录开始时间
        long begin=System.currentTimeMillis();
        //执行目标service方法
        Object result = joinPoint.proceed();
        //记录结束时间
        long end = System.currentTimeMillis();
        long takeTime=end-begin;

        if (takeTime>3000){
            log.error("======执行 {} 结束，耗时{}毫秒", joinPoint.getSignature().getName(), takeTime);
        }else if(takeTime>2000){
            log.warn("======执行 {} 结束，耗时{}毫秒", joinPoint.getSignature().getName(), takeTime);
        }else {
            log.info("======执行 {} 结束，耗时{}毫秒", joinPoint.getSignature().getName(), takeTime);
        }

        return result;

    }
}
