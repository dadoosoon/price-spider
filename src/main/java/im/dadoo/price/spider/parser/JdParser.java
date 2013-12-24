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
		if (html.indexOf("无货") > -1) {
			fruit.setStock(0);
		} else {
			fruit.setStock(1);
		}
		//然后解析价格
		es = doc.select(".p-price");
		html = es.get(1).text();
		if (html != null && !html.equals("")) {
			Double value = Double.parseDouble(html.substring(6));
			fruit.setValue(value);
		}
		else {
			fruit.setValue(null);
		}
		return fruit;
	}

}
