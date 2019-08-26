package dev.shoppingcart.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import dev.shoppingcart.domain.enumeration.OrderStatus;

/**
 * A DTO for the {@link cart.shopping.domain.ProductOrder} entity.
 */
public class ProductOrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    private Instant orderDate;

    private Double price;

    private OrderStatus status;

    private Long productId;

    private String productName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductOrderDTO productOrderDTO = (ProductOrderDTO) o;
        if (productOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductOrderDTO{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductName() + "'" +
            "}";
    }
}
