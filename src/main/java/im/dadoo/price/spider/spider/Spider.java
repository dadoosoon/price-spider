package im.dadoo.price.spider.spider;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.dadoo.price.core.domain.Seller;
import im.dadoo.price.spider.cons.Constants;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
  private ObjectMapper mapper;
  
	public void start(ApplicationContext ctx) {
    List<Thread> threads = new ArrayList<>(15);
    List<Seller> sellers = this.listSellers();
    for (Seller seller : sellers) {
      SpiderTask task = ctx.getBean(SpiderTask.class);
      task.init(seller);
      threads.add(new Thread(task));
    }
    for (Thread thread : threads) {
      thread.start();
    }
	}
  
  private List<Seller> listSellers() {
    List<Seller> list = null;
    try {
      list = Arrays.asList(this.mapper.readValue(new URL(Constants.LIST_SELLERS_URL), Seller[].class));
      System.out.println(list);
    } catch (IOException ex) {
      logger.error(ex.getMessage());
    } finally {
      return list;
    }
  }
}
