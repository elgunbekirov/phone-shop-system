package az.elgunb.shopping.order.error.exception;

import az.elgunb.shopping.common.error.exception.CommonBadRequestException;

public class CustomerMustNotBeAssignedException extends CommonBadRequestException {

    public CustomerMustNotBeAssignedException() {
        super(400, "Customer must not be assigned to unaccepted order");
    }

}
