/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.parser;

import im.dadoo.price.spider.cons.Constants;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 *
 * @author codekitten
 */
@Component
public class JumeiParser extends Parser{

  @Override
  public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    Document doc = Jsoup.connect(url).timeout(Constants.TIME_OUT).get();
    Elements es = doc.select("#mall_price");
    if (es.first() != null) {
      String html = es.first().text();
      fruit.setPrice(this.parsePrice(html));
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
    
    es = doc.select("#add_to_shoppingcart");
    if (es.first() != null) {
      fruit.setStock(1);
    } else {
      fruit.setStock(0);
    }
    return fruit;
  }
 
}
