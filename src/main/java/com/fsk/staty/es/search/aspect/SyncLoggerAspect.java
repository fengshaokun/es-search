package com.fsk.staty.es.search.aspect;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 17:42
 */
import com.fsk.staty.es.search.aspect.log.SyncLogAspectHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SyncLoggerAspect {
    private SyncLogAspectHelper logAspectHelper = new SyncLogAspectHelper();

    public SyncLoggerAspect() {
    }

    @Pointcut("execution(public * com.fsk.staty.es.search.controller.*Controller.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        this.logAspectHelper.doBeforeHelper(joinPoint);
    }

    @AfterReturning(
            returning = "response",
            pointcut = "webLog()"
    )
    public void doAfterReturning(Object response) throws Throwable {
        this.logAspectHelper.doAfterReturningHelper(response);
    }
}
