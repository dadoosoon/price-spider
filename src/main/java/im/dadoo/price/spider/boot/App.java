package im.dadoo.price.spider.boot;

import im.dadoo.price.core.configuration.PriceCoreContext;
import im.dadoo.price.spider.configuration.PriceSpiderContext;
import im.dadoo.price.spider.spider.Spider;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
 
public class App {
	
  private static final Logger logger = LoggerFactory.getLogger(App.class);
  
  public static void main(String[] args) throws IOException {
  	
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            PriceCoreContext.class, PriceSpiderContext.class);
    final Spider spider = (Spider) ctx.getBean("spider");
    spider.start(ctx);
  }
}
