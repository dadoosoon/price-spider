package im.dadoo.price.spider.parser;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class WomaiParser extends Parser {
	
	private static String PREFIX = "\"priceValue\":\"";
	public static String URL = "http://price.womai.com/PriceServer/open/productlist.do?ids=%s&prices=buyPrice";
	
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		Integer endIndex = url.indexOf(".htm");
		Integer beginIndex = endIndex - 6;
		String ids = url.substring(beginIndex, endIndex);
    String json = this.getHtml(String.format(URL, ids));
		//首先判断是否有货
		if (json.indexOf("\"sellable\":true") > -1) {
			fruit.setStock(1);
		} else if (json.indexOf("\"sellable\":false") > -1){
			fruit.setStock(0);
		} else {
			logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
		}
		Integer index1 = json.indexOf(PREFIX) + PREFIX.length();
		Integer index2 = json.substring(index1).indexOf("\"");
		String result = json.substring(index1, index1 + index2);
		if (result != null && !result.equals("")) {
      Double value = this.parsePrice(result);
      fruit.setPrice(value);  
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
		return fruit;
	}

}
