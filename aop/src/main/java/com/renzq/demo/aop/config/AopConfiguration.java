package com.renzq.demo.aop.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * AopConfiguration is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 16:33
 * Description:
 */
@Aspect
@Configuration
public class AopConfiguration {

    @Pointcut("execution(* com.renzq.demo.aop.web.*.*(..))")
    public void executation(){

    }

    @Before("executation()")
    public void before(){
        System.out.println("Before---start");
    }

    @After("executation()")
    public void after(){
        System.out.println("After-end");
    }

}