package im.dadoo.price.spider;

import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.spider.Spider;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	
  public static void main(String[] args) throws IOException {
  	
  	Timer timer = new Timer();
  	timer.schedule(new TimerTask() {
  		
   		private ApplicationContext ctx = new ClassPathXmlApplicationContext("pricespider-spring.xml");
    	private Spider spider = (Spider) ctx.getBean("spider");
    	
			@Override
			public void run() {
					spider.start();
					
			}}, 0, Parser.PERIOD);
  }
}
