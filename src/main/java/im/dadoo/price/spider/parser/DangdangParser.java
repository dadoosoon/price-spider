package im.dadoo.price.spider.parser;

import java.io.IOException;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class DangdangParser extends Parser {
	
	private static final String STOCK_URL_TPL = 
			"http://product.dangdang.com/pricestock/callback.php?type=stockv2&product_id=%s";
  
  public DangdangParser() {
    super();
  }
  
  @Override
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		//首先判断是否有货
		String[] ts = url.split("/");
		String pid = ts[ts.length - 1].split("\\.")[0];
    String json = this.getHtml(String.format(STOCK_URL_TPL, pid));
		
    //首先判断用户页面是否有效
    Map<String, Object> map = this.mapper.readValue(json, Map.class);
    if (map.containsKey("havestock")) {
      Object havestock = map.get("havestock");
      if (havestock instanceof String && ((String)havestock).equals("true")) {
        fruit.setStock(1);
      } else {
        fruit.setStock(0);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
    }
    
		//接下来解析价格
		String html = this.getHtml(url);
		Document doc = Jsoup.parse(html);
    Elements es = doc.select("#promo_price");
    if (es.first() == null) {
      es = doc.select("#salePriceTag");
      if (es.first() == null) {
        return fruit;
      }
    }
		String fragment = es.first().text();
		
		if (fragment != null && !fragment.equals("")) {
			Double value = null;
			if (Character.isDigit(fragment.charAt(0))) {
        value = this.parsePrice(fragment);
			}
			else {
        value = this.parsePrice(fragment.substring(1));
			}
			fruit.setPrice(value);
		} else {
      logger.error(String.format("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL));
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
		return fruit;
	}

	
}
