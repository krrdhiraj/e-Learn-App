package com.order.service.functions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class NotifyService {

    @Bean
    public Consumer<String> consumeAck(){
        return (orderId ->{
            updateOrder(orderId);
            System.out.println("Ack notification success : " + orderId);
        });
    }

    private void updateOrder(String orderId) {
        // logic to update the order and their status
    }
}
