package im.dadoo.price.spider.spider;

import im.dadoo.price.core.domain.Seller;
import im.dadoo.price.core.service.SellerService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Spider {

	protected static final Logger logger = LoggerFactory.getLogger(Spider.class);
	
  @Resource
  private SellerService sellerService;
  
	public void start(ApplicationContext ctx) {
    List<Thread> threads = new ArrayList<>(15);
    List<Seller> sellers = this.sellerService.list();
    for (Seller seller : sellers) {
      SpiderTask task = ctx.getBean(SpiderTask.class);
      task.init(seller);
      threads.add(new Thread(task));
    }
    for (Thread thread : threads) {
      thread.start();
    }
	}
}
