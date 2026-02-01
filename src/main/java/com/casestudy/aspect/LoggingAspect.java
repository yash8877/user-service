package com.casestudy.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Before the method executes, log method entry
    @Before("execution(* com.casestudy.service.UserService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Entering method: " + joinPoint.getSignature().getName());
    }

    // After the method executes, log method exit
    @After("execution(* com.casestudy.service.UserService.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("Exiting method: " + joinPoint.getSignature().getName());
    }

    // After returning a result, log method exit and the result
    @AfterReturning(pointcut = "execution(* com.casestudy.service.UserService.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("Method: " + joinPoint.getSignature().getName() + " returned: " + result);
    }
}
