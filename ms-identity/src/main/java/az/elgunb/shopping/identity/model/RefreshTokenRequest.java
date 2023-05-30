package az.elgunb.shopping.identity.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenRequest {

    @NotEmpty
    private String refreshToken;

}
