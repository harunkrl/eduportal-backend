package com.eduportal.service;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.InstructorDTO;
import com.eduportal.dto.request.InstructorCreateRequest;
import java.util.List;

public interface InstructorService {
    InstructorDTO createInstructor(InstructorCreateRequest request);
    InstructorDTO getInstructorById(Long id);
    List<InstructorDTO> getAllInstructors();
    InstructorDTO updateInstructor(Long id, InstructorCreateRequest request);
    void deleteInstructor(Long id);
    List<CourseDTO> getInstructorCourses(Long id);
}