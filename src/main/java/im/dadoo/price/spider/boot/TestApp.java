package im.dadoo.price.spider.boot;


import im.dadoo.price.spider.parser.AmazonCnParser;
import im.dadoo.price.spider.parser.DangdangParser;
import im.dadoo.price.spider.parser.GomeParser;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.parser.SfbestParser;
import im.dadoo.price.spider.parser.WomaiParser;
import im.dadoo.price.spider.parser.YhdParser;
import im.dadoo.price.spider.parser.YixunParser;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestApp {

	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
              new String[]{"dadoo-mq-context.xml",
                "logger-client-context.xml", 
                "price-core-context.xml",
                "price-spider-context.xml"});
    Parser parser = ctx.getBean(WomaiParser.class);
    
    System.out.println(parser.parse("http://www.womai.com/Product-0-332935.htm"));
	}
}
