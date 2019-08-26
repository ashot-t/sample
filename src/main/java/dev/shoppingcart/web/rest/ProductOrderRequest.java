package dev.shoppingcart.web.rest;

import java.io.Serializable;

public class ProductOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productId;

	public ProductOrderRequest() {}
	
	public ProductOrderRequest(Long productId) {
		this.productId = productId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}
