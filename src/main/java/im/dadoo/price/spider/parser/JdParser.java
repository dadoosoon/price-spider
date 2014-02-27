package im.dadoo.price.spider.parser;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;

import org.springframework.stereotype.Component;

@Component
public class JdParser extends Parser {
	
  private static final String PRICE_URL = "http://p.3.cn/prices/get?skuid=J_%s";
  
  private static final String STOCK_URL = 
          "http://st.3.cn/gds.html?skuid=%s&provinceid=1&cityid=2800&areaid=2849";
  
  private static final String SKUID_KEY_PREFIX = "skuidkey:\'";
  
  private static final String SUFFIX = "\'";
  
  @Resource
  private ObjectMapper mapper;
  
	public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    //解析价格
		String[] st = url.split("/");
		String pid = st[st.length - 1].split("\\.")[0];
    String json = this.getHtml(String.format(PRICE_URL, pid));
    if (json != null) {
      List<Map<String, Object>> list = this.mapper.readValue(json, List.class);
      if (list != null && !list.isEmpty()) {
        Map<String, Object> map = list.get(0);
        if (map.containsKey("p")) {
          Double price = this.parsePrice((String)map.get("p"));
          if (price < 0.0) {
            fruit.setPrice(null);
          } else {
            fruit.setPrice(price);
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
		
    //解析库存
    String html = this.getHtml(url);
    String skuidKey = this.parsePrefix(html, SKUID_KEY_PREFIX, SUFFIX);
    if (skuidKey != null && skuidKey.length() == 32) {
      json = this.getHtml(String.format(STOCK_URL, skuidKey));
      if (json != null) {
        Map<String, Object> map = this.mapper.readValue(json, Map.class);
        if (map != null && map.containsKey("stock")) {
          map = (Map<String, Object>)map.get("stock");
          if (map != null && map.containsKey("StockState")) {
            Integer state = (Integer)map.get("StockState");
            if (state.equals(33)) {
              fruit.setStock(1);
            } else {
              fruit.setStock(0);
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
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
    }
		return fruit;
	}
}
