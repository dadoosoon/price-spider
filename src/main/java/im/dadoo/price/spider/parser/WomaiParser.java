package im.dadoo.price.spider.parser;


import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class WomaiParser extends Parser {
	
	private static final String PREFIX = "\"priceValue\":\"";
  private static final String SUFFIX = "\"";
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

    String result = this.parsePrefix(json, PREFIX, SUFFIX);
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
