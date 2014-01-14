package im.dadoo.price.spider.boot;

import im.dadoo.price.spider.spider.Spider;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
public class App {
	
  private static final Logger logger = LoggerFactory.getLogger(App.class);
  
  public static void main(String[] args) throws IOException {
  	
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
              new String[]{"dadoo-mq-context.xml",
                "logger-client-context.xml", 
                "price-core-context.xml",
                "price-spider-context.xml"});
    final Spider spider = (Spider) ctx.getBean("spider");
    spider.start();
  }
}
