package im.dadoo.price.spider.boot;


import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.parser.AmazonCnParser;
import im.dadoo.price.spider.parser.BenlaiParser;
import im.dadoo.price.spider.parser.DangdangParser;
import im.dadoo.price.spider.parser.GomeParser;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.parser.SfbestParser;
import im.dadoo.price.spider.parser.SuningParser;
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
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
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
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
              new String[]{"dadoo-mq-context.xml",
                "logger-client-context.xml", 
                "price-spider-context.xml"});
//    RequestConfig globalConfig = RequestConfig.custom()
//        .setCookieSpec(CookieSpecs.BEST_MATCH)
//        .setConnectTimeout(Constants.TIME_OUT)
//        .setSocketTimeout(Constants.TIME_OUT)
//        .build();
//    CloseableHttpClient httpClient = HttpClients.custom()
//        .setDefaultRequestConfig(globalConfig)
//        .build();
//    HttpGet httpGet = new HttpGet("http://product.suning.com/105077896.html");
//    
//    
//    CloseableHttpResponse res = httpClient.execute(httpGet);
//    System.out.println(EntityUtils.toString(res.getEntity()));
//    res.close();
//
//    httpGet = new HttpGet("http://product.suning.com/SNProductStatusView?storeId=10052&catalogId=10051&productId=17857647&cityId=9017&_=0");
//    httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
//    httpGet.addHeader("Connection", "keep-alive");
//    httpGet.addHeader("Referer", "http://product.suning.com/105077896.html");
//    httpGet.addHeader("X-Requested-With", "XMLHttpRequest");
//    httpGet.addHeader("Pragma", "no-cache");
//    httpGet.addHeader("Accept", "application/json, text/javascript, */*");
//    httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//    httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
//
//    res = httpClient.execute(httpGet);
//    System.out.println(EntityUtils.toString(res.getEntity()));
//    res.close();
    Parser parser = ctx.getBean(BenlaiParser.class);
    System.out.println(parser.parse("http://www.benlai.com/item-509.html"));
	}
}
