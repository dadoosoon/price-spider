package im.dadoo.price.spider.boot;

import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.spider.Spider;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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
    while (true) {
      Long t1 = System.currentTimeMillis();
      spider.start();
      Long t2 = System.currentTimeMillis();
      Double m = (t2 - t1) / 60000.0;
      logger.info(String.format("本轮采集总共耗时%f", m));
    }
//  	Timer timer = new Timer();
//  	timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//        Long t1 = System.currentTimeMillis();
//				spider.start();
//				Long t2 = System.currentTimeMillis();
//        Double m = (t2 - t1) / 60000.0;
//        logger.info(String.format("本轮采集总共耗时%f", m));
//			}}, 0, Constants.PERIOD);
  }
}
