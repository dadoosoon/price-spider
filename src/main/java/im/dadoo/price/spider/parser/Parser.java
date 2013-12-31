package im.dadoo.price.spider.parser;

import im.dadoo.log.Log;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.domain.Price;
import im.dadoo.price.spider.cons.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Parser {

	protected static final Logger logger = LoggerFactory.getLogger(Parser.class);
	
	public static final Integer PERIOD = 900000;
	public static final Integer TIME_OUT = 60000;
	
	public static final String PRICE = "price";
	public static final String TIME = "time";
	
  public static final String Log_PARSE_VALUE_FAIL = "解析价格失败,可能页面已被修改";
  
  public static final String Log_PARSE_STOCK_FAIL = "解析库存失败,可能页面已被修改";
  
	@Autowired
	protected LoggerClient loggerClient;
	
	protected static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public abstract Fruit parse(String url) throws IOException;
	
  protected Double parserValue(String html) {
    Double value = null;
    try {
      value = Double.parseDouble(html);
    } catch(NumberFormatException e) {}
    return value;
  }
  
	public void sendExtractionLog(Price price, Long time) {
		Map<String, Object> content = new HashMap<String, Object>();
		content.put(Parser.PRICE, price);
		content.put(Parser.TIME, time);
		Log log = new Log(Constants.SERVICE_NAME, 
				Constants.TYPE_EXTRACTION, System.currentTimeMillis(), content);
		this.loggerClient.send(log);
	}
  
  public void sendFailureLog(String url, String parserName, String description) {
    Map<String, Object> content = new HashMap<String, Object>();
    content.put("url", url);
    content.put("parserName", parserName);
    content.put("description", description);
    Log log = new Log(Constants.SERVICE_NAME, Constants.TYPE_PARSE_FAIL, 
            System.currentTimeMillis(), content);
    this.loggerClient.send(log);
  }
}
