/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package im.dadoo.price.spider.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.dadoo.logger.client.LoggerClient;
import im.dadoo.logger.client.impl.DefaultLoggerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 *
 * @author codekitten
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("im.dadoo.price.spider")
public class PriceSpiderContext {
  
  @Bean
  public ObjectMapper mapper() {
    return new ObjectMapper();
  }
  
  @Bean
  public LoggerClient loggerClient() {
    return new DefaultLoggerClient("http://log.dadoo.im/logger");
  }
}
