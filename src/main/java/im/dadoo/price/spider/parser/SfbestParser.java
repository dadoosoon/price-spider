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
	
	private static final String PRICE_URL = "http://www.sfbest.com/product/price";
	
	private static final String STOCK_URL = "http://www.sfbest.com/product/stock";
	
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		String[] ts = url.split("/");
		String pid = ts[ts.length - 1].split("\\.")[0];
		//首先判断是否有货
		HttpPost httpPost = new HttpPost(STOCK_URL);
    httpPost.setConfig(this.config);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("product_id", pid.substring(pid.length() - 5)));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse res = this.httpClient.execute(httpPost);
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
      this.sendFailureLog(url, "SfbestParser", Parser.LOG_PARSE_STOCK_FAIL);
    }
		res.close();
		
		//然后解析价格
		httpPost = new HttpPost(PRICE_URL);
    httpPost.setConfig(this.config);
		nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("product_id", pid.substring(pid.length() - 5)));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		res = this.httpClient.execute(httpPost);
		entity = res.getEntity();
		fragment = EntityUtils.toString(entity);
		Document doc = Jsoup.parseBodyFragment(fragment);
		Elements es = doc.select("#price font");
    if (es.first() != null) {
      String html = es.first().text();
      if (html != null && !html.equals("")) {
        Double value = this.parsePrice(html);
        fruit.setPrice(value);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, "SfbestParser", Parser.LOG_PARSE_VALUE_FAIL);
    }
		res.close();
		return fruit;
	}

}
