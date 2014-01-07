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
public class BenlaiParser extends Parser{

	@Override
	public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(this.config);
    CloseableHttpResponse res = httpClient.execute(httpGet);
    HttpEntity entity = res.getEntity();
    String html = EntityUtils.toString(entity);
		res.close();
		Document doc = Jsoup.parse(html);
		Elements es = doc.select(".newprice span");
		if (es.first() != null) {
			String fragment = es.first().ownText();
      if (fragment != null) {
        Double value = this.parserValue(fragment.substring(1));
        fruit.setPrice(value);
        fruit.setStock(1);
      } else {
        fruit.setPrice(null);
        fruit.setStock(0);
      }
		} 
		return fruit;
	}

}
