package com.itqgroup.ezharko.apiOrders.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    //Вместо кучи геттеров и сеттеров можно было бы использовать ломбок. Не стал

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "price_sum")
    private Integer priceSum;
    @Column(name = "order_date")
    private Date date;

    @OneToMany(mappedBy = "order")
    private List<ProductsInOrder> products;


    public Order(Long id, Integer sumPrice, Date date) {
        this.id = id;
        this.priceSum = sumPrice;
        this.date = date;
    }

    public List<ProductsInOrder> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsInOrder> products) {
        this.products = products;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(Integer priceSum) {
        this.priceSum = priceSum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", sumPrice=" + priceSum +
                ", date=" + date +
                '}';
    }
}
