package im.dadoo.price.spider.parser;


import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class SuningParser extends Parser{
	
	private static final String PREFIX = "currPrice=";
	
	public Fruit parse(String url) throws IOException {
//		Document doc = Jsoup.connect(url).timeout(Parser.TIME_OUT).get();
//		String html = doc.select("#tellMe a").attr("href");
//		Integer index1 = html.indexOf(PREFIX) + PREFIX.length();
//		Integer index2 = html.substring(index1).indexOf("&");
//		String result = html.substring(index1, index1 + index2);
//		
//		Double value = Double.parseDouble(result);
//		return value;
		return null;
	}

}
