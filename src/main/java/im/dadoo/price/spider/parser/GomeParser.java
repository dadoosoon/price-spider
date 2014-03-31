package im.dadoo.price.spider.parser;


import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;

import org.springframework.stereotype.Component;

@Component
public class GomeParser extends Parser {
	
  @Resource
  private ObjectMapper mapper;
	
  private static final String PID_PREFIX = "prdId:";
	private static final String SID_PREFIX = "sku:\"";
  private static final String GOODS_NO_PREFIX = "skuNo:\"";
  private static final String SITE_ID_PREFIX = "siteId:\"";
  private static final String SKU_TYPE_PREFIX = "skuType:\"";
  private static final String SHELF_PREFIX = "shelf:\"";
  
  private static final String STOCK_PREFIX = "result:\"";
  private static final String PRICE_PREFIX = "price:\"";
  
  private static final String SUFFIX = "\"";
  
  private static final String NEW_URL_TPL = 
          "http://g.gome.com.cn/ec/homeus/browse/exactMethod.jsp?"
          + "callback=exact&"
          + "goodsNo=%s&"
          + "city=11011400&"
          + "siteId_p=%s&"
          + "skuType_p=%s&"
          + "shelfCtgy3=%s&"
          + "zoneId=11010000&"
          + "sid=%s&"
          + "pid=%s";
  
  public GomeParser() {
    super();
  }
  
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
    
    String html = this.getHtml(url);
		
		String pid = this.parsePrefix(html, PID_PREFIX, SUFFIX);
    String sid = this.parsePrefix(html, SID_PREFIX, SUFFIX);
    String goodsNo = this.parsePrefix(html, GOODS_NO_PREFIX, SUFFIX);
    String siteId = this.parsePrefix(html, SITE_ID_PREFIX, SUFFIX);
    String skuType = this.parsePrefix(html, SKU_TYPE_PREFIX, SUFFIX);
    String shelf = this.parsePrefix(html, SHELF_PREFIX, SUFFIX);
    
    String newUrl = String.format(NEW_URL_TPL, goodsNo, siteId, skuType, shelf, sid, pid);
    String fragment = this.getHtml(newUrl);
    
    Integer index1 = fragment.indexOf("{");
    Integer index2 = fragment.lastIndexOf("}");
    fragment = fragment.substring(index1, index2 + 1);
    Map<String, String> map = this.mapper.readValue(fragment, Map.class);
    if (map != null) {
      //解析stock
      if (map.containsKey("result")) {
        String stock = map.get("result");
        if (stock.equals("Y")) {
          fruit.setStock(1);
        } else if (stock.equals("N")) {
          fruit.setStock(0);
        } else {
          logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
          this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
        }
      } else {
        logger.error("url:%s,%s", url, Parser.LOG_PARSE_STOCK_FAIL);
        this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_STOCK_FAIL);
      }
      
      //解析price
      if (map.containsKey("price")) {
        String price = map.get("price");
        fruit.setPrice(this.parsePrice(price));
      } else {
        logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
        this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
      }
    }
		return fruit;
	}
}
