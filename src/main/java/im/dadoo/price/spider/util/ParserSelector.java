/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.util;

import im.dadoo.price.spider.parser.Parser;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author codekitten
 */
@Component
public final class ParserSelector {
  
  @Resource
  private Parser jdParser;
  
  @Resource
  private Parser amazonCnParser;
  
  @Resource
  private Parser yhdParser;
  
  @Resource
  private Parser womaiParser;
  
  @Resource
  private Parser sfbestParser;
  
  @Resource
  private Parser yixunParser;
  
  @Resource
  private Parser suningParser;
  
  @Resource
  private Parser gomeParser;
  
  @Resource
  private Parser dangdangParser;
  
  @Resource
  private Parser tootooParser;
  
  @Resource
  private Parser benlaiParser;
  
  @Resource
  private Parser jumeiParser;
  
  @Resource
  private Parser lefengParser;
  
  @Resource
  private Parser yintaiParser;
  
  public ParserSelector() {}
  
  public Parser select(Integer sellerId) {
    switch(sellerId) {
      case 1:
        return this.jdParser;
      case 2:
        return this.amazonCnParser;
      case 3:
        return this.yhdParser;
      case 4:
        return this.womaiParser;
      case 5:
        return this.sfbestParser;
      case 6:
        return this.yixunParser;
      case 7:
        return this.suningParser;
      case 8:
        return this.gomeParser;
      case 9:
        return this.dangdangParser;
      case 10:
        return this.tootooParser;
      case 11:
        return this.benlaiParser;
      case 12:
        return this.jumeiParser;
      case 13:
        return this.lefengParser;
      case 14:
        return this.yintaiParser;
      default:
        return null;
    }
  }
}
