package board.aop;



import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.*;

@Configuration
@Aspect
@Slf4j
public class TransactionAspect {
   @Autowired
   private PlatformTransactionManager transactionManager;
   
   @Around("execution(* board..service.*Impl.*(..))")
   public Object transaction(ProceedingJoinPoint joinPoint) throws Throwable {
      TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionAttribute());
      Object object = null;
      try {
         object = joinPoint.proceed();
         transactionManager.commit(transactionStatus);
      } catch (Exception e) {
         transactionManager.rollback(transactionStatus);
      }
      return object;
   }      
}