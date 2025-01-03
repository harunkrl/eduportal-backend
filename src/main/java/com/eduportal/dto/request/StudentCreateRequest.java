package com.eduportal.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateRequest {
    @NotBlank(message = "Ad zorunludur")
    private String firstName;

    @NotBlank(message = "Soyad zorunludur")
    private String lastName;

    @NotBlank(message = "E-posta zorunludur")
    @Email
    private String email;

    @NotBlank(message = "Şifre zorunludur")
    private String password;

    @NotBlank(message = "Bölüm zorunludur")
    private String major;
}