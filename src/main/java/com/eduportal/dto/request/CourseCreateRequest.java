package com.eduportal.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequest {
    @NotBlank(message = "Ders adı zorunludur")
    private String courseName;

    @NotNull(message = "Kredi sayısı zorunludur")
    @Min(1)
    private Integer credits;

    @NotNull(message = "Akademisyen seçimi zorunludur")
    private Long instructorId;
}