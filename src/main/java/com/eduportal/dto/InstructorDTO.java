package com.eduportal.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private List<CourseDTO> courses;
}