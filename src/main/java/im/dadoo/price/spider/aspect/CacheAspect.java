package im.dadoo.price.spider.aspect;

import im.dadoo.price.cache.entity.CachePrice;
import im.dadoo.price.cache.service.CachePriceService;
import im.dadoo.price.core.domain.Price;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CacheAspect {

	private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);
	
	@Autowired
	private CachePriceService cachePriceService;
	
	@AfterReturning(value = "execution(public * im.dadoo.price.core.service.PriceService.save(..)) ", 
			returning="price")
	public void setLatest(Price price) {
		CachePrice cp = this.cachePriceService.set(price);
		logger.info(String.format("记录到cache成功,内容为%s", cp));
	}
}
