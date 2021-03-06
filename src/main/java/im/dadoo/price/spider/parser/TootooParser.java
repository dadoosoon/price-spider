package im.dadoo.price.spider.parser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class TootooParser extends Parser {

	@Override
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
    
    String html = this.getHtml(url);
		Document doc = Jsoup.parse(html);
		Elements es = doc.select(".cuxiaojia span");
    if (es.first() == null) {
      return fruit;
    }
		String fragment = es.first().ownText();
		if (fragment != null) {
      Double value = this.parsePrice(fragment.substring(1));
			fruit.setPrice(value);
      fruit.setStock(1);
		} else {
			fruit.setPrice(null);
      fruit.setStock(0);
		}
		return fruit;
	}

}
