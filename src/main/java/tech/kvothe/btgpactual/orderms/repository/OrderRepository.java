package tech.kvothe.btgpactual.orderms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import tech.kvothe.btgpactual.orderms.dto.OrderResponse;
import tech.kvothe.btgpactual.orderms.entity.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, Long> {

    Page<Order> findAllByCustomerId(Long customerId, PageRequest pageRequest);
}
