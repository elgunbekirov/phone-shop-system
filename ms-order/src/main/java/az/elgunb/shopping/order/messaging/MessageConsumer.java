package az.elgunb.shopping.order.messaging;

import az.elgunb.shopping.order.messaging.event.OrderStatusChangeEvent;
import az.elgunb.shopping.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final OrderService orderService;

    @Bean
    public Consumer<OrderStatusChangeEvent> receiveOrderStatusChangeEvent() {
        return event -> {
            log.info("Receive OrderStatusChangeEvent from ms-order: {}", event);
            orderService.changeStatus(event);
        };
    }

}
