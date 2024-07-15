package com.itqgroup.ezharko.apiOrders.model;

import java.util.Date;

public class OrderNumber {

    private Long number;
    private Date date;

    public OrderNumber(Long number, Date date) {
        this.number = number;
        this.date = date;
    }

    public OrderNumber() {
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OrderNumber{" +
                "number=" + number +
                ", date=" + date +
                '}';
    }
}
