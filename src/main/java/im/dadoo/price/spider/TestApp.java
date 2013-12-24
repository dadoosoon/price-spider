package im.dadoo.price.spider;


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
		System.out.println(ObjectUtils.compare(null, 1.00));
		
	}
}
