package ducnv.com.controller;


import ducnv.com.dto.CreateOrderDTO;
import ducnv.com.dto.CreateOrderItemDTO;
import ducnv.com.dto.OrderDTO;
import ducnv.com.dto.RemoveOrderItemDTO;
import ducnv.com.entity.Order;
import ducnv.com.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public Page<OrderDTO> findAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        log.info("Request All Customer. PageNumber: {}, PageSize: {}", pageNumber, pageSize);
        return this.orderService.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> findById(@PathVariable(name = "orderId") Long orderId) {
        Order post = this.orderService.getOrder(orderId);
        OrderDTO postResponse = modelMapper.map(post, OrderDTO.class);
        return ResponseEntity.ok().body(postResponse);
    }

    @PutMapping("/")
    public ResponseEntity<OrderDTO> create(@RequestBody CreateOrderDTO createOrderDTO) {
        return ResponseEntity.ok(this.orderService.create(createOrderDTO));
    }

    @PostMapping("/addOrderItem")
    public ResponseEntity<OrderDTO> addOrderItem(@RequestBody CreateOrderItemDTO createOrderItemDTO) {
        return ResponseEntity.ok(orderService.addOrderItem(createOrderItemDTO));
    }

    @PostMapping("/removeOrderItem")
    public ResponseEntity<OrderDTO> removeOrderItem(@RequestBody RemoveOrderItemDTO removeOrderItemDTO) {
        return ResponseEntity.ok(orderService.removeOrderItem(removeOrderItemDTO));
    }

    @DeleteMapping("/{orderId}")
    public void delete(@PathVariable Long orderId) {
        this.orderService.deleteOrder(orderId);
    }

    @PostMapping("/paid/{orderId}")
    public ResponseEntity<OrderDTO> paid(@PathVariable Long orderId) {
        this.orderService.updatePaidOrder(orderId);
        Order post = this.orderService.getOrder(orderId);
        OrderDTO postResponse = modelMapper.map(post, OrderDTO.class);
        return ResponseEntity.ok().body(postResponse);
    }


    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancel(@PathVariable Long orderId) {
        this.orderService.updateCancelledOrder(orderId);
        Order post = this.orderService.getOrder(orderId);
        OrderDTO postResponse = modelMapper.map(post, OrderDTO.class);
        return ResponseEntity.ok().body(postResponse);
    }
}
