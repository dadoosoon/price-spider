package im.dadoo.price.spider.parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author codekitten
 */
@Component
public class YintaiParser extends Parser {

  private static final String TAG_PREFIX = "var YinTai_TagData= ";
  private static final String TAG_SUFFIX = "<";
  
  public YintaiParser() {
    super();
  }
  
  @Override
  public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    String html = this.getHtml(url);
    String json = this.parsePrefix(html, TAG_PREFIX, TAG_SUFFIX).trim();
    Map<String, Object> map = this.mapper.readValue(json, Map.class);
    if (map != null && !map.isEmpty() && map.containsKey("prods")) {
      List<Map<String, Object>> prods = (List<Map<String, Object>>)map.get("prods");
      if (prods != null && !prods.isEmpty()) {
        map = prods.get(0);
        if (map != null && !map.isEmpty()) {
          if (map.containsKey("price")) {
            fruit.setPrice((Double)map.get("price"));
          } else {
            logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
            this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
          }
          if (map.containsKey("stock")) {
            Double stock = (Double)map.get("stock");
            if (stock < 1.00) {
              fruit.setStock(0);
            } else {
              fruit.setStock(1);
            }
          } else {
            logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
            this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
          }
        } else {
          logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
          this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
        }
      } else {
        logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
        this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
    return fruit;
  }
  
}
