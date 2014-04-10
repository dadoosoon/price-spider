/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.spider;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.dadoo.log.Log;
import im.dadoo.log.LogMaker;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.core.domain.Link;
import im.dadoo.price.core.domain.Product;
import im.dadoo.price.core.domain.Record;
import im.dadoo.price.core.domain.Seller;
import im.dadoo.price.core.service.ProductService;
import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.parser.Fruit;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.util.ParserSelector;
import java.io.IOException;
import javax.annotation.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author codekitten
 */
@Scope("prototype")
@Component
public class SpiderTask implements Runnable {

  protected static final Logger logger = LoggerFactory.getLogger(SpiderTask.class);
  
  private Seller seller;
  
  private CloseableHttpClient httpClient;
  
  private Parser parser;
  
  @Resource
  private ParserSelector selector;
  
  @Resource
	private LoggerClient loggerClient;
  
  @Resource
  private ObjectMapper mapper;
  
  @Resource
  private ProductService productService;
  
  public SpiderTask() {
  }
  
  public void init(Seller seller) {
    this.seller = seller;
    this.httpClient = HttpClients.createDefault();
    this.parser = this.selector.select(seller.getId());
  }
  
  @Override
  public void run() {
    Long beginTime = null;
    Long parseEndTime = null;
    Long storeEndTime = null;
    
    while(true) {
      Link link = this.getLink();
      if (link == null) {
        logger.warn(String.format("获取到的link为null,%s", this.seller.getName()));
        try {
          Thread.sleep(2000);
        } catch (InterruptedException ex) {
          logger.error("线程等待失败", ex);
        }
      } else {
        Product product = this.productService.findById(link.getProductId());
        try {
          if (parser != null) {
            logger.info(String.format("开始采集%s网站数据,商品名%s,总量%d,url为%s", 
              this.seller.getName(), product.getName(), link.getAmount(), link.getUrl()));
            beginTime = System.currentTimeMillis();
            Fruit fruit = this.parser.parse(link.getUrl());
            parseEndTime = System.currentTimeMillis();
            if (fruit != null) {
              Double price = null;
              if (fruit.getPrice() != null) {
                price = fruit.getPrice() / link.getAmount();
              }
              Record record = Record.create(price, fruit.getStock(), null, link.getId(), parseEndTime);
              Boolean success = this.handover(record);
              storeEndTime = System.currentTimeMillis();
              if (success) {
                logger.info(String.format("采集%s网站成功,商品名为%s,单价为%2.2f,库存状况%d,促销信息%s,采集耗时%d毫秒,存储耗时%d毫秒", 
                  this.seller.getName(), product.getName(), price, fruit.getStock(), null, 
                  parseEndTime - beginTime, storeEndTime - parseEndTime));
                parser.sendExtractionLog(record, storeEndTime - parseEndTime);
              } else {
                logger.warn("manager保存失败");
              }
            }
          }
        } catch(Exception e) {
          storeEndTime = System.currentTimeMillis();
          String description = String.format("采集%s网站结束,商品名为%s,url为%s,价格解析失败,共耗时%d毫秒", 
              this.seller.getName(), product.getName(), link.getUrl(), storeEndTime - beginTime);
          logger.error(description, e);
          Log log = LogMaker.makeExceptionLog(Constants.SERVICE_NAME, description, e);
          this.loggerClient.send(log);
        } finally {
          try {
            Thread.sleep(seller.getDelay());
          } catch (InterruptedException ex) {}
        }
      }
    }
  }
  
  private Link getLink() {
    Link link = null;

    HttpGet httpGet = new HttpGet(String.format(Constants.MANAGER_URL, this.seller.getId()));
    RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(Constants.TIME_OUT)
            .setSocketTimeout(Constants.TIME_OUT).build();
    httpGet.setConfig(config);
    
		CloseableHttpResponse res;
    try {
      res = this.httpClient.execute(httpGet);
      HttpEntity entity = res.getEntity();
      String json = EntityUtils.toString(entity);
      res.close();
      link = this.mapper.readValue(json, Link.class);
    } catch (IOException e) {
      logger.error(String.format("获取link失败,%s", this.seller.getName()), e);
    }
		return link;
  }
  
  private Boolean handover(Record record) {
    Boolean success = false;
    HttpPost httpPost = new HttpPost(String.format(Constants.MANAGER_URL, this.seller.getId()));
    RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(Constants.TIME_OUT)
            .setSocketTimeout(Constants.TIME_OUT).build();
    httpPost.setConfig(config);
    
		CloseableHttpResponse res;
    try {
      String json = this.mapper.writeValueAsString(record);
      httpPost.setEntity(new StringEntity(json));
      httpPost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
      res = this.httpClient.execute(httpPost);
      HttpEntity entity = res.getEntity();
      json = EntityUtils.toString(entity);
      res.close();
      success = this.mapper.readValue(json, Boolean.class);
    } catch (IOException e) {
      logger.error("发送record失败", e);
    }
    return success;
  }
}
