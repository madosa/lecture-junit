package com.interpark.lecture.junit.common.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Trace 로그 기록을 위한 AOP 적용 클래스
 * @author Ma JoonChae
 * Async 처리는 추후에 진행.
 */
@Component
@Aspect
public class ServiceLogTraceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogTraceAspect.class);

    /**
     * com.interpark.jukebox 하위의 모든 public 메소드가 실행 될 때 이벤트 처리
     * @throws Throwable 
     */
    @Around("execution(public * *(..)) && within(com.interpark.lecture.junit..*) && !execution(* com.interpark.ncl.std.common.log.LogInterceptor.*(..))")
    public Object loggingTrace(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        // 메소드 argument를 조회해서 로그 출력
        Object[] args = proceedingJoinPoint.getArgs();
        List<Object> convertedListArgs = Arrays.asList(args);
        List<String> strArgs = convertedListArgs.stream().filter(arg -> arg instanceof String)
                .map(arg -> {
                    String temp = (String)arg;
                    return temp.length() > 100 ? temp.substring(0, 97) + "..." : temp;
                }).collect(Collectors.toList());
        String argValues = String.join("|", strArgs);
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        LOGGER.info("{}::{} start - args {}", className, methodName, argValues);

        // 작업 진행
        Object retVal = proceedingJoinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        // 결과 값 로그 출력
        LOGGER.info("{}::{} end - return {}, time taken {}", className, methodName, retVal, executeTime);
        return retVal;
    }
}
