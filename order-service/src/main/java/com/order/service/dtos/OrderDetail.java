package com.order.service.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    private String orderId;
    private  String emailId;
    private  String userId;
    private  String userPhone;
    private  boolean orderPaymentStatus=false;
    private  boolean orderStatus=false;
    private  String courseId;
    ///.. amount..

}
