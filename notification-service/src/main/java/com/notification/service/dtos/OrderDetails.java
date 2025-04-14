package com.notification.service.dtos;

import lombok.Data;

@Data
public class OrderDetails {
    private String orderId;
    private String emailId;
    private String userId;
    private String userPhone;
    private boolean orderPaymentStatus = false;
    private boolean orderStatus = false;
    private String courseId;
}
