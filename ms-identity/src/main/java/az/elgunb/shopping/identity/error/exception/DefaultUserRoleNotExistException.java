package az.elgunb.shopping.identity.error.exception;

import az.elgunb.shopping.common.error.exception.CommonAuthException;

public class DefaultUserRoleNotExistException extends CommonAuthException {

    public DefaultUserRoleNotExistException() {
        super(401, "There is no default role of user");
    }

}
