package im.dadoo.price.spider.cons;

public final class Constants {

	private Constants() {}
	
	public static final String SERVICE_NAME = "price-spider";
	
	public static final String TYPE_EXTRACTION = "extraction";
	
	public static final String TYPE_PARSE_FAIL = "parse-fail";
  
  public static final Integer TIME_OUT = 40000;
  
  public static final String MANAGER_URL = "http://price.spider.manager.dadoo.im/seller/%d";
  
  //public static final String MANAGER_URL = "http://localhost:8084/price-spider-manager/seller/%d";
}
