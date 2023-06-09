package az.elgunb.shopping.identity.controller;

import az.elgunb.shopping.identity.model.RefreshTokenRequest;
import az.elgunb.shopping.identity.model.SigninRequest;
import az.elgunb.shopping.identity.model.SignupRequest;
import az.elgunb.shopping.identity.model.TokenPair;
import az.elgunb.shopping.identity.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Api(value = "Sign in, sign up, sign out services")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authenticationService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<Void> signupCustomerAccount(@Valid @RequestBody SignupRequest signupRequest) {
        authenticationService.signupCustomerAccount(signupRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenPair> signin(@Valid @RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @GetMapping("/signout")
    public ResponseEntity<Void> signout(@RequestHeader("Authorization") @NotNull String authorizationHeader) {
        authenticationService.signout(authorizationHeader);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestHeader("Authorization") @NotNull String authorizationHeader) {
        authenticationService.verify(authorizationHeader);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Use refresh token to get new access token")
    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }

}
