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
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(
//              new String[]{"dadoo-mq-context.xml",
//                "logger-client-context.xml", 
//                "price-core-context.xml",
//                "price-spider-context.xml"});
    RequestConfig globalConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.BEST_MATCH)
        .build();
    CloseableHttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(globalConfig)
        .build();
    Long time = System.currentTimeMillis();
    HttpGet httpGet = new HttpGet("http://product.suning.com/101991660.html");
    
    
    CloseableHttpResponse res = httpClient.execute(httpGet);
    System.out.println();
    EntityUtils.consume(res.getEntity());
    res.close();
    
    //
   
    httpGet = new HttpGet("http://product.suning.com/SNProductStatusView?storeId=10052&catalogId=10051&productId=412356&cityId=9017&_=0");
    httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
    httpGet.addHeader("Connection", "keep-alive");
    httpGet.addHeader("Referer", "http://product.suning.com/102647171.html");
    httpGet.addHeader("X-Requested-With", "XMLHttpRequest");
    httpGet.addHeader("Pragma", "no-cache");
    httpGet.addHeader("Accept", "application/json, text/javascript, */*");
    httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");
    httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
    //httpGet.addHeader("Cookie", "JSESSIONID=0000FdClVVJhAVvKsb69cb2U8bf:17atejorj; cityId=9017; districtId=10106; WC_SESSION_ESTABLISHED=true; WC_PERSISTENT=U52mYuCR3cw6P93XEUCKPc02gMY%3d%0a%3b2014%2d01%2d16+17%3a32%3a50%2e16%5f1389864770156%2d1185656%5f0; WC_ACTIVEPOINTER=%2d7%2c0; WC_USERACTIVITY_-1002=%2d1002%2c0%2cnull%2cnull%2cnull%2cnull%2cnull%2cnull%2cnull%2cnull%2c%2b3UNwud3Wht5gFoCkweL8dCiZjSVjhFgqwVeUBad6U0T66dAcwzgZjcvT5HGk4CcJtSKiaSd6iKK%0aVsySYwdR5V7OXYeqOeAOM%2fyT8oPwN7HcH7t7Ivfs8nSxt%2fY%2bxTv%2blCzswXRTU02%2flqy772Pd9A%3d%3d; WC_GENERIC_ACTIVITYDATA=[20000026664514364%3atrue%3afalse%3a0%3aaiL6c4qDzk3k9Ny98bPlBLBX%2f4U%3d][com.ibm.commerce.context.audit.AuditContext|1389864770156%2d1185656][com.ibm.commerce.store.facade.server.context.StoreGeoCodeContext|null%26null%26null%26null%26null%26null][CTXSETNAME|Store][com.ibm.commerce.context.globalization.GlobalizationContext|%2d7%26null%26%2d7%26null][com.ibm.commerce.catalog.businesscontext.CatalogContext|10051%26null%26false%26false%26false][com.suning.commerce.context.common.SNContext|9017%26%2d1%26null%26159%2e226%2e43%2e61%252C%2b61%2e135%2e175%2e80%26null%26null%26null%26null%26null%26null%26null%26null%26null%26null%26null%26null%26null%26null][com.ibm.commerce.context.base.BaseContext|0%26%2d1002%26%2d1002%26%2d1][com.ibm.commerce.context.experiment.ExperimentContext|null][com.ibm.commerce.context.entitlement.EntitlementContext|null%26null%26null%26%2d2000%26null%26null%26null][com.ibm.commerce.giftcenter.context.GiftCenterContext|null%26null%26null]; smhst=2327661");
    Header[] headers = httpGet.getAllHeaders();
    for (Header header : headers) {
      System.out.println(header.getName());
      System.out.println(header.getValue());
    }
    res = httpClient.execute(httpGet);
    System.out.println(EntityUtils.toString(res.getEntity()));
    res.close();
	}
}
