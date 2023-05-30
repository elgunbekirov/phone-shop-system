package az.elgunb.shopping.order.dto;

import az.elgunb.shopping.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private String orderNumber;

    @NotBlank
    private String name;

    @NotBlank
    private String destination;

    private String note;

    private OrderStatus status;

    private Double weight;

    private BigDecimal amount;

    private String customer;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
