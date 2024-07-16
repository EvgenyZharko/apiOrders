package com.itqgroup.ezharko.apiOrders.controller;

import com.itqgroup.ezharko.apiOrders.entity.Order;
import com.itqgroup.ezharko.apiOrders.entity.ProductsInOrder;
import com.itqgroup.ezharko.apiOrders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Tag(name = "main_methods")
@RestController
public class OrderController {
    private OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    // Не совсем понял, поидее на вход должен получать джейсон,  например, с какой-то формы. То есть пост запрос.
    // Но у нас из параметров для создания требуется только сумма. А остальное мы получаем от апишки (дату и номер)
    // Также не совсем понятно, как сумму. Раз у нас цен нет даже в детализации заказа. Потому сделал гет, где
    // Контроллер на вход получает только сумму заказа, а остальное генерит, дергая вторую апишку и создает запись в БД
    @Operation(
            summary = "Метод, получающий на вход сумму заказа и создающий новый заказ в БД",
            description = "Получает на вход сумму заказа, делает запрос к Api по генерации номеров заказа," +
                    "создает новый заказ в БД, используя полученные данные"
    )
    @GetMapping("orders/create")
    public void createOrder(@RequestParam Integer priceSum) {
        orderService.createOrder(priceSum);
    }

    @Operation(
            summary = "Метод, получающий на вход id заказа, возвращает список продуктов",
            description = "Получает на вход id заказа, делает запрос к бд, возвращает список продуктов"
    )
    @GetMapping("orders/products_in_order")
    public List<ProductsInOrder> getProductInOrder(@RequestParam Long id) {
        return orderService.getProductInOrder(id);
    }

    @Operation(
            summary = "Возвращает список всех заказов"
    )
    @GetMapping("orders/all")
    public List<Order> findAllOrders() {
        return orderService.findAllOrders();
    }

    @Operation(
            summary = "Ищет заказ по его Id",
            description = "Получает на вход вход Id заказа, запрашивает из БД, возвращает информацию по заказу"
    )
    @GetMapping("orders/find")
    public Optional<Order> findOrderById(@RequestParam Long id) {
        return orderService.findOrderById(id);
    }

    @Operation(
            summary = "Удаляет заказ из БД",
            description = "Получает на вход Id заказа, производит удаление заказа из базы данных"
    )
    @GetMapping("orders/delete")
    public void deleteOrderById(@RequestParam Long id) {
        orderService.deleteOrderById(id);
    }

    @Operation(
            summary = "Возвращает заказ с максимальной суммой за выбранную дату.",
            description = "Получает на вход дату оформления заказа, делает запрос в базу данных" +
                    ", возвращает информацию по заказу с максимальной суммой заказа за выбранную дату"
    )
    @GetMapping("orders/max_price_in_date")
    public Order findMaxPriceOrderInDate(@RequestParam String date) {
        return orderService.findMaxPriceOrderInDate(date);
    }

    @Operation(
            summary = "Метод, получающий на вход диапазон дат и название товара, возвращает список Id заказов" +
                    " за выбранный период, которые не содержат указанный товар",
            description = "Получает на вход название товара, дату начала диапазона, дату окончания диапазона. " +
                    "Метод производит запрос к базе данных, возвращает список Id заказов, произведенных в " +
                    "указанный временной диапазон (не включая даты начала и конца), в которых отсутствует указанный" +
                    "товар"
    )
    @GetMapping("orders/find_orders_id_in_range_not_prod")
    public List<Long> findOrdersIdNotProductInPeriod(String productName, String dateStart, String dateEnd) {
        return orderService.findOrdersIdNotProductInPeriod(productName, dateStart, dateEnd);
    }
}
