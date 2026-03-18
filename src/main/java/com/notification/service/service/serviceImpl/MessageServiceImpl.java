package com.notification.service.service.serviceImpl;

import com.notification.service.dto.EmailRequest;
import com.notification.service.dto.SmsRequest;
import com.notification.service.service.MessageService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private JavaMailSender mailSender;

    /* ================================
       Twilio Configuration
    ================================= */

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    /* ================================
       MSG91 Configuration
    ================================= */

    @Value("${sms.auth.key}")
    private String authKey;

    @Value("${sms.sender.id}")
    private String senderId;


    /* ================================
       Email Service
    ================================= */

    @Override
    public String sendEmail(EmailRequest request) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getToEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());

            mailSender.send(message);

            return "Email sent successfully!";

        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    /* ================================
       Twilio SMS Service
    ================================= */

    public String sendSmsUsingTwilio(SmsRequest request) {

        try {

            Twilio.init(accountSid, authToken);

            Message message = Message.creator(
                    new PhoneNumber(request.getToPhoneNumber()),
                    new PhoneNumber(fromPhoneNumber),
                    request.getMessage()
            ).create();

            return "SMS sent successfully! SID: " + message.getSid();

        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }

    /* ================================
       MSG91 SMS Service
    ================================= */

    public String sendSmsUsingMsg91(SmsRequest request) {

        try {

            String url = "https://control.msg91.com/api/v5/flow/";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("authkey", authKey);

            Map<String, Object> body = new HashMap<>();

            body.put("template_id", request.getTemplateId()); // ⭐ IMPORTANT
            body.put("sender", senderId);
            body.put("mobiles", "91" + request.getToPhoneNumber());

            // Variable mapping (if template has VAR1)
            body.put("VAR1", request.getMessage());

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            return response.getBody();

        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }

}
