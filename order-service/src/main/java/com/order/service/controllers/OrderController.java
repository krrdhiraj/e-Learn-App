package com.order.service.controllers;

import com.order.service.dtos.OrderDetail;
import com.order.service.dtos.OrderRequestDto;
import com.order.service.dtos.PaymentVerifyDto;
import com.order.service.entities.Order;
import com.order.service.services.OrderService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin("*")
public class OrderController {

    private OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequestDto orderRequestDto
            ) throws RazorpayException {
        Order order = (Order) this.orderService.createOrder(orderRequestDto);

        // send notification to notification service: so that notification service will send the email and message to the user
//        orderCreatedNotification(order);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody PaymentVerifyDto paymentVerifyDto) throws RazorpayException {

        boolean verified = orderService.verifyPayment(
                paymentVerifyDto.getRazorpayPaymentId(),
                paymentVerifyDto.getRazorpayOrderId(),
                paymentVerifyDto.getRazorpaySignature());
        if(verified){
            OrderDetail orderDetails = new OrderDetail();
            Order order = orderService.getOrder(paymentVerifyDto.getRazorpayOrderId());
            orderDetails.setCourseId(order.getCourseId());
            orderDetails.setEmailId(order.getUserName());
            orderDetails.setOrderId(order.getUserId());
            orderDetails.setOrderPaymentStatus(true);
            orderCreatedNotification(new Order());
            return ResponseEntity.ok("order verified!");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment not verified");
        }
    }

    @Autowired
    private StreamBridge streamBridge;

    private void orderCreatedNotification(Order orderDetails){
        // login to send the notification service
        boolean send = streamBridge.send("orderCreatedEvent-out-0", orderDetails);

        if(send){
            System.out.println("Order Success event is successfully send to notification service");
        }else{
            System.out.println("Event fail");
        }
    }


}
