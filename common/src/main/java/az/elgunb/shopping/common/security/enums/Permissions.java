package az.elgunb.shopping.common.security.enums;

public enum Permissions {

    VIEW_USER,
    VIEW_USER_LIST,
    ADD_USER,
    UPDATE_USER,
    DELETE_USER,

    VIEW_ROLE,
    ADD_ROLE,
    UPDATE_ROLE,
    DELETE_ROLE,

    VIEW_PERMISSION,
    ADD_PERMISSION,
    UPDATE_PERMISSION,
    DELETE_PERMISSION,

    CREATE_CUSTOMER,
    VIEW_CUSTOMER,

    VIEW_PHONE,
    UPDATE_PHONE,
    CHANGE_PHONE_STATUS,
    RECEIVE_PHONE,
    TRACK_PHONE,
    COMPLETE_PHONE_ORDER,

    VIEW_ORDER,
    CREATE_ORDER,
    UPDATE_ORDER,
    CHANGE_ORDER_DESTINATION,
    CANCEL_ORDER,
    ACCEPT_ORDER,
    CHANGE_ORDER_STATUS,
    ASSIGN_CUSTOMER;

    public String get() {
        return this.name().toLowerCase();
    }

}