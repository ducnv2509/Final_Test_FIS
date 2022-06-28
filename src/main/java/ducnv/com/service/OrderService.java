package ducnv.com.service;

import ducnv.com.dto.CreateOrderDTO;
import ducnv.com.dto.CreateOrderItemDTO;
import ducnv.com.dto.OrderDTO;
import ducnv.com.dto.RemoveOrderItemDTO;
import ducnv.com.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDTO> findAll(Pageable pageable);
    Order getOrder(Long id);
    OrderDTO create(CreateOrderDTO createOrderDTO);
    void deleteOrder(Long id);
    void updatePaidOrder(Long id);
    void updateCancelledOrder(Long id);
    OrderDTO removeOrderItem(RemoveOrderItemDTO removeOrderItemDTO);
    OrderDTO addOrderItem(CreateOrderItemDTO createOrderItemDTO);

}
