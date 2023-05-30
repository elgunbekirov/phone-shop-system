package az.elgunb.shopping.identity.error.exception;

import az.elgunb.shopping.common.error.exception.CommonAuthException;

public class InvalidAccessTokenException extends CommonAuthException {

    public InvalidAccessTokenException() {
        super(401, "Invalid access token");
    }
}
