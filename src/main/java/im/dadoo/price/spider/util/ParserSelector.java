/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.util;

import im.dadoo.price.spider.parser.AmazonCnParser;
import im.dadoo.price.spider.parser.BenlaiParser;
import im.dadoo.price.spider.parser.DangdangParser;
import im.dadoo.price.spider.parser.GomeParser;
import im.dadoo.price.spider.parser.JdParser;
import im.dadoo.price.spider.parser.Parser;
import im.dadoo.price.spider.parser.SfbestParser;
import im.dadoo.price.spider.parser.SuningParser;
import im.dadoo.price.spider.parser.TooTooParser;
import im.dadoo.price.spider.parser.WomaiParser;
import im.dadoo.price.spider.parser.YhdParser;
import im.dadoo.price.spider.parser.YixunParser;

/**
 *
 * @author codekitten
 */
public final class ParserSelector {
  
  private ParserSelector() {}
  
  public static Parser select(Integer sellerId) {
    switch(sellerId) {
      case 1:
        return new JdParser();
      case 2:
        return new AmazonCnParser();
      case 3:
        return new YhdParser();
      case 4:
        return new WomaiParser();
      case 5:
        return new SfbestParser();
      case 6:
        return new YixunParser();
      case 7:
        return new SuningParser();
      case 8:
        return new GomeParser();
      case 9:
        return new DangdangParser();
      case 10:
        return new TooTooParser();
      case 11:
        return new BenlaiParser();
      default:
        return null;
    }
  }
}
