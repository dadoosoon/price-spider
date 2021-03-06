package im.dadoo.price.spider.parser;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BenlaiParser extends Parser{

  private static final String REAL_URL_TPL = "http://www.benlai.com/Products/GetProductPrice?SysNo=%s";
  
  public BenlaiParser() {
    super();
  }
  
	@Override
	public Fruit parse(String url) throws IOException {
    Fruit fruit = new Fruit();
    
    Integer i1 = url.indexOf("-");
    Integer i2 = url.indexOf(".html");
    String pid = url.substring(i1 + 1, i2);
    String json = this.getHtml(String.format(REAL_URL_TPL, pid));
    logger.debug(json);
    Map<String, Object> map = this.mapper.readValue(json, Map.class);
    if (map != null && map.containsKey("currentPrice")) {
      fruit.setPrice((Double)map.get("currentPrice"));
    } else {
      logger.error(String.format("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL));
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
    
    if (map != null && map.containsKey("VisType")) {
      Integer visType = (Integer) map.get("VisType");
      if (visType == 0) {
        fruit.setStock(1);
      } else {
        fruit.setStock(0);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
    }
		return fruit;
	}

}
