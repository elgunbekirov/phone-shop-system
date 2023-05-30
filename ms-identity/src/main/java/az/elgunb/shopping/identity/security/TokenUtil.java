package az.elgunb.shopping.identity.security;

import az.elgunb.shopping.common.constant.CommonConstants.HttpAttribute;

public final class TokenUtil {

    private TokenUtil() {
    }

    public static String extractToken(String authorizationHeader) {
        TokenValidator.validateAuthorizationHeader(authorizationHeader);
        return authorizationHeader.replace(HttpAttribute.BEARER, "").trim();
    }

}
