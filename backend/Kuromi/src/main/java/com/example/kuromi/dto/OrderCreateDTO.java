package com.example.kuromi.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {
    private String username;
    private String consignee;
    private String phone;
    private String address;
}
