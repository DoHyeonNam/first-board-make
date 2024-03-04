package board.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggerAspect {
	
	@Around("execution(* board..controller.*Controller.*(..)) || execution(* board..service.*Impl.*(..)) || execution(* board..mapper.*Mapper.*(..))")
	// @Around 어노테이션으로 대상 메서드의 실행 전후에 로그 기록
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		// ProceedingJoinPoint 인터페이스
		// : 어드바이스가 적용된 메서드의 실행 시점에서 aspect 가 해당 메서드를 호출하면 이때 proceedingJoinPoint 객체가 생성되어 어드바이스 내부에서 사용된다

		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();

		if (name.indexOf("Controller") > -1) {
			type = "Controller  \t:  ";
		}
		else if (name.indexOf("Service") > -1) {
			type = "ServiceImpl  \t:  ";
		}
		else if (name.indexOf("Mapper") > -1) {
			type = "Mapper  \t\t:  ";
		}

		log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}
}
