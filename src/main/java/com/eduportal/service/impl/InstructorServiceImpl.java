package com.eduportal.service.impl;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.InstructorDTO;
import com.eduportal.dto.request.InstructorCreateRequest;
import com.eduportal.entity.Course;
import com.eduportal.entity.Instructor;
import com.eduportal.exception.BusinessException;
import com.eduportal.exception.ResourceNotFoundException;
import com.eduportal.repository.CourseRepository;
import com.eduportal.repository.InstructorRepository;
import com.eduportal.service.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public InstructorDTO createInstructor(InstructorCreateRequest request) {
        if (instructorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten kullanımda");
        }

        Instructor instructor = Instructor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .build();

        Instructor savedInstructor = instructorRepository.save(instructor);
        log.info("Yeni akademisyen oluşturuldu: {} {}",
                savedInstructor.getFirstName(), savedInstructor.getLastName());

        return convertToDTO(savedInstructor);
    }

    @Override
    public InstructorDTO getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + id));
        return convertToDetailedDTO(instructor);
    }

    @Override
    public List<InstructorDTO> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InstructorDTO updateInstructor(Long id, InstructorCreateRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + id));

        if (!instructor.getEmail().equals(request.getEmail()) &&
                instructorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten kullanımda");
        }

        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setEmail(request.getEmail());
        instructor.setDepartment(request.getDepartment());

        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Akademisyen güncellendi: {} {}",
                updatedInstructor.getFirstName(), updatedInstructor.getLastName());

        return convertToDTO(updatedInstructor);
    }

    @Override
    @Transactional
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + id));

        if (!instructor.getCourses().isEmpty()) {
            throw new BusinessException("Bu akademisyenin aktif dersleri var. Önce dersleri başka akademisyenlere atayın.");
        }

        instructorRepository.delete(instructor);
        log.info("Akademisyen silindi: {} {}", instructor.getFirstName(), instructor.getLastName());
    }

    @Override
    public List<CourseDTO> getInstructorCourses(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + id));

        return instructor.getCourses().stream()
                .map(this::convertCourseToDTO)
                .collect(Collectors.toList());
    }

    private InstructorDTO convertToDTO(Instructor instructor) {
        return InstructorDTO.builder()
                .id(instructor.getId())
                .firstName(instructor.getFirstName())
                .lastName(instructor.getLastName())
                .email(instructor.getEmail())
                .department(instructor.getDepartment())
                .build();
    }

    private InstructorDTO convertToDetailedDTO(Instructor instructor) {
        return InstructorDTO.builder()
                .id(instructor.getId())
                .firstName(instructor.getFirstName())
                .lastName(instructor.getLastName())
                .email(instructor.getEmail())
                .department(instructor.getDepartment())
                .courses(instructor.getCourses().stream()
                        .map(this::convertCourseToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private CourseDTO convertCourseToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .build();
    }
}