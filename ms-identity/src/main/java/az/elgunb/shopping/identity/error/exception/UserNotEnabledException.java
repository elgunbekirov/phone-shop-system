package az.elgunb.shopping.identity.error.exception;

import az.elgunb.shopping.common.error.exception.CommonAuthException;

public class UserNotEnabledException extends CommonAuthException {

    public UserNotEnabledException() {
        super(401, "User is not enabled");
    }

}
