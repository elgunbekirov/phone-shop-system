package az.elgunb.shopping.order.error.exception;

import az.elgunb.shopping.common.error.exception.CommonBadRequestException;

public class OrderCanNotCancelException extends CommonBadRequestException {

    public OrderCanNotCancelException() {
        super(400, "Order is in progress so it can not be cancelled");
    }

}
