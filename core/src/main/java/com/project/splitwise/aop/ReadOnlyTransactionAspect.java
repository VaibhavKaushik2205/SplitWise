package com.project.splitwise.aop;

import com.project.splitwise.datasource.DataSourceContextHolder;
import com.project.splitwise.datasource.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReadOnlyTransactionAspect {

    @Around("@annotation(ReadOnly)")
    public Object proceed(ProceedingJoinPoint pjp) throws Throwable {
        try {
            DataSourceContextHolder.set(DataSourceType.READ_ONLY);
            return pjp.proceed();
        } finally {
            DataSourceContextHolder.setDefault();
        }
    }
}