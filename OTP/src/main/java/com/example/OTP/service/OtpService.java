package com.example.OTP.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
	private final RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	public OtpService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	
	public String generateOTP(String email) {
		try {
			String otp = String.format("%06d", new Random().nextInt(999999));
			String key = "OTP:" + email;
			redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(3));
			return otp;
		} catch (RedisConnectionFailureException e) {
		//	logger.error("Failed to connect to Redis", e);
			// Implement a fallback mechanism or rethrow a custom exception
			throw new RuntimeException("OTP service unavailable", e);
		}
	}
	
	public boolean validateOTP(String email, String otp) {
		String key = "OTP:" + email;
		String storedOtp = redisTemplate.opsForValue().get(key);
		if (storedOtp != null && storedOtp.equals(otp)) {
			redisTemplate.delete(key);
			return true;
		}
		return false;
	}
	
//	@Scheduled(fixedRate = 300000) // Run every 5 minutes
//	public void clearExpiredOTPs() {
//		LocalDateTime now = LocalDateTime.now();
//		otpMap.entrySet().removeIf(entry -> now.isAfter(entry.getValue().getExpiryTime()));
//	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	private static class OtpData {
		private String otp;
		private LocalDateTime expiryTime;
		
		// Constructor, getters, and setters
	}
}
