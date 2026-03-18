package com.notification.service.service;

import com.notification.service.dto.EmailRequest;
import com.notification.service.dto.SmsRequest;

public interface MessageService {

    String sendEmail(EmailRequest request);
    String sendSmsUsingTwilio(SmsRequest request);
    String sendSmsUsingMsg91(SmsRequest request);
}