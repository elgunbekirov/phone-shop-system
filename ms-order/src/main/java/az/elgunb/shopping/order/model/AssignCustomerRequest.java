package az.elgunb.shopping.order.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AssignCustomerRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String customerUsername;

}
