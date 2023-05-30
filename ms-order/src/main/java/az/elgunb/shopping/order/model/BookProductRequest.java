package az.elgunb.shopping.order.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String destination;

    private String note;

}
