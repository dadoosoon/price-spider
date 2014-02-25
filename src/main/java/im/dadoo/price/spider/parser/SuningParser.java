package im.dadoo.price.spider.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuningParser extends Parser{
  
  private static final String PID_PREFIX = "productId\":\"";
	
  private static final String PRICE_URL_TPL = "http://product.suning.com/SNProductStatusView?storeId=10052&catalogId=10051&productId=%s&cityId=9017&_=%d";
	
  private static final String STOCK_URL_TPL = "http://product.suning.com/SNProductSaleView?storeId=10052&catalogId=10051&productId=%s&cityId=9017&salesOrg=%s&deptNo=%s&vendor=%s";
  
  private ObjectMapper mapper;
  
  public SuningParser() {
    this.mapper = new ObjectMapper();
  }
  
  @Override
  public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    String html = this.getHtml(url);
    String pid = this.parsePrefix(html, PID_PREFIX);
    
    //解析价格
    String priceUrl = String.format(PRICE_URL_TPL, pid, 0);
    Map<String, String> headers = new HashMap<>();
    headers.put("Referer", url);
    String json = StringUtils.deleteWhitespace(this.getHtml(priceUrl, headers));
    System.out.println(json);
    Map<String, String> map = this.mapper.readValue(json, Map.class);
    if (map != null && map.containsKey("promotionPrice")) {
      fruit.setPrice(this.parsePrice(map.get("promotionPrice")));
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
    
    if (map != null) {
      String stockUrl = String.format(STOCK_URL_TPL, pid,
              map.get("salesOrg"), map.get("deptNo"), map.get("vendor"));
      json = StringUtils.deleteWhitespace(this.getHtml(stockUrl));
      System.out.println(json);
      map = this.mapper.readValue(json, Map.class);
      if (map != null && map.containsKey("productStatus")) {
        String flag = map.get("productStatus");
        switch (flag) {
          case "1":
            fruit.setStock(1);
            break;
          case "2":
            fruit.setStock(0);
            break;
          default:
            logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
            this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
            break;
        }
      } else {
        logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
        this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
      }
    }
    
		return fruit;
	}

  private String parsePrefix(String html, String prefix) {
    Integer index1 = html.indexOf(prefix) + prefix.length();
		Integer index2 = html.substring(index1).indexOf("\"");
		return html.substring(index1, index1 + index2);
  }
  
}
