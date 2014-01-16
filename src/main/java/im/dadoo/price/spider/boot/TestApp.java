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
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestApp {

	public static void main(String[] args) throws IOException, InterruptedException {
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(
//              new String[]{"dadoo-mq-context.xml",
//                "logger-client-context.xml", 
//                "price-core-context.xml",
//                "price-spider-context.xml"});
    CloseableHttpClient httpClient = HttpClients.createDefault();
    Long time = System.currentTimeMillis();
    HttpGet httpGet = new HttpGet("http://product.suning.com/0000000000/102757957.html");
    CloseableHttpResponse res = httpClient.execute(httpGet);
    EntityUtils.consume(res.getEntity());
    res.close();
    Thread.sleep(1000);
    httpGet = new HttpGet("http://product.suning.com/0000000000/SNProductStatusView?storeId=10052&catalogId=10051&productId=2669559&cityId=9017&_=0");
    res = httpClient.execute(httpGet);
    System.out.println(EntityUtils.toString(res.getEntity()));
    res.close();
	}
}
