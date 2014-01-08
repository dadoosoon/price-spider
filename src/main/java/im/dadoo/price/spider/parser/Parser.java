package im.dadoo.price.spider.parser;

import im.dadoo.log.Log;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.core.domain.Record;
import im.dadoo.price.spider.cons.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Parser {

	protected static final Logger logger = LoggerFactory.getLogger(Parser.class);
	
	public static final String RECORD = "record";
	public static final String TIME = "time";
	
  public static final String LOG_PARSE_VALUE_FAIL = "解析价格失败,可能页面已被修改";
  
  public static final String LOG_PARSE_STOCK_FAIL = "解析库存失败,可能页面已被修改";
  
	@Autowired
	protected LoggerClient loggerClient;
	
  @Autowired
	protected CloseableHttpClient httpClient;
	
  protected RequestConfig config;
  
	public abstract Fruit parse(String url) throws IOException;
	
  public Parser() {
    config = RequestConfig.custom().setConnectTimeout(Constants.TIME_OUT)
            .setSocketTimeout(Constants.TIME_OUT).build();
  }
  
  protected String getHtml(String url) throws IOException {
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(this.config);
    
		CloseableHttpResponse res = this.httpClient.execute(httpGet);
		HttpEntity entity = res.getEntity();
		String html = EntityUtils.toString(entity);
    res.close();
    return html;
  }
  
  protected Double parserValue(String html) {
    Double value = null;
    try {
      value = Double.parseDouble(html);
    } catch(NumberFormatException e) {
      logger.error("价格解析失败");
      e.printStackTrace();
    }
    return value;
  }
  
	public void sendExtractionLog(Record record, Long time) {
		Map<String, Object> content = new HashMap<String, Object>();
		content.put(Parser.RECORD, record);
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
