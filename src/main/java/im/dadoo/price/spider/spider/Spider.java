package im.dadoo.price.spider.spider;

import im.dadoo.log.Log;
import im.dadoo.log.LogMaker;
import java.util.ArrayList;
import java.util.List;

import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.core.domain.Link;
import im.dadoo.price.core.domain.Record;
import im.dadoo.price.core.domain.Seller;
import im.dadoo.price.core.service.LinkService;
import im.dadoo.price.core.service.RecordService;
import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.parser.Fruit;
import im.dadoo.price.spider.parser.Parser;
import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Spider {

	private static final Logger logger = LoggerFactory.getLogger(Spider.class);
	
	@Autowired
	private LoggerClient loggerClient;
  
  @Autowired
  private CloseableHttpClient httpClient; 
  
  @Autowired
  private ObjectMapper mapper;
	
	@Autowired
	private LinkService linkService;
	
	@Autowired
	private RecordService recordService;
	
	@Autowired
	private Parser jdParser;
	
	@Autowired
	private Parser amazonCnParser;
	
	@Autowired
	private Parser yhdParser;
	
	@Autowired
	private Parser womaiParser;
	
	@Autowired
	private Parser sfbestParser;
	
	@Autowired
	private Parser yixunParser;
	
	@Autowired
	private Parser gomeParser;
	
	@Autowired
	private Parser dangdangParser;
	
	@Autowired
	private Parser tooTooParser;
	
	@Autowired
	private Parser benlaiParser;
	
	public void start() {

    Long beginTime = null;
    Long parseEndTime = null;
    Long storeEndTime = null;
		while(true) {
      Link link = this.getLink();
      if (link == null) continue;
      try {
        Parser parser = this.choose(link.getSeller());
        if (parser != null) {
          logger.info(String.format("开始采集%s网站数据,商品名%s,总量%d,url为%s", 
              link.getSeller().getName(), link.getProduct().getName(), link.getAmount(), link.getUrl()));
          beginTime = System.currentTimeMillis();
          Fruit fruit = parser.parse(link.getUrl());
					parseEndTime = System.currentTimeMillis();
          if (fruit != null) {
            Double price = null;
						if (fruit.getPrice() != null) {
							price = fruit.getPrice() / link.getAmount();
						}
            Record record = Record.create(price, fruit.getStock(), link, parseEndTime);
            Boolean success = this.save(record);
            storeEndTime = System.currentTimeMillis();
            if (success) {
              logger.info(String.format("采集%s网站成功,商品名为%s,单价为%2.2f,库存状况%d,采集耗时%d毫秒,存储耗时%d毫秒", 
								link.getSeller().getName(), link.getProduct().getName(), price, fruit.getStock(), 
                parseEndTime - beginTime, storeEndTime - parseEndTime));
              parser.sendExtractionLog(record, storeEndTime - parseEndTime);
            } else {
              logger.error("manager保存失败");
            }
          }
        }
      } catch(Exception e) {
        storeEndTime = System.currentTimeMillis();
        String description = String.format("采集%s结束,商品名为%s,价格解析失败,共耗时%d毫秒", 
            link.getUrl(), link.getProduct().getName(), storeEndTime - beginTime);
        logger.error(description);
        e.printStackTrace();
        Log log = LogMaker.makeExceptionLog(Constants.SERVICE_NAME, description, e);
        this.loggerClient.send(log);
      }
    }
	}
	
	private Parser choose(Seller seller) {
		if (seller.getName().equals("京东")) {
			return this.jdParser;
		} else if (seller.getName().equals("一号店")) {
			return this.yhdParser;
		} else if (seller.getName().equals("亚马逊中国")) {
			return this.amazonCnParser;
		} else if (seller.getName().equals("顺丰优选")) {
			return this.sfbestParser;
		} else if (seller.getName().equals("易迅")) {
			return this.yixunParser;
		} else if (seller.getName().equals("中粮我买网")) {
			return this.womaiParser;
		} else if (seller.getName().equals("国美在线")) {
			return this.gomeParser;
		} else if (seller.getName().equals("当当")) {
			return this.dangdangParser;
		} else if (seller.getName().equals("沱沱工社")) {
			return this.tooTooParser;
		} else if (seller.getName().equals("本来生活")) {
			return this.benlaiParser;
		}
		else {
			return null;
		}
	}
  
  private Link getLink() {
    Link link = null;

    RequestConfig config = RequestConfig.custom().setConnectTimeout(Constants.TIME_OUT)
            .setSocketTimeout(Constants.TIME_OUT).build();
    HttpGet httpGet = new HttpGet("http://localhost:8080/price-spider-manager/center");
    httpGet.setConfig(config);
    
		CloseableHttpResponse res;
    try {
      res = this.httpClient.execute(httpGet);
      HttpEntity entity = res.getEntity();
      String json = EntityUtils.toString(entity);
      res.close();
      link = this.mapper.readValue(json, Link.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
		return link;
  }
  
  private Boolean save(Record record) {
    Boolean success = false;
    RequestConfig config = RequestConfig.custom().setConnectTimeout(Constants.TIME_OUT)
            .setSocketTimeout(Constants.TIME_OUT).build();
    HttpPost httpPost = new HttpPost("http://localhost:8080/price-spider-manager/center");
    httpPost.setConfig(config);
    
		CloseableHttpResponse res;
    try {
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      String json = this.mapper.writeValueAsString(record);
      
      nvps.add(new BasicNameValuePair("record", json));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
      res = this.httpClient.execute(httpPost);
      HttpEntity entity = res.getEntity();
      json = EntityUtils.toString(entity);
      logger.info(json);
      res.close();
      success = this.mapper.readValue(json, Boolean.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return success;
  }
}
