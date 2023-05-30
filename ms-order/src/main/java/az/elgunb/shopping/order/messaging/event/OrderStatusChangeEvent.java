package az.elgunb.shopping.order.messaging.event;

import az.elgunb.shopping.order.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusChangeEvent {

    private String orderNumber;

    private OrderStatus orderStatus;

}
