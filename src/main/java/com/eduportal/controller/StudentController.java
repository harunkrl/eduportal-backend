package com.eduportal.controller;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.StudentDTO;
import com.eduportal.dto.request.StudentCreateRequest;
import com.eduportal.dto.request.StudentUpdateRequest;
import com.eduportal.dto.response.ApiResponse;
import com.eduportal.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(
            @Valid @RequestBody StudentCreateRequest request) {
        StudentDTO student = studentService.createStudent(request);
        return ResponseEntity.ok(ApiResponse.success("Öğrenci başarıyla oluşturuldu", student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudent(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success(student));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {
        StudentDTO student = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Öğrenci başarıyla güncellendi", student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Öğrenci başarıyla silindi"));
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<ApiResponse<Void>> enrollCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        studentService.enrollCourse(studentId, courseId);
        return ResponseEntity.ok(ApiResponse.success("Derse kayıt başarılı"));
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<ApiResponse<Void>> withdrawCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        studentService.withdrawCourse(studentId, courseId);
        return ResponseEntity.ok(ApiResponse.success("Dersten çıkış başarılı"));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getStudentCourses(@PathVariable Long id) {
        List<CourseDTO> courses = studentService.getStudentCourses(id);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }
}