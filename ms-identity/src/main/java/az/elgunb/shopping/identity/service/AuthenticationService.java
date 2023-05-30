package az.elgunb.shopping.identity.service;

import az.elgunb.shopping.common.enums.UserType;
import az.elgunb.shopping.common.security.TokenProvider;
import az.elgunb.shopping.common.security.enums.Roles;
import az.elgunb.shopping.identity.error.exception.InvalidAccessTokenException;
import az.elgunb.shopping.identity.error.exception.InvalidRefreshTokenException;
import az.elgunb.shopping.identity.error.exception.UsernameAlreadyExistsException;
import az.elgunb.shopping.identity.repository.RedisRepository;
import az.elgunb.shopping.identity.security.TokenUtil;
import az.elgunb.shopping.identity.security.jwt.TokenCreator;
import az.elgunb.shopping.identity.dto.RoleSmallDto;
import az.elgunb.shopping.identity.dto.UserDto;
import az.elgunb.shopping.identity.model.SigninRequest;
import az.elgunb.shopping.identity.model.SignupRequest;
import az.elgunb.shopping.identity.model.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final TokenCreator tokenCreator;
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest req) {
        if (userService.isUsernameExist(req.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        RoleSmallDto roleSmallDto = roleService.findByName(Roles.USER.name());
        UserDto userDto = UserDto.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .type(UserType.USER)
                .phone(req.getPhone())
                .enabled(true)
                .roles(Set.of(roleSmallDto))
                .build();
        userService.save(userDto);
    }

    public void signupCustomerAccount(SignupRequest req) {
        if (userService.isUsernameExist(req.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        RoleSmallDto roleSmallDto = roleService.findByName(Roles.CUSTOMER.name());
        UserDto userDto = UserDto.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .type(UserType.CUSTOMER)
                .phone(req.getPhone())
                .enabled(true)
                .roles(Set.of(roleSmallDto))
                .build();
        userService.save(userDto);
    }

    public TokenPair signin(SigninRequest signinRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(
                signinRequest.getUsername(),
                signinRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        return createAndSaveToken(authentication);
    }

    public void signout(String authorizationHeader) {
        String accessToken = TokenUtil.extractToken(authorizationHeader);
        log.info("logout accessToken: {}", accessToken);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        TokenPair currentTokenPair = redisRepository.read(authentication.getName());
        if (currentTokenPair == null || !Objects.equals(accessToken, currentTokenPair.getAccessToken()))
            throw new InvalidAccessTokenException();

        redisRepository.delete(authentication.getName());
    }

    public void verify(String authorizationHeader) {
        String accessToken = TokenUtil.extractToken(authorizationHeader);
        Authentication authentication = tokenProvider.parseAuthentication(accessToken);
        TokenPair tokenPair = redisRepository.read(authentication.getName());

        Optional.ofNullable(tokenPair)
                .map(TokenPair::getAccessToken)
                .filter(accessToken::equals)
                .orElseThrow(InvalidAccessTokenException::new);
    }

    public TokenPair refreshToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken, InvalidRefreshTokenException::getInstance);
        Authentication authentication = tokenProvider.parseAuthentication(refreshToken);
        TokenPair currentTokenPair = redisRepository.read(authentication.getName());

        if (currentTokenPair == null || !Objects.equals(refreshToken, currentTokenPair.getRefreshToken()))
            throw InvalidRefreshTokenException.getInstance();

        TokenPair newTokenPair = tokenCreator.createTokenPair(authentication);
        redisRepository.update(authentication.getName(), newTokenPair);

        return newTokenPair;
    }

    private TokenPair createAndSaveToken(Authentication authentication) {
        TokenPair tokenPair = tokenCreator.createTokenPair(authentication);
        redisRepository.save(authentication.getName(), tokenPair);
        return tokenPair;
    }

}
