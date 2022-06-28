package ducnv.com.repository;

import ducnv.com.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "{call SPDeleteOrder(:Id)}", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByIdOrder(@Param("Id") Long Id);

    @Modifying
    @Transactional
    @Query("update Order o set o.status = 'PAID' where o.id = ?1 and o.status = 'CREATED'")
    void updateOrderPaid(Long id);

    @Modifying
    @Transactional
    @Query("update Order o set o.status = 'CANCELLED' where o.id = ?1 and o.status = 'CREATED'")
    void updateOrderCancel(Long id);
}
