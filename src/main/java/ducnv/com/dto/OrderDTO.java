package ducnv.com.dto;

import ducnv.com.entity.Customer;
import ducnv.com.entity.Order;
import ducnv.com.entity.OrderItem;
import ducnv.com.entity.OrderStatus;
import ducnv.com.entity.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;

    private LocalDateTime orderDateTime;

    private Customer customer;

    private List<OrderItem> orderItems;

    private Double totalAmount;

    private OrderStatus status;


    public static class Mapper{
        public static  OrderDTO fromEntity(Order order) {
            return OrderDTO.builder().id(order.getId())
                    .orderDateTime(order.getOrderDateTime())
                    .orderItems(order.getOrderItems())
                    .customer(order.getCustomer())
                    .totalAmount(order.getTotalAmount())
                    .status(order.getStatus())
                    .build();
        }
    }

}
