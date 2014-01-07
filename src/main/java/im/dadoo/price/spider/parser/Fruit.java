package im.dadoo.price.spider.parser;

public class Fruit {

	private Double price;
	
	private Integer stock;
	
	public Fruit() {
    this.price = null;
    this.stock = 0;
  }
	
	public Fruit(Double price, Integer stock) {
		this.price = price;
		this.stock = stock;
	}

  @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("price:").append(price).append(",");
		sb.append("stock:").append(stock);
		sb.append("}");
		return sb.toString();
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	
}
