package im.dadoo.price.spider.parser;

public class Fruit {

	private Double value;
	
	private Integer stock;
	
	public Fruit() {
    this.value = null;
    this.stock = 0;
  }
	
	public Fruit(Double value) {
		this.value = value;
		this.stock = 1;
	}
	
	public Fruit(Double value, Integer stock) {
		this.value = value;
		this.stock = stock;
	}

  @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("value:").append(value).append(",");
		sb.append("stock:").append(stock);
		sb.append("}");
		return sb.toString();
	}
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	
}
