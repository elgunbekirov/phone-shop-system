package az.elgunb.shopping.gateway.security;

import az.elgunb.shopping.gateway.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver exceptionResolver;
    private final ApplicationProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        log.debug("JwtVerifyFilter is calling for uri: {} {}", request.getMethod(), request.getRequestURI());

        /**
         * You can write some logics in here. For example after signout token removed from redis, you can check token
         * if it exists in redis or not before allowing process.
         */

        filterChain.doFilter(request, response);
    }

}