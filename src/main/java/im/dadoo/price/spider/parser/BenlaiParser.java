package im.dadoo.price.spider.parser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class BenlaiParser extends Parser{

	@Override
	public Fruit parse(String url) throws IOException {
		Document doc = Jsoup.connect(url).timeout(Parser.TIME_OUT).get();
		Elements es = doc.select(".newprice span");
		if (es.first() == null) {
			return new Fruit(null, 0);
		} 
		String html = es.first().ownText();
		
		Fruit fruit = null;
		if (html != null) {
			Double value = Double.parseDouble(html.substring(1));
			fruit = new Fruit(value);
		} else {
			fruit = new Fruit(null, 0);
		}
		return fruit;
	}

}
