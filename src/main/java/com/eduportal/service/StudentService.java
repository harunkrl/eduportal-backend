package com.eduportal.service;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.StudentDTO;
import com.eduportal.dto.request.StudentCreateRequest;
import com.eduportal.dto.request.StudentUpdateRequest;

import java.util.List;

public interface StudentService {
    StudentDTO createStudent(StudentCreateRequest request);
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getAllStudents();
    StudentDTO updateStudent(Long id, StudentUpdateRequest request);
    void deleteStudent(Long id);
    void enrollCourse(Long studentId, Long courseId);
    void withdrawCourse(Long studentId, Long courseId);
    List<CourseDTO> getStudentCourses(Long studentId);
}