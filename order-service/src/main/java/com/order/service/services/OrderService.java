package com.order.service.services;

import com.order.service.dtos.OrderRequestDto;
import com.order.service.entities.Order;
import com.order.service.repositories.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class OrderService {

    private OrderRepository orderRepo;

    private RazorpayClient razorpayClient;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    public OrderService(OrderRepository orderRepo) throws RazorpayException {
        this.orderRepo = orderRepo;
    }

    public Order getOrder(String razorpayOrderId){

        return orderRepo.findByRazorpayOrderId(razorpayOrderId);
    }


    public Order createOrder(OrderRequestDto orderRequestDto) throws RazorpayException {

        razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);

        //creating : razorpay order id
        JSONObject options = new JSONObject();
        options.put("amount", orderRequestDto.getAmount() * 100);
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

        Order order = Order.builder()
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(orderRequestDto.getAmount() * 100)
                .pmtStatus("PENDING")
                .createdDate(LocalDate.now())
                .courseId(orderRequestDto.getCourseId())
                .userId(orderRequestDto.getUserId())
                .userName(orderRequestDto.getUserName()).
                build();
        return orderRepo.save(order);

    }

    // update order
    public Order updateOrder(String razorpayId, String status){
        Order order = orderRepo.findByRazorpayOrderId(razorpayId);

        if(order != null ){
            order.setPmtStatus(status);
            Order save = orderRepo.save(order);
            return save;
        }else{
            return null;
        }
    }
    // verify payment form Razorpay
    public boolean verifyPayment(
            String razorpayPaymentId,
            String razorpayOrderId,
            String razorpaySignature

    ) throws RazorpayException {
        razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", razorpayOrderId);
        options.put("razorpay_payment_id", razorpayPaymentId);
        options.put("razorpay_signature", razorpaySignature);
        boolean isVerifed = Utils.verifyPaymentSignature(options, razorpaySecret);
        if (isVerifed) {
            //success
            this.updateOrder(razorpayOrderId, "PAID");
            return true;
        } else {
            //fail
            return false;
        }
    }
}
