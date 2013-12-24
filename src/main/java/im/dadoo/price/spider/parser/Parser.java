package im.dadoo.price.spider.parser;

import im.dadoo.logger.client.DadooLog;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.core.domain.Price;
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
	public static final Integer TIME_OUT = PERIOD / 2;
	
	public static final String PRICE = "price";
	public static final String TIME = "time";
	
	@Autowired
	protected LoggerClient loggerClient;
	
	protected static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public abstract Fruit parse(String url) throws IOException;
	
	public void sendExtractionLog(Price price, Long time) {
		Map<String, Object> content = new HashMap<String, Object>();
		content.put(Parser.PRICE, price);
		content.put(Parser.TIME, time);
		DadooLog log = new DadooLog(Constants.SERVICE_NAME, 
				Constants.TYPE_EXTRACTION, System.currentTimeMillis(), content);
		this.loggerClient.send(log);
	}
  
}
