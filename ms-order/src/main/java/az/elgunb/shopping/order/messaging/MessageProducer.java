package az.elgunb.shopping.order.messaging;

import az.elgunb.shopping.order.messaging.event.EmailNotificationEvent;
import az.elgunb.shopping.order.messaging.event.PhoneOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final StreamBridge streamBridge;

    public void sendPhoneOrderEvent(PhoneOrderEvent event) {
        log.info("Send PhoneOrderEvent to ms-order: {}", event);
        streamBridge.send(OutputChannel.PHONE_ORDER, event);
    }

    public void sendEmailNotificationEvent(EmailNotificationEvent event) {
        log.info("Send EmailNotificationEvent to ms-notification: {}", event);
        streamBridge.send(OutputChannel.EMAIL_NOTIFICATION, event);
    }

}
