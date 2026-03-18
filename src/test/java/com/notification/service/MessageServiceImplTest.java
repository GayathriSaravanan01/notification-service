package com.notification.service;

import com.notification.service.dto.EmailRequest;
import com.notification.service.dto.SmsRequest;
import com.notification.service.service.serviceImpl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* ================================
       EMAIL TEST CASES
    ================================= */

    @Test
    void testSendEmail_Success() {

        EmailRequest request = new EmailRequest();
        request.setToEmail("test@gmail.com");
        request.setSubject("Test Subject");
        request.setMessage("Test Message");

        String response = messageService.sendEmail(request);

        assertEquals("Email sent successfully!", response);

        verify(mailSender, times(1))
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_Failure() {

        EmailRequest request = new EmailRequest();
        request.setToEmail("test@gmail.com");
        request.setSubject("Test Subject");
        request.setMessage("Test Message");

        doThrow(new RuntimeException("Mail error"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        String response = messageService.sendEmail(request);

        assertTrue(response.contains("Failed to send email"));
    }

    /* ================================
       TWILIO SMS TEST CASE
    ================================= */

    @Test
    void testSendSmsUsingTwilio_Failure() {

        SmsRequest request = new SmsRequest();
        request.setToPhoneNumber("9876543210");
        request.setMessage("Hello");

        // Since Twilio uses static methods, we test failure scenario
        String response = messageService.sendSmsUsingTwilio(request);

        assertTrue(response.contains("Failed to send SMS"));
    }

    /* ================================
       MSG91 SMS TEST CASE
    ================================= */

}