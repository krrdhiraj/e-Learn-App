package com.notification.service.functions;


import com.notification.service.dtos.OrderInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class NotificationService {

    @Bean
    public Supplier<String> testing(){
        return ()-> "This is our notification service. for testing...";
    }

    @Bean
    public Function<String, String> sayHello(){
        return (message)-> "How are you ? : " + message;
    }

    @Bean
    public Function<OrderInformation, String> orderNotification(){
        // logic to send the notification.
        return orderInformation -> {
            // function for sending notification
            sendNotification(orderInformation);
            System.out.println("Sending the notification...");
            System.out.println(orderInformation.getOrderId());
            System.out.println(orderInformation.getCreatedDate());
            System.out.println(orderInformation.getPrice());
            System.out.println(orderInformation.getEmailId());
            return orderInformation.getOrderId();
        };
    }

    private void sendNotification(OrderInformation orderInformation) {
    }
}
