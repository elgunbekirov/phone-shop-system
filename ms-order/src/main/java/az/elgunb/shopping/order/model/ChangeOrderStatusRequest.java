package az.elgunb.shopping.order.model;

import az.elgunb.shopping.order.enums.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeOrderStatusRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus newOrderStatus;

}
