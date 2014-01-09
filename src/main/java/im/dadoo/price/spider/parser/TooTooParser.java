package im.dadoo.price.spider.parser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class TooTooParser extends Parser {

	@Override
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		fruit.setStock(1);
    
    String html = this.getHtml(url);
		Document doc = Jsoup.parse(html);
		Elements es = doc.select(".cuxiaojia span");
		String fragment = es.first().ownText();
		if (fragment != null) {
      Double value = this.parsePrice(fragment.substring(1));
			fruit.setPrice(value);
		} else {
			fruit.setPrice(null);
		}
		return fruit;
	}

}
