package im.dadoo.price.spider.parser;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class AmazonCnParser extends Parser {
	
  public AmazonCnParser() {
    super();
  }
  
  /**
   *
   * @param url
   * @return
   * @throws IOException
   */
  @Override
	public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    //首先抽取价格
    String html = this.getHtml(url);
    Document doc = Jsoup.parse(html);
		Elements es = doc.select("#actualPriceValue .priceLarge");
		if (es.first() != null) {
			String fragment = es.first().text();
			if (fragment != null && !fragment.equals("")) {
        Double value = this.parsePrice(fragment.substring(2));
				fruit.setPrice(value);
        fruit.setStock(1);
			}
		} else {
			es = doc.select(".ddm-sbr-avail-title");
			if (es.text() != null && es.text().equals("缺货登记")) {
        fruit.setPrice(null);
				fruit.setStock(0);
			} else {
        logger.error(String.format("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL));
        this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
      }
		}
		return fruit;
	}

}
