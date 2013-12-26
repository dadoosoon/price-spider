package im.dadoo.price.spider.parser;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class JdParser extends Parser {
	
	public Fruit parse(String url) throws IOException {
		String[] st = url.split("/");
		String newUrl = "http://m.jd.com/product/" + st[st.length - 1];
		Document doc = Jsoup.connect(newUrl).timeout(Parser.TIME_OUT).get();
		
		Fruit fruit = new Fruit();
		//首先判断有没有货
		Elements es = doc.select(".p-stock");
		String html = es.text();
    //html为null说明库存解析方法已失效
    if (html != null) {
      if (html.indexOf("无货") > -1) {
        fruit.setStock(0);
      } else {
        fruit.setStock(1);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.Log_PARSE_STOCK_FAIL);
      this.sendFailureLog(url, "JdParser", Parser.Log_PARSE_STOCK_FAIL);
    }
		
		//然后解析价格
		es = doc.select(".p-price");
    //若es.get(1)为null，则证明价格解析方法已失效
    if (es.get(1) != null) {
      html = es.get(1).text();
      if (html != null && !html.equals("")) {
        Double value = this.parserValue(html.substring(6));
        fruit.setValue(value);
      }
      else {
        fruit.setValue(null);
      }
    } else {
      logger.error("url:%s,%s", url, Parser.Log_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, "JdParser", Parser.Log_PARSE_VALUE_FAIL);
    }
		
		return fruit;
	}

}
