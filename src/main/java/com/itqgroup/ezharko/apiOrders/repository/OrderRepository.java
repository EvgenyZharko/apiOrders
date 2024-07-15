package com.itqgroup.ezharko.apiOrders.repository;

import com.itqgroup.ezharko.apiOrders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    /*@Override
    public List<Order> findAllOrders() {
        return null;
    }

    @Override
    public void createOrder() {

    }

    @Override
    public Order findMaxPriceOrderInDate(Date date) {
        return null;
    }

    @Override
    public List<Long> findOrdersIdNotProductInPeriod(String productName, Date dateStart, Date dateEnd) {
        return null;
    }

    @Override
    public void deleteOrder(Order order) {

    }

    @Override
    public Order findOrderById(Long id) {
        return null;
    }*/
}
