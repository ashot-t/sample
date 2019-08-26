package dev.shoppingcart.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import dev.shoppingcart.domain.enumeration.OrderStatus;

/**
 * A ProductOrder.
 */
@Entity
@Table(name = "product_order")
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", updatable = false)
    private Instant orderDate = Instant.now();

    @Column(name = "price", updatable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("productOrders")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public ProductOrder orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Double getPrice() {
        return price;
    }

    public ProductOrder price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ProductOrder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public ProductOrder product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
