package im.dadoo.price.spider.parser;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class DangdangParser extends Parser {
	
	private static final String STOCK_URL = 
			"http://product.dangdang.com/pricestock/callback.php?type=stockv2&product_id=%s";
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		//首先判断是否有货
		String[] ts = url.split("/");
		String pid = ts[ts.length - 1].split("\\.")[0];
		HttpGet httpGet = new HttpGet(String.format(STOCK_URL, pid));
		CloseableHttpResponse res = Parser.httpclient.execute(httpGet);
		HttpEntity entity = res.getEntity();
		String fragment = EntityUtils.toString(entity);
		
    //首先判断用户页面是否有效
    if (fragment.indexOf("havestock") == -1) {
      logger.error("url:%s,%s", url, Parser.Log_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, "DangdangParser", Parser.Log_PARSE_STOCK_FAIL);
    } else {
      if (-1 < fragment.indexOf("\"havestock\":true")) {
        fruit.setStock(1);
      } else {
        fruit.setStock(0);
      }
    }
		res.close();
		//接下来解析价格
		Document doc = Jsoup.connect(url).timeout(Parser.TIME_OUT).get();
		Elements es = doc.select("#salePriceTag");
		String html = es.first().text();
		
		if (html != null && !html.equals("")) {
			Double value = null;
			if (Character.isDigit(html.charAt(0))) {
				value = Double.parseDouble(html);
			}
			else {
				value = Double.parseDouble(html.substring(1));
			}
			fruit.setValue(value);
		} else {
      logger.error(String.format("url:%s,%s", url, Parser.Log_PARSE_VALUE_FAIL));
      this.sendFailureLog(url, "DangdangParser", Parser.Log_PARSE_VALUE_FAIL);
    }
		return fruit;
	}

	
}
