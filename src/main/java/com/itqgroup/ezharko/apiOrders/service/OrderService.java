package com.itqgroup.ezharko.apiOrders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itqgroup.ezharko.apiOrders.entity.Order;
import com.itqgroup.ezharko.apiOrders.entity.ProductsInOrder;
import com.itqgroup.ezharko.apiOrders.model.OrderNumber;
import com.itqgroup.ezharko.apiOrders.repository.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Order> orders = session.createQuery("select o from Order o").getResultList();
            session.getTransaction().commit();
            return orders;
        }
    }

    public List<ProductsInOrder> getProductInOrder(Long id) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        Order order = null;
        List<ProductsInOrder> productsInOrders = null;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            order = session.get(Order.class, id);

            productsInOrders = order.getProducts();
            System.out.println(productsInOrders);
            session.getTransaction().commit();

            return productsInOrders;
        }
    }

    public Order findMaxPriceOrderInDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Order order = session.createQuery("select o from Order o where o.priceSum = " +
                    "(select max(o.priceSum) from Order o where date = :date)", Order.class)
                    .setParameter("date",  timestamp).getSingleResult();
            session.getTransaction().commit();
            return order;
        }
    }

    public void createOrder(Integer sumPrice) {
        HttpURLConnection con;
        String responceStr = "";
        URL url;
        OrderNumber orderNumber = null;
        StringBuilder content = new StringBuilder();;

        try {
            url = new URL("http://localhost:8189/orderNumber/createOrderNumber");
            con = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }
            responceStr =  content.toString();
            orderNumber = new ObjectMapper().readValue(responceStr, OrderNumber.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long id = orderNumber.getNumber();
        Date date = orderNumber.getDate();

        Order order = new Order(id, sumPrice, date);
        orderRepository.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Long> findOrdersIdNotProductInPeriod(String productName, String dateStart, String dateEnd) {

        // ну тут не смейтесь, пожалуйста. Наколхозил с 2мя запросами в листы и последующим сравнением.
        // в SQL я строил один запрос, но как это реализовать тут, не придумал.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dtStart = null;
        Date dtEnd = null;
        try {
            dtStart = dateFormat.parse(dateStart);
            dtEnd = dateFormat.parse(dateEnd);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Timestamp tsStart = new java.sql.Timestamp(dtStart.getTime());
        Timestamp tsEnd = new java.sql.Timestamp(dtEnd.getTime());

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            List<Long> idOrder = session.createQuery("select o.id from Order o where o.date > :dateStart and o.date < :dateEnd")
                    .setParameter("dateStart", dtStart)
                    .setParameter("dateEnd", dtEnd).getResultList();
            List<Long> idOrderWithProduct = session.createQuery("select p.order.id from ProductsInOrder p where p.productName = :productName")
                    .setParameter("productName", productName).getResultList();

            session.getTransaction().commit();

            List<Long> resultList = new ArrayList<>();

            for (int i = 0; i < idOrder.size(); i++) {
                for (int j = 0; j < idOrderWithProduct.size(); j++) {
                    if (idOrder.get(i).equals(idOrderWithProduct.get(j))) {
                        continue;
                    }
                    resultList.add(idOrder.get(i));
                }
            }

            return resultList;
        }

    }

}
