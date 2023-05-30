package az.elgunb.shopping.identity.security;

import az.elgunb.shopping.common.constant.CommonConstants.HttpAttribute;
import az.elgunb.shopping.identity.error.exception.InvalidAccessTokenException;
import org.apache.commons.lang3.StringUtils;

public final class TokenValidator {

    private TokenValidator() {
    }

    public static void validateAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader) ||
                !authorizationHeader.startsWith(HttpAttribute.BEARER) ||
                StringUtils.isBlank(authorizationHeader.replace(HttpAttribute.BEARER, "")))
            throw new InvalidAccessTokenException();
    }

}
