package com.eduportal.service;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.StudentDTO;
import com.eduportal.dto.request.CourseCreateRequest;
import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseCreateRequest request);
    CourseDTO getCourseById(Long id);
    List<CourseDTO> getAllCourses();
    CourseDTO updateCourse(Long id, CourseCreateRequest request);
    void deleteCourse(Long id);
    CourseDTO assignInstructor(Long courseId, Long instructorId);
    List<StudentDTO> getCourseStudents(Long courseId);
    List<CourseDTO> getCoursesByInstructor(Long instructorId);
}