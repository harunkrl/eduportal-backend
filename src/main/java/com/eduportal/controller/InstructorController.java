package com.eduportal.controller;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.InstructorDTO;
import com.eduportal.dto.request.InstructorCreateRequest;
import com.eduportal.dto.response.ApiResponse;
import com.eduportal.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<ApiResponse<InstructorDTO>> createInstructor(
            @Valid @RequestBody InstructorCreateRequest request) {
        InstructorDTO instructor = instructorService.createInstructor(request);
        return ResponseEntity.ok(ApiResponse.success("Akademisyen başarıyla oluşturuldu", instructor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorDTO>> getInstructor(@PathVariable Long id) {
        InstructorDTO instructor = instructorService.getInstructorById(id);
        return ResponseEntity.ok(ApiResponse.success(instructor));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstructorDTO>>> getAllInstructors() {
        List<InstructorDTO> instructors = instructorService.getAllInstructors();
        return ResponseEntity.ok(ApiResponse.success(instructors));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorDTO>> updateInstructor(
            @PathVariable Long id,
            @Valid @RequestBody InstructorCreateRequest request) {
        InstructorDTO instructor = instructorService.updateInstructor(id, request);
        return ResponseEntity.ok(ApiResponse.success("Akademisyen başarıyla güncellendi", instructor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.ok(ApiResponse.success("Akademisyen başarıyla silindi"));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getInstructorCourses(@PathVariable Long id) {
        List<CourseDTO> courses = instructorService.getInstructorCourses(id);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }
}