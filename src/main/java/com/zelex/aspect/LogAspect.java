package com.zelex.aspect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author Zelex
 * @Date 2021/2/10 20:14
 * @Version 1.0
 */
//日志切面
@Aspect
@Component
public class LogAspect {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    //切面
    @Pointcut("execution(* com.zelex.web.*.*(..))")
    public void log(){}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        //1、首先要获得请求
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();//拿到请求
        //2、通过请求来封装内部类
        String classMethod=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();//方法名
        Object[] args = joinPoint.getArgs();//方法参数
        RequestLog requestLog = new RequestLog(request.getRequestURL().toString(),
                request.getRemoteAddr(),
                classMethod,args);

        logger.info("Result:{}",requestLog);
    }

    @After("log()")
    public void doAfter(){
        logger.info("---------------doAfter---------------");
    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void afterReturn(Object result){
        logger.info("Result:{}",result);
    }
    @Data
    @AllArgsConstructor
    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
