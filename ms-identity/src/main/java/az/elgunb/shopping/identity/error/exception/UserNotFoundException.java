package az.elgunb.shopping.identity.error.exception;

import az.elgunb.shopping.common.error.exception.CommonAuthException;

public class UserNotFoundException extends CommonAuthException {

    public UserNotFoundException() {
        super(401, "User is not found!");
    }

}
