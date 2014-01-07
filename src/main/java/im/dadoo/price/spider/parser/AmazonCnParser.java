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
public class AmazonCnParser extends Parser {
	
  public AmazonCnParser() {
    super();
  }
  
	public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    //首先抽取价格
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(this.config);
    CloseableHttpResponse res = httpClient.execute(httpGet);
    HttpEntity entity = res.getEntity();
    String html = EntityUtils.toString(entity);
		res.close();
    Document doc = Jsoup.parse(html);
		Elements es = doc.select("#actualPriceValue .priceLarge");
		if (es.first() != null) {
			String fragment = es.first().text();
			if (fragment != null && !fragment.equals("")) {
        Double value = this.parserValue(html.substring(2));
				fruit.setPrice(value);
        fruit.setStock(1);
			}
		} else {
			es = doc.select(".ddm-sbr-avail-title");
			if (es.text() != null && es.text().equals("缺货登记")) {
				logger.info(String.format("缺货:%s", url));
				fruit.setStock(0);
			} else {
        logger.error(String.format("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL));
        this.sendFailureLog(url, "AmazonCnParser", Parser.LOG_PARSE_STOCK_FAIL);
      }
		}
		return fruit;
	}

}
