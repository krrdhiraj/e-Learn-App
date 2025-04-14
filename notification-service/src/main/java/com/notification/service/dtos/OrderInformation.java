package com.notification.service.dtos;

import lombok.Data;

import java.sql.Date;

@Data
public class OrderInformation {

    private String orderId;
    private Date createdDate;
    private Double price;
    private String emailId;
    private String userPhone;
}
