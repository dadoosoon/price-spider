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

import org.apache.commons.math3.random.RandomDataGenerator;
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
		List<Link> links = this.linkService.list();
    //乱序采集
		links = this.disorderLinks(links);
		for (Link link : links) {
			Parser parser = this.choose(link.getSeller());
			if (parser != null) {
				logger.info(String.format("开始采集%s网站数据,商品名%s,总量%d,url为%s", 
						link.getSeller().getName(), link.getProduct().getName(), link.getAmount(), link.getUrl()));
				Long t1 = System.currentTimeMillis();
				try {
					Fruit fruit = parser.parse(link.getUrl());
					Long t2 = System.currentTimeMillis();
					if (fruit != null) {
						Double price = null;
						if (fruit.getPrice() != null) {
							price = fruit.getPrice() / link.getAmount();
						}
						Record record = this.recordService.save(link, price, fruit.getStock());
						Long t3 = System.currentTimeMillis();
						logger.info(String.format("采集%s网站结束,商品名为%s,单价为%2.2f,库存状况%d,采集耗时%d毫秒,存储耗时%d毫秒", 
								link.getSeller().getName(), link.getProduct().getName(), price, fruit.getStock(), t2 - t1, t3 - t2));
						parser.sendExtractionLog(record, t3 - t1);
					}
				} catch(Exception e1) {
					Long t4 = System.currentTimeMillis();
					String description = String.format("采集%s结束,商品名为%s,价格解析失败,共耗时%d毫秒", 
							link.getUrl(), link.getProduct().getName(), t4 - t1);
					logger.error(description);
          e1.printStackTrace();
					Log log = LogMaker.makeExceptionLog(Constants.SERVICE_NAME, description, e1);
					this.loggerClient.send(log);
				} 
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
	
	private List<Link> disorderLinks(List<Link> links) {
		RandomDataGenerator rdg = new RandomDataGenerator();
		List<Link> result = new ArrayList<Link>(links.size());
		int[] p = rdg.nextPermutation(links.size(), links.size());
		for (int i = 0; i < p.length; i++) {
			result.add(links.get(p[i]));
		}
		return result;
	}
}
