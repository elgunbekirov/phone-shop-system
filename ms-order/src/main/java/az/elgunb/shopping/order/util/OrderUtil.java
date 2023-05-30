package az.elgunb.shopping.order.util;

import az.elgunb.shopping.order.dto.OrderDto;
import az.elgunb.shopping.order.enums.NotificationMessage;
import az.elgunb.shopping.order.messaging.event.EmailNotificationEvent;
import az.elgunb.shopping.order.messaging.event.PhoneOrderEvent;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class OrderUtil {

    private static final double DEFAULT_TAX_PERCENT = 18.0;    // VAT
    private static final String MAIN_ADDRESS_LOCATION = "40.4403340:49.8075000";

    private OrderUtil() {
    }

    public static BigDecimal calculateTotalOrderAmount(final double price) {
        return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(DEFAULT_TAX_PERCENT));
    }

    public static double calculateRandomPrice() {
        DecimalFormat df = new DecimalFormat("#.###");
        return Double.parseDouble(df.format(Math.random()));
    }

    public static PhoneOrderEvent createPhoneOrderEvent(OrderDto dto) {
        return PhoneOrderEvent.builder()
                .orderNumber(dto.getOrderNumber())
                .destination(dto.getDestination())
                .status(dto.getStatus())
                .customer(dto.getCustomer())
                .coordination(getAddressLocation())
                .build();
    }

    public static String getAddressLocation() {
        return MAIN_ADDRESS_LOCATION;
    }

    public static EmailNotificationEvent createEmailNotificationEvent(OrderDto dto, NotificationMessage notification) {
        return EmailNotificationEvent.builder()
                .orderNumber(dto.getOrderNumber())
                .name(dto.getName())
                .destination(dto.getDestination())
                .status(dto.getStatus())
                .weight(dto.getWeight())
                .amount(dto.getAmount())
                .email(dto.getCreatedBy())
                .message(notification.message())
                .build();
    }

}
