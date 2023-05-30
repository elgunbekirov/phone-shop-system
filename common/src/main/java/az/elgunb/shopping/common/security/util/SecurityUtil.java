package az.elgunb.shopping.common.security.util;

import az.elgunb.shopping.common.enums.UserType;
import az.elgunb.shopping.common.security.model.CustomUserPrincipal;
import az.elgunb.shopping.common.constant.CommonConstants.HttpAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class SecurityUtil {

    private static final String AUTHORIZATION_HEADER = HttpAttribute.AUTHORIZATION;
    private static final String TOKEN_PREFIX_BEARER = HttpAttribute.BEARER;

    private SecurityUtil() {
    }

    public static String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX_BEARER))
            return authHeader.substring(TOKEN_PREFIX_BEARER.length());
        return null;
    }

    public static Optional<CustomUserPrincipal> getCurrentUserPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(CustomUserPrincipal.class::isInstance)
                .map(CustomUserPrincipal.class::cast);
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()))
                .orElse("anonymous");
    }

    public static UserType getCurrentUserType() {
        return getCurrentUserPrincipal()
                .map(CustomUserPrincipal::getUserType)
                .orElse(null);
    }

    public static boolean hasAdminRole() {
        if (getCurrentUserPrincipal().isPresent()) {
            return getCurrentUserPrincipal().get().getAuth().contains("ROLE_ADMIN");
        }
        return false;
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

}
