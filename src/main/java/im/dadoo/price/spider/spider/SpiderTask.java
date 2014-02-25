/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.spider;

import im.dadoo.log.Log;
import im.dadoo.log.LogMaker;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.price.core.domain.Link;
import im.dadoo.price.core.domain.Product;
import im.dadoo.price.core.domain.Record;
import im.dadoo.price.core.domain.Seller;
import im.dadoo.price.core.service.ProductService;
import im.dadoo.price.core.service.SellerService;
import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.parser.Fruit;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.util.ParserSelector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
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

  private static final Logger logger = LoggerFactory.getLogger(SpiderTask.class);
  
  private Seller seller;
  
  private CloseableHttpClient httpClient;
  
  private Parser parser;
  
  @Resource
	private LoggerClient loggerClient;
  
  @Resource
  private ObjectMapper mapper;
  
  @Resource
  private SellerService sellerService;
  
  @Resource
  private ProductService productService;
  
  public SpiderTask() {
  }
  
  public void init(Seller seller) {
    this.seller = seller;
    this.httpClient = HttpClients.createDefault();
    this.parser = ParserSelector.select(seller.getId());
  }
  
  @Override
  public void run() {
    Long beginTime = null;
    Long parseEndTime = null;
    Long storeEndTime = null;
    
    while(true) {
      Link link = this.getLink();
      if (link == null) {
        logger.info("获取到的link为null");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException ex) {
          logger.error("线程等待失败");
          ex.printStackTrace();
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
              Boolean success = this.save(record);
              storeEndTime = System.currentTimeMillis();
              if (success) {
                logger.info(String.format("采集%s网站成功,商品名为%s,单价为%2.2f,库存状况%d,促销信息%s,采集耗时%d毫秒,存储耗时%d毫秒", 
                  this.seller.getName(), product.getName(), price, fruit.getStock(), null, 
                  parseEndTime - beginTime, storeEndTime - parseEndTime));
                parser.sendExtractionLog(record, storeEndTime - parseEndTime);
              } else {
                logger.error("manager保存失败");
              }
            }
          }
        } catch(Exception e) {
          storeEndTime = System.currentTimeMillis();
          String description = String.format("采集%s网站结束,商品名为%s,url为%s,价格解析失败,共耗时%d毫秒", 
              this.seller.getName(), product.getName(), link.getUrl(), storeEndTime - beginTime);
          logger.error(description);
          e.printStackTrace();
          //Log log = LogMaker.makeExceptionLog(Constants.SERVICE_NAME, description, e);
          //this.loggerClient.send(log);
        } finally {
          try {
            Thread.sleep(Constants.DELAY);
          } catch (InterruptedException ex) {}
        }
      }
    }
  }
  
  private Link getLink() {
    Link link = null;

    RequestConfig config = RequestConfig.custom().setConnectTimeout(Constants.TIME_OUT)
            .setSocketTimeout(Constants.TIME_OUT).build();
    HttpGet httpGet = new HttpGet(String.format(Constants.MANAGER_URL, this.seller.getId()));
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
    HttpPost httpPost = new HttpPost(String.format(Constants.MANAGER_URL, this.seller.getId()));
    httpPost.setConfig(config);
    
		CloseableHttpResponse res;
    try {
      List<NameValuePair> nvps = new ArrayList<>();
      String json = this.mapper.writeValueAsString(record);
      
      nvps.add(new BasicNameValuePair("record", json));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
      res = this.httpClient.execute(httpPost);
      HttpEntity entity = res.getEntity();
      json = EntityUtils.toString(entity);
      res.close();
      success = this.mapper.readValue(json, Boolean.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return success;
  }
}
