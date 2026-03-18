package com.notification.service.service;

public interface AuthService {
    String register(String email, String password);

    String verifyOtp(String email, String otp);
}
