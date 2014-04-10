package im.dadoo.price.spider.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.dadoo.log.Log;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.core.domain.Record;
import im.dadoo.price.spider.cons.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Parser {

	protected static final Logger logger = LoggerFactory.getLogger(Parser.class);
	
	public static final String RECORD = "record";
	public static final String TIME = "time";
	
  public static final String LOG_PARSE_VALUE_FAIL = "解析价格失败,可能页面已被修改";
  
  public static final String LOG_PARSE_STOCK_FAIL = "解析库存失败,可能页面已被修改";
  
  protected static CloseableHttpClient httpClient;
  
	@Resource
	protected LoggerClient loggerClient;
  
  @Resource
  protected ObjectMapper mapper;
	
	static {
    RequestConfig globalConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.BEST_MATCH)
        .setConnectTimeout(Constants.TIME_OUT)
        .setSocketTimeout(Constants.TIME_OUT)
        .build();
    httpClient = HttpClients.custom()
        .setDefaultRequestConfig(globalConfig)
        .build();
  }
  
  public Parser() {
  }
  
  public abstract Fruit parse(String url) throws IOException;
  
  protected String getHtml(String url) throws IOException {
    HttpGet httpGet = new HttpGet(url.trim());
    httpGet.addHeader("User-Agent", "Chrome/33.0.1750.154 Safari/537.36");
		CloseableHttpResponse res = Parser.httpClient.execute(httpGet);
		HttpEntity entity = res.getEntity();
		String html = EntityUtils.toString(entity);
    res.close();
    return html;
  }
  
  protected String getHtml(String url, Map<String, String> headers) throws IOException {
    HttpGet httpGet = new HttpGet(url.trim());
    for (String key : headers.keySet()) {
      httpGet.addHeader(key, headers.get(key));
    }
    httpGet.addHeader("User-Agent", "Chrome/33.0.1750.154 Safari/537.36");
		CloseableHttpResponse res = Parser.httpClient.execute(httpGet);
		HttpEntity entity = res.getEntity();
		String html = EntityUtils.toString(entity);
    res.close();
    return html;
  }
  
  protected Double parsePrice(String html) {
    Double price = null;
    try {
      if (html != null && !html.trim().isEmpty()) {
        price = Double.parseDouble(html.trim());
      }
    } catch(NumberFormatException e) {
      logger.error("价格解析失败", e);
    }
    return price;
  }
  
  public String parsePrefix(String html, String prefix, String suffix) {
    Integer index1 = html.indexOf(prefix) + prefix.length();
		Integer index2 = html.substring(index1).indexOf(suffix);
		return html.substring(index1, index1 + index2);
  }
  
	public void sendExtractionLog(Record record, Long time) {
		Map<String, Object> content = new HashMap<>();
		content.put(Parser.RECORD, record);
		content.put(Parser.TIME, time);
		Log log = new Log(Constants.SERVICE_NAME, 
				Constants.TYPE_EXTRACTION, System.currentTimeMillis(), content);
		this.loggerClient.send(log);
	}
  
  public void sendFailureLog(String url, String parserName, String description) {
    Map<String, Object> content = new HashMap<>();
    content.put("url", url);
    content.put("parserName", parserName);
    content.put("description", description);
    Log log = new Log(Constants.SERVICE_NAME, Constants.TYPE_PARSE_FAIL, 
            System.currentTimeMillis(), content);
    this.loggerClient.send(log);
  }
}
