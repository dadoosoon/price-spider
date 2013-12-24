package im.dadoo.price.spider.parser;

import java.io.IOException;




import java.net.URL;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YhdParser extends Parser {
	
	private static final String PRICE_URL = "http://busystock.i.yihaodian.com/restful/detail?mcsite=1&provinceId=2&pmId=%s";
	
	@Autowired
	private ObjectMapper mapper;
	
	public Fruit parse(String url) throws IOException {
		Fruit fruit = null;
		String[] ts = url.split("/");
		
		String pid = ts[ts.length - 1];
		Map<String, Object> map = this.mapper.readValue(new URL(String.format(PRICE_URL, pid)), Map.class);
		
		//首先判断是否有货
		if (map.containsKey("canSale")) {
			fruit = new Fruit();
			if ((Integer)map.get("canSale") == 0) {
				fruit.setStock(0);
			} else {
				fruit.setStock(1);
			}
		} else {
			return null;
		}
		if (map.containsKey("currentPrice")) {
			Double value = null;
			Object rawValue = map.get("currentPrice");
			if (rawValue.getClass().equals(Integer.class)) {
				value = (Integer)rawValue * 1.0;
			} else {
				value = (Double)rawValue;
			}
			fruit.setValue(value);
		}
		return fruit;
	}

}
