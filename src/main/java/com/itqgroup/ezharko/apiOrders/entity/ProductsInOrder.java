package com.itqgroup.ezharko.apiOrders.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products_in_order")
public class ProductsInOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_count")
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public ProductsInOrder(Long id, String productName, Integer count, Order order) {
        this.id = id;
        this.productName = productName;
        this.count = count;
        this.order = order;
    }

    public ProductsInOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

   /* public Order getOrder() {
        return order;
    }*/

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ProductsInOrder{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", count=" + count +
                ", order=" + order +
                '}';
    }
}
