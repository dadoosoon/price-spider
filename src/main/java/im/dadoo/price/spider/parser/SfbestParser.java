package im.dadoo.price.spider.parser;


import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class SfbestParser extends Parser {
  
  private static final String PID_PREFIX = "cartAdd(";
	
	private static final String PRICE_URL = "http://www.sfbest.com/product/price";
	
	private static final String STOCK_URL = "http://www.sfbest.com/product/stock";
	
  private static final String SUFFIX = ",";
  
  @Override
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
    //获取product_id
    String html = this.getHtml(url);
    String pid = this.parsePrefix(html, PID_PREFIX, SUFFIX);
		//首先判断是否有货
		HttpPost httpPost = new HttpPost(STOCK_URL);
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("product_id", pid));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse res = Parser.httpClient.execute(httpPost);
		HttpEntity entity = res.getEntity();
		String fragment = EntityUtils.toString(entity);
    //若fragment为null证明获取库存方式已失效
    if (fragment != null) {
      if (fragment.indexOf("现货") > -1) {
        fruit.setStock(1);
      } else {
        fruit.setStock(0);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
    }
		res.close();
		
		//然后解析价格
		httpPost = new HttpPost(PRICE_URL);
		nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("product_id", pid));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		res = Parser.httpClient.execute(httpPost);
		entity = res.getEntity();
		fragment = EntityUtils.toString(entity);
		Document doc = Jsoup.parseBodyFragment(fragment);
		Elements es = doc.select("#price font");
    if (es.first() != null) {
      html = es.first().text();
      if (html != null && !html.equals("")) {
        Double value = this.parsePrice(html);
        fruit.setPrice(value);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
		res.close();
		return fruit;
	}

}
