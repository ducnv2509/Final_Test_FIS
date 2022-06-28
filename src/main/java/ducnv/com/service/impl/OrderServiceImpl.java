package ducnv.com.service.impl;

import ducnv.com.dto.CreateOrderDTO;
import ducnv.com.dto.CreateOrderItemDTO;
import ducnv.com.dto.OrderDTO;
import ducnv.com.dto.RemoveOrderItemDTO;
import ducnv.com.entity.*;
import ducnv.com.ex.OrderNotFoundException;
import ducnv.com.ex.QuantityProductException;
import ducnv.com.repository.CustomerRepository;
import ducnv.com.repository.OrderItemRepository;
import ducnv.com.repository.OrderRepository;
import ducnv.com.repository.ProductRepository;
import ducnv.com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        return this.orderRepository.findAll(pageable).map(OrderDTO.Mapper::fromEntity);
    }

    @Override
    public Order getOrder(Long id) {
        Optional<Order> result = this.orderRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new OrderNotFoundException("Order Not found: " + id);
        }
    }

    public Product findByProductById(Long id) {
        return this.productRepository.findById(id).get();
    }

    private Product updateAvailable(Product product, Integer qtyOrder, Boolean flag) {
        if (flag) {
            product.setAvailable(product.getAvailable() + qtyOrder);
        } else {
            product.setAvailable(product.getAvailable() - qtyOrder);
        }
        return productRepository.save(product);
    }

    @Override
    public OrderDTO create(CreateOrderDTO createOrderDTO) {
        Order orderAdd = Order.builder()
                .customer(Customer.builder().id(createOrderDTO.getCustomerId()).build())
                .orderDateTime(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .totalAmount(0.0)
                .build();
        Order order = this.orderRepository.save(orderAdd);
        AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);
        createOrderDTO.getOrderItems().forEach(i -> {
            Product product = this.findByProductById(i.getProductId());
            if (product.getAvailable() < i.getQuantity()) {
                throw new IllegalArgumentException("quantity is not enough: " + i.getProductId());
            }
            this.updateAvailable(product, i.getQuantity(), false);
            OrderItem orderItemAdd = OrderItem.builder()
                    .quantity(i.getQuantity())
                    .product(Product.builder().id(i.getProductId()).build())
                    .order(order)
                    .amount(i.getQuantity() * product.getPrice())
                    .build();
            totalAmount.updateAndGet(o -> o + orderItemAdd.getAmount());
            orderItemRepository.save(orderItemAdd);
        });
        order.setTotalAmount(totalAmount.get());
        return OrderDTO.Mapper.fromEntity(orderRepository.save(orderAdd));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    throw new OrderNotFoundException("Order not found:" + id);
                });
    }

    @Override
    public OrderDTO addOrderItem(CreateOrderItemDTO createOrderItemDTO) {
        Order order = this.findOrderById(createOrderItemDTO.getOrderId());
        if (!"CREATED".equals(order.getStatus().toString())) {
            throw new IllegalArgumentException("Not create because order is status create");
        }
        Product product = this.findByProductById(createOrderItemDTO.getProductId());
        if (product.getAvailable() < createOrderItemDTO.getQuantity()) {
            throw new QuantityProductException(("Quantity is not enough: " + createOrderItemDTO.getProductId()));
        }
        this.updateAvailable(product, createOrderItemDTO.getQuantity(), true);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .quantity(createOrderItemDTO.getQuantity())
                .product(product)
                .amount(product.getPrice() * createOrderItemDTO.getQuantity())
                .build();

        order.getOrderItems().add(orderItem);
        Double totalAmount = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getAmount)
                .reduce(0, Double::sum);
        order.setTotalAmount(totalAmount);

        orderItemRepository.save(orderItem);

        return OrderDTO.Mapper.fromEntity(orderRepository.save(order));
    }


    @Override
    public void deleteOrder(Long id) {
        if (this.orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Order Not found: " + id);
        }
        this.orderRepository.deleteByIdOrder(id);
    }

    @Override
    public void updatePaidOrder(Long id) {
        if (this.orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Order Not found: " + id);
        }
        this.orderRepository.updateOrderPaid(id);
    }

    @Override
    public void updateCancelledOrder(Long id) {
        if (this.orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Order Not found: " + id);
        }
        this.orderRepository.updateOrderCancel(id);
    }

    @Override
    public OrderDTO removeOrderItem(RemoveOrderItemDTO removeOrderItemDTO) {
        Order order = this.findOrderById(removeOrderItemDTO.getOrderId());
        if (!"CREATED".equals(order.getStatus().toString())) {
            throw new OrderNotFoundException("status is not CREATED");
        }
        order.getOrderItems().forEach(i -> {
            if (i.getId().equals(removeOrderItemDTO.getOrderItemId())) {
                Product product = this.findByProductById(i.getProduct().getId());
                this.updateAvailable(product, i.getQuantity(), true);
                order.setTotalAmount(order.getTotalAmount() - i.getAmount());
                orderItemRepository.delete(i);
                return;
            }
        });
        return OrderDTO.Mapper.fromEntity(orderRepository.save(order));
    }
}
