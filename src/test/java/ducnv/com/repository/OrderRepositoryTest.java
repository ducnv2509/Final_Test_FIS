package ducnv.com.repository;

import ducnv.com.TestFinalApplication;
import ducnv.com.entity.Customer;
import ducnv.com.entity.Order;
import ducnv.com.entity.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = TestFinalApplication.class)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void deleteByIdOrder() {
        Order order = getOrder();
        this.orderRepository.save(order);
        this.orderRepository.deleteById(order.getId());
        List<Order> result = this.orderRepository.findAll();
        assertEquals(result.size(), 1);
    }

    @Test
    void updateOrderPaid() {
        Order order = getOrder();
        this.orderRepository.save(order);
        this.orderRepository.updateOrderPaid(order.getId());
        List<Order> result = this.orderRepository.findAll();
        assertEquals(result.size(), 1);
    }

    @Test
    void updateOrderCancel() {
        Order order = getOrder();
        this.orderRepository.save(order);
        this.orderRepository.updateOrderCancel(order.getId());
        List<Order> result = this.orderRepository.findAll();
        assertEquals(result.size(), 1);
    }

    private Order getOrder() {
        Customer customer = new Customer();
        customer.setId(1L);
        return Order.builder()
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.valueOf("CREATED"))
                .totalAmount(1000.0)
                .customer(customer)
                .build();
    }
}