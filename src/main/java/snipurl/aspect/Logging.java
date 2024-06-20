package snipurl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static snipurl.utils.Constants.INVOKED_IN_CONTROLLER;
import static snipurl.utils.Constants.INVOKED_IN_SERVICE;

@Aspect
@Component
public class Logging {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * A method that runs before any method execution in the controller package.
     *
     * @param joinPoint the join point at which the advice is being applied
     */
    @Before("execution(* snipurl.controller.*.*(..))")
    public void beforeController(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info(INVOKED_IN_CONTROLLER, methodName);
    }

    /**
     * A method that runs before any method execution in the service package.
     *
     * @param joinPoint the join point at which the advice is being applied
     */
    @Before("execution(* snipurl.service.*.*(..))")
    public void beforeService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info(INVOKED_IN_SERVICE, methodName);
    }
}
