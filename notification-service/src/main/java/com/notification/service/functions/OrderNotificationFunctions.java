package com.notification.service.functions;

import com.notification.service.dtos.OrderDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.function.Function;

@Configuration
public class OrderNotificationFunctions {

    @Bean
    public Function<OrderDetails, String> orderEventReceiver(){
        return (orderDetails -> {
            // process the event
            System.out.println("Sending notification to user");
            logicToSendEmailAndMessageToUser(orderDetails.getEmailId(), orderDetails.getUserPhone());
//            return "Order notification send to user";  // acknowledge ke time only this will return but to receive ke time pura body return krega
            return orderDetails.getOrderId();
        });
    }

    private void logicToSendEmailAndMessageToUser(String emailId, String userPhone) {

        System.out.println("Sending Email to " + emailId);
        System.out.println("Sending Email to " + userPhone);
        System.out.println("Notification send to user");
        System.out.println("----------------------------");
    }
}
