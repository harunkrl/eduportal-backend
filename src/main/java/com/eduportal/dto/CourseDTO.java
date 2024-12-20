package com.eduportal.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String courseName;
    private Integer credits;
    private InstructorDTO instructor;
    private List<StudentDTO> students;
}