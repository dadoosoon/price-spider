package im.dadoo.price.spider.boot;

import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.spider.Spider;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
public class App {
	
  public static void main(String[] args) throws IOException {
  	
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
              new String[]{"dadoo-mq-context.xml",
                "logger-client-context.xml", 
                "price-core-context.xml",
                "price-spider-context.xml"});
    final Spider spider = (Spider) ctx.getBean("spider");
    
  	Timer timer = new Timer();
  	timer.schedule(new TimerTask() {

			@Override
			public void run() {
					spider.start();
					
			}}, 0, Constants.PERIOD);
  }
}
