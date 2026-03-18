package com.notification.service.controller;


import com.notification.service.dto.EmailRequest;
import com.notification.service.dto.SmsRequest;
import com.notification.service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/msg")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/email")
    public String sendEmail(@RequestBody EmailRequest request) {
        return messageService.sendEmail(request);
    }

    @PostMapping("/phoneTwilio")
    public String sendSmsUsingTwilio(@RequestBody SmsRequest request) {
        return messageService.sendSmsUsingTwilio(request);
    }
//    @PostMapping("/phoneMsg91")
//    public String sendSmsUsingMsg91(@RequestBody SmsRequest request) {
//        return messageService.sendSmsUsingMsg91(request);
//    }

}
