package com.notification.service.dto;

import lombok.Data;

@Data
public class SmsRequest {
    private String toPhoneNumber;
    private String message;
    private String templateId;
}
