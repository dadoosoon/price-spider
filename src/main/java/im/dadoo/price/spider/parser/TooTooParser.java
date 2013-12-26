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
		Document doc = Jsoup.connect(url).timeout(Parser.TIME_OUT).get();
		Elements es = doc.select(".cuxiaojia span");
		String html = es.first().ownText();
		if (html != null) {
      Double value = this.parserValue(html.substring(1));
			fruit.setValue(value);
		} else {
			fruit.setValue(null);
		}
		return fruit;
	}

}
