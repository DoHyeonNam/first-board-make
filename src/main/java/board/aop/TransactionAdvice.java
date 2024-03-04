//package board.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
//
//@Slf4j
//@Aspect
//@Component
//public class TransactionAdvice {
//
//    @Autowired
//    private PlatformTransactionManager platformTransactionManager;
//
//    @Around("execution(* board..service.*.*(..))")
//    public Object transaction(ProceedingJoinPoint joinPoint)throws Throwable{
//        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionAttribute());
//        Object object = null;
//        try {
//            object = joinPoint.proceed();
//            platformTransactionManager.commit(transactionStatus);
//        } catch (Exception e){
//            platformTransactionManager.rollback(transactionStatus);
//        }
//        return object;
//    }
//
//}