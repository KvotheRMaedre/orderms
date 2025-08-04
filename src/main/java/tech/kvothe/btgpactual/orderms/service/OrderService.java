package tech.kvothe.btgpactual.orderms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.kvothe.btgpactual.orderms.dto.OrderCreatedEvent;
import tech.kvothe.btgpactual.orderms.dto.OrderResponse;
import tech.kvothe.btgpactual.orderms.entity.Order;
import tech.kvothe.btgpactual.orderms.entity.OrderItem;
import tech.kvothe.btgpactual.orderms.repository.OrderRepository;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public void saveOrder(OrderCreatedEvent orderCreatedEvent) {

        var items = orderCreatedEvent.itens()
                .stream()
                .map(item -> new OrderItem(item.produto(), item.quantidade(), item.preco()))
                .toList();

        var order = new Order(
                orderCreatedEvent.codigoPedido(),
                orderCreatedEvent.codigoCliente(),
                getTotal(orderCreatedEvent),
                items
        );

        orderRepository.save(order);
    }

    private BigDecimal getTotal(OrderCreatedEvent orderCreatedEvent) {
        return orderCreatedEvent.itens()
                .stream()
                .map(item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
