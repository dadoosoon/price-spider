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
		
		Fruit fruit = new Fruit();
		if (html != null) {
      Double value = this.parserValue(html.substring(1));
			fruit.setValue(value);
      fruit.setStock(1);
		} else {
      fruit.setStock(0);
		}
		return fruit;
	}

}
