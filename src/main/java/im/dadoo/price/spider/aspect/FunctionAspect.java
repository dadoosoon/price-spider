package im.dadoo.price.spider.aspect;

import im.dadoo.logger.client.DadooLog;
import im.dadoo.logger.client.LogCreator;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.spider.cons.Constants;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class FunctionAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(FunctionAspect.class);
	
	@Autowired
	private LoggerClient loggerClient;
	
	@Around("execution(public Double im.dadoo.price.spider.parser.Parser.parse(..)) ")
	public Object logFun(ProceedingJoinPoint pjp) throws Throwable {
		Long t1 = System.currentTimeMillis();
		Object ret = pjp.proceed();
		Long t2 = System.currentTimeMillis();
		String sig = pjp.getSignature().toLongString();
		Object[] args = pjp.getArgs();
		
		//发送到日志服务器
		DadooLog log = LogCreator.createFunLog(Constants.SERVICE_NAME, sig, args, ret, t2 - t1);
		this.loggerClient.send(log);
		
		logger.info("函数信息:{}~~参数值:{}~~返回值:{}~~运行时间:{}", sig, args, ret, t2 - t1);
		return ret;
	}
}
