package az.elgunb.shopping.order.repository;

import az.elgunb.shopping.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndCreatedBy(Long id, String createdBy);

    List<Order> findAllByCreatedBy(String createdBy);

    Optional<Order> findByOrderNumber(String orderNumber);

    Optional<Order> findByName(String name);

}