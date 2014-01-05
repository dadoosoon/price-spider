package im.dadoo.price.spider.parser;


import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class GomeParser extends Parser {
	
	private static String PREFIX = "price:\"";
	
	public Fruit parse(String url) throws IOException {
		Fruit fruit = new Fruit();
		Document doc = Jsoup.connect(url).timeout(Parser.TIME_OUT).get();
		String html = doc.html();
		Integer index1 = html.indexOf(PREFIX) + PREFIX.length();
		Integer index2 = html.substring(index1).indexOf("\"");
		String result = html.substring(index1, index1 + index2);
		
    Double value = this.parserValue(result);
		fruit.setPrice(value);
		fruit.setStock(1);
		return fruit;
	}

}
