package im.dadoo.price.spider.parser;

import im.dadoo.price.spider.cons.Constants;
import java.io.IOException;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class YixunParser extends Parser {
	
	public Fruit parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).userAgent("").cookie("wsid", "2001")
            .timeout(Constants.TIME_OUT).get();
		Elements es = doc.select("#sea_notify");
		Fruit fruit = new Fruit();
		if (es.first() != null) {
			fruit.setStock(0);
		} else {
			fruit.setStock(1);
		}
		es = doc.select(".xprice_val");
    if (es.first() != null) {
      List<Double> prices = new ArrayList<>();
      for (Element e : es) {
        Double value = this.parsePrice(e.text().substring(1));
        prices.add(value);
      }
      fruit.setPrice(Collections.min(prices));
    } else {
      logger.error("url:%s,%s", url, Parser.LOG_PARSE_VALUE_FAIL);
      this.sendFailureLog(url, this.getClass().getSimpleName(), Parser.LOG_PARSE_VALUE_FAIL);
    }
		
		return fruit;
	}

}
