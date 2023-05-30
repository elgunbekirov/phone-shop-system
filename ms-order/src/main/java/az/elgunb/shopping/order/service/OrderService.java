package az.elgunb.shopping.order.service;

import az.elgunb.shopping.order.error.exception.CustomerMustNotBeAssignedException;
import az.elgunb.shopping.order.error.validation.ValidationMessage;
import az.elgunb.shopping.order.mapper.OrderMapper;
import az.elgunb.shopping.order.messaging.MessageProducer;
import az.elgunb.shopping.order.messaging.event.EmailNotificationEvent;
import az.elgunb.shopping.order.messaging.event.OrderStatusChangeEvent;
import az.elgunb.shopping.order.messaging.event.PhoneOrderEvent;
import az.elgunb.shopping.order.repository.OrderRepository;
import az.elgunb.shopping.common.enums.UserType;
import az.elgunb.shopping.common.error.exception.InvalidInputException;
import az.elgunb.shopping.order.domain.Order;
import az.elgunb.shopping.order.dto.OrderDto;
import az.elgunb.shopping.order.enums.NotificationMessage;
import az.elgunb.shopping.order.enums.OrderStatus;
import az.elgunb.shopping.order.model.AssignCustomerRequest;
import az.elgunb.shopping.order.model.ChangeOrderDestinationRequest;
import az.elgunb.shopping.order.model.ChangeOrderStatusRequest;
import az.elgunb.shopping.order.model.BookProductRequest;
import az.elgunb.shopping.order.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static az.elgunb.shopping.common.security.util.SecurityUtil.getCurrentUserLogin;
import static az.elgunb.shopping.common.security.util.SecurityUtil.getCurrentUserType;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MessageProducer messageProducer;

    public OrderDto save(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        List<Order> orderList = isAdmin() ? orderRepository.findAll() :
                orderRepository.findAllByCreatedBy(getCurrentUserLogin());
        return orderList.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public OrderDto findOne(Long id) {
        Optional<Order> order = isAdmin() ? orderRepository.findById(id) :
                orderRepository.findByIdAndCreatedBy(id, getCurrentUserLogin());
        return order.map(orderMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.PRODUCT_NOT_FOUND, List.of(id)));
    }

    @Transactional(readOnly = true)
    public OrderDto findByName(String name) {
        Optional<Order> order = orderRepository.findByName(name);
        return order.map(orderMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.PRODUCT_NOT_FOUND, List.of(name)));
    }

    @Transactional(readOnly = true)
    public OrderDto findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.PRODUCT_NOT_FOUND, List.of(orderNumber)));
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    private boolean orderExists(String name) {
        OrderDto item = findByName(name);
        return item.getName().equals(name);
    }
    public OrderDto bookProduct(BookProductRequest req) {
        String name = req.getName();
        OrderDto orderDto = findByName(name);
        if (orderDto.getStatus() == OrderStatus.NOT_AVAILABLE) {
            throw new RuntimeException(String.format("Product isn't available for the given name {0} ", name));
        }
        if (orderDto.getName().equals(name)) {
            throw new RuntimeException(String.format("Product already exists for the given name {0}", name));
        }
        if (orderDto.getStatus() == OrderStatus.BOOKED) {
            throw new RuntimeException(String.format("Product already booked for the given name {0} ", name));
        }
        orderDto = OrderDto.builder()
                .name(req.getName())
                .destination(req.getDestination())
                .note(req.getNote())
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatus.BOOKED)
                .createdBy(getCurrentUserLogin())
                .build();
        return save(orderDto);
    }

    public OrderDto changeDestination(ChangeOrderDestinationRequest request) {
        OrderDto orderDto = findOne(request.getOrderId());
        orderDto.setDestination(request.getNewDestination());
        return save(orderDto);
    }

    public void returnProduct(Long id) {
        OrderDto orderDto = findOne(id);
        if (orderDto == null) {
            throw new RuntimeException(String.format("Product not found for the given id {0}", id));
        }
        if (orderDto.getStatus() == OrderStatus.BOOKED) {
            throw new RuntimeException(String.format("Product isn't booked for the given id {0} ", id));
        }
        orderDto.setStatus(OrderStatus.INITIAL);
        save(orderDto);
    }

    public OrderDto acceptProduct(Long id) {
        OrderDto orderDto = findOne(id);
        if (orderDto.getStatus() == OrderStatus.AVAILABLE) {
            return orderDto;
        }
        double weight = OrderUtil.calculateRandomPrice();
        orderDto.setWeight(weight);
        orderDto.setAmount(OrderUtil.calculateTotalOrderAmount(weight));
        orderDto.setStatus(OrderStatus.AVAILABLE);
        orderDto = save(orderDto);

        EmailNotificationEvent event =
                OrderUtil.createEmailNotificationEvent(orderDto, NotificationMessage.ORDER_ACCEPTED);
        messageProducer.sendEmailNotificationEvent(event);

        return orderDto;
    }

    public OrderDto changeStatus(ChangeOrderStatusRequest request) {
        OrderDto orderDto = findOne(request.getOrderId());
        orderDto.setStatus(request.getNewOrderStatus());
        return save(orderDto);
    }

    public OrderDto changeStatus(OrderStatusChangeEvent event) {
        OrderDto orderDto = findByOrderNumber(event.getOrderNumber());
        orderDto.setStatus(event.getOrderStatus());
        return save(orderDto);
    }

    public OrderDto assignCustomer(AssignCustomerRequest request) {
        OrderDto orderDto = findOne(request.getOrderId());
        checkOrderStatus(orderDto);

        orderDto.setCustomer(request.getCustomerUsername());
        orderDto.setStatus(OrderStatus.INITIAL);
        orderDto = save(orderDto);

        PhoneOrderEvent event = OrderUtil.createPhoneOrderEvent(orderDto);
        messageProducer.sendPhoneOrderEvent(event);

        return orderDto;
    }

    private void checkOrderStatus(OrderDto orderDto) {
        OrderStatus status = orderDto.getStatus();
        if (status == OrderStatus.INITIAL || status == OrderStatus.NOT_AVAILABLE) {
            throw new CustomerMustNotBeAssignedException();
        }
    }

    private boolean isAdmin() {
        return getCurrentUserType() == UserType.ADMIN;
    }

}
