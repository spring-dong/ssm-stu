package com.keafmd.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AnnotationPointcut {
    @Before("execution(* com.keafmd.service.impl.*.*(..))")
    public void before(){
        System.out.println("---------方法执行前---------");
    }

    @After("execution(* com.keafmd.service.impl.*.*(..))")
    public void after(){
        System.out.println("---------方法执行后---------");
    }

    @Around("execution(* com.keafmd.service.impl.*.*(..))")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕前");
        System.out.println("签名:"+jp.getSignature());
        //执行目标方法proceed
        Object proceed = jp.proceed();
        System.out.println("环绕后");
        System.out.println(proceed);
    }

/**
 * 1.@Before 前置增强（目标方法执行之前，执行注解标注的内容）
 *
 * 2.@AfterReturning 后置增强（目标方法正常执行完毕后，执行）
 *
 * 3.@Around 环绕增强（目标方法执行前后，分别执行一些代码）
 *
 * 4.@AfterThrowing 抛出增强（目标方法发生异常，执行）
 *
 * 5.@After Final增强（不管是抛出异常还是正常退出，该增强都会得到执行。一般用于释放资源，相当于try{}finally{}）
 */

}
