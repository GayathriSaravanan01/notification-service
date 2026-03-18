package com.notification.service.repository;

import com.notification.service.enums.OtpType;
import com.notification.service.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {

    Optional<Otp> findTopByEmailAndType(String email, OtpType otpType);

    Optional<Otp> findByEmailAndType(String email, OtpType otpType);
}
