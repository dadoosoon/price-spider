/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.parser;

import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 *
 * @author codekitten
 */
@Component
public class LefengParser extends Parser {
  
  private static final String STOCK_URL_TPL = 
          "http://lop.lefeng.com/product/arrtime.jsp?quantity=1&skuIds=%s&countyId=010108";
  
  @Resource
  private ObjectMapper mapper;
  
  public LefengParser() {
    super();
  }

  @Override
  public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    //解析价格
    String html = this.getHtml(url);
    Document doc = Jsoup.parse(html);
    Elements es = doc.select("#uprice");
    if (es.first() != null) {
      Element e = es.first();
      fruit.setPrice(this.parsePrice(e.attr("value")));
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
    
    //解析库存
    es = doc.select("#skuId");
    if (es.first() != null) {
      Element e = es.first();
      String skuId = e.attr("value");
      String json = this.getHtml(String.format(STOCK_URL_TPL, skuId));
      if (json != null && !json.isEmpty()) {
        Map<String, Object> map = this.mapper.readValue(json, Map.class);
        if (map != null && !map.isEmpty() && map.containsKey("retCode")) {
          Integer code = (Integer)map.get("retCode");
          if (code.equals(1)) {
            fruit.setStock(0);
          } else if (code.equals(0)) {
            fruit.setStock(1);
          } else {
            fruit.setStock(code);
          }
        } else {
          logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
          this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
        }
      } else {
        logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
        this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
    }
    return fruit;
  }
  
}
