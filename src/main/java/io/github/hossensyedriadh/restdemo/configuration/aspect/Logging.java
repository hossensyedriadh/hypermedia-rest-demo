package io.github.hossensyedriadh.restdemo.configuration.aspect;

import io.github.hossensyedriadh.restdemo.utils.HttpUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class Logging {
    private static final Logger logger = Logger.getLogger(Logging.class);

    @Pointcut("execution(* io.github.hossensyedriadh.restdemo.controller.*.*.*.*(..))")
    public void executeForAllControllers() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.InitBinder)")
    public void excludeInitBinders() {
    }

    @Pointcut("@annotation(org.springframework.beans.factory.annotation.Value)")
    public void excludeSetterMethods() {
    }

    @Before("executeForAllControllers() && !excludeInitBinders() && !excludeSetterMethods()")
    public void incoming(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpUtils httpUtils = new HttpUtils(servletRequestAttributes.getRequest());
        logger.info("Inbound request -> Src: " + httpUtils.parseClientAddress() + " | ID: "
                + servletRequestAttributes.getRequest().getAttribute("Request-ID") + " | Method: " + methodSignature + ".");
    }

    @After("executeForAllControllers() && !excludeInitBinders() && !excludeSetterMethods()")
    public void outgoing(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpUtils httpUtils = new HttpUtils(servletRequestAttributes.getRequest());
        logger.info("Outbound response -> Dest: " + httpUtils.parseClientAddress() + " | ID: "
                + servletRequestAttributes.getRequest().getAttribute("Request-ID") + " | Method: " + methodSignature + ".");
    }

    @AfterReturning(pointcut = "executeForAllControllers() && !excludeInitBinders() && !excludeSetterMethods()", returning = "object")
    public void onSuccess(JoinPoint joinPoint, Object object) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> returnType = methodSignature.getReturnType();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpUtils httpUtils = new HttpUtils(servletRequestAttributes.getRequest());
        logger.info("Address: " + httpUtils.parseClientAddress() + " | ID: "
                + servletRequestAttributes.getRequest().getAttribute("Request-ID") + " | Method: " + methodSignature +
                " | returned " + returnType + " | response: " + object + ".");
    }

    @AfterThrowing(pointcut = "executeForAllControllers() && !excludeInitBinders() && !excludeSetterMethods()", throwing = "throwable")
    public void onFailure(JoinPoint joinPoint, Throwable throwable) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> returnType = methodSignature.getReturnType();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpUtils httpUtils = new HttpUtils(servletRequestAttributes.getRequest());
        logger.error("Address: " + httpUtils.parseClientAddress() + " | ID: "
                + servletRequestAttributes.getRequest().getAttribute("Request-ID") + " | Method: " + methodSignature +
                " | Return Type: " + returnType + " has thrown " + throwable + ".");
    }
}
