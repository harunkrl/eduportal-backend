package com.eduportal.controller;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.StudentDTO;
import com.eduportal.dto.request.CourseCreateRequest;
import com.eduportal.dto.response.ApiResponse;
import com.eduportal.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(
            @Valid @RequestBody CourseCreateRequest request) {
        CourseDTO course = courseService.createCourse(request);
        return ResponseEntity.ok(ApiResponse.success("Ders başarıyla oluşturuldu", course));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourse(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(course));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateRequest request) {
        CourseDTO course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Ders başarıyla güncellendi", course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Ders başarıyla silindi"));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getCourseStudents(@PathVariable Long id) {
        List<StudentDTO> students = courseService.getCourseStudents(id);
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @PostMapping("/{courseId}/instructor/{instructorId}")
    public ResponseEntity<ApiResponse<CourseDTO>> assignInstructor(
            @PathVariable Long courseId,
            @PathVariable Long instructorId) {
        CourseDTO course = courseService.assignInstructor(courseId, instructorId);
        return ResponseEntity.ok(ApiResponse.success("Akademisyen başarıyla atandı", course));
    }
}