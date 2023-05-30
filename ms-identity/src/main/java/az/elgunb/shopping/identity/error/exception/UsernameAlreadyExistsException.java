package az.elgunb.shopping.identity.error.exception;

import az.elgunb.shopping.common.error.exception.CommonBadRequestException;

public class UsernameAlreadyExistsException extends CommonBadRequestException {

    public UsernameAlreadyExistsException() {
        super(400, "Username already exists!");
    }

}
