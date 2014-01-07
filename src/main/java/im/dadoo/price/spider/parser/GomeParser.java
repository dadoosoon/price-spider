package im.dadoo.price.spider.parser;


import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class GomeParser extends Parser {
	
	private static String PREFIX = "price:\"";
	
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
    
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(this.config);
		CloseableHttpResponse res = this.httpClient.execute(httpGet);
		HttpEntity entity = res.getEntity();
		String html = EntityUtils.toString(entity);
		
		Integer index1 = html.indexOf(PREFIX) + PREFIX.length();
		Integer index2 = html.substring(index1).indexOf("\"");
		String result = html.substring(index1, index1 + index2);
		
    Double value = this.parserValue(result);
		fruit.setPrice(value);
		fruit.setStock(1);
		return fruit;
	}

}
