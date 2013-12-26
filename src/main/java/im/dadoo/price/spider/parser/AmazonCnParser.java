package im.dadoo.price.spider.parser;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AmazonCnParser extends Parser {
	
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		Document doc = Jsoup.connect(url).timeout(Parser.TIME_OUT).get();
		Elements es = doc.select("#actualPriceValue .priceLarge");
		if (es.first() != null) {
			String html = es.first().text();
			if (html != null && !html.equals("")) {
				Double value = Double.parseDouble(html.substring(2));
				fruit.setValue(value);
			}
		} else {
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse res = httpclient.execute(httpGet);
			HttpEntity entity = res.getEntity();
			String fragment = EntityUtils.toString(entity);
			doc = Jsoup.parseBodyFragment(fragment);
			es = doc.select(".ddm-sbr-avail-title");
			if (es.text() != null && es.text().equals("缺货登记")) {
				logger.info(String.format("缺货:%s", url));
				fruit = new Fruit(null, 0);
			} else {
        logger.error(String.format("url:%s,%s", url, Parser.Log_PARSE_STOCK_FAIL));
        this.sendFailureLog(url, "AmazonCnParser", Parser.Log_PARSE_STOCK_FAIL);
      }
      res.close();
		}
		return fruit;
	}

}
