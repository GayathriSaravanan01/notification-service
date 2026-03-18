package com.notification.service.model;

import com.notification.service.enums.OtpType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    private String password;
    private String otp;
    @Enumerated(EnumType.STRING)
    private OtpType type;
    private LocalDateTime otpExpiry;
    private LocalDateTime createdAt = LocalDateTime.now();

}
