package az.elgunb.shopping.order.messaging.event;

import az.elgunb.shopping.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneOrderEvent {

    private String orderNumber;

    private String destination;

    private OrderStatus status;

    private String customer;

    private String coordination;

}
