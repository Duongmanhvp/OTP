package com.example.OTP.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerifitionRequest {
	private String email;
	private String otp;
	// Other fields, getters, and setters
}
