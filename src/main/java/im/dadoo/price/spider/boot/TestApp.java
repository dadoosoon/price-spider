package im.dadoo.price.spider.boot;


import im.dadoo.price.spider.parser.DangdangParser;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.parser.SfbestParser;
import im.dadoo.price.spider.parser.YhdParser;

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
              new String[]{"price-spider-context.xml", "logger-client-context.xml"});
    Parser parser = ctx.getBean(YhdParser.class);
    System.out.println(parser.parse("http://item.yhd.com/item/10791608"));
		
	}
}
