package im.dadoo.price.spider.boot;


import im.dadoo.price.core.configuration.PriceCoreContext;
import im.dadoo.price.spider.configuration.PriceSpiderContext;
import im.dadoo.price.spider.cons.Constants;
import im.dadoo.price.spider.parser.AmazonCnParser;
import im.dadoo.price.spider.parser.BenlaiParser;
import im.dadoo.price.spider.parser.DangdangParser;
import im.dadoo.price.spider.parser.GomeParser;
import im.dadoo.price.spider.parser.JdParser;
import im.dadoo.price.spider.parser.JumeiParser;
import im.dadoo.price.spider.parser.LefengParser;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.parser.SfbestParser;
import im.dadoo.price.spider.parser.SuningParser;
import im.dadoo.price.spider.parser.TootooParser;
import im.dadoo.price.spider.parser.WomaiParser;
import im.dadoo.price.spider.parser.YhdParser;
import im.dadoo.price.spider.parser.YintaiParser;
import im.dadoo.price.spider.parser.YixunParser;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestApp {

	public static void main(String[] args) throws IOException, InterruptedException {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            PriceCoreContext.class, 
            PriceSpiderContext.class);
    Parser parser = ctx.getBean(WomaiParser.class);
    System.out.println(parser.parse("http://www.womai.com/Product-0-297575.htm"));
	}
}
