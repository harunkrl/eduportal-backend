package com.eduportal.service.impl;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.InstructorDTO;
import com.eduportal.dto.StudentDTO;
import com.eduportal.dto.request.CourseCreateRequest;
import com.eduportal.entity.Course;
import com.eduportal.entity.Instructor;
import com.eduportal.entity.Student;
import com.eduportal.exception.BusinessException;
import com.eduportal.exception.ResourceNotFoundException;
import com.eduportal.repository.CourseRepository;
import com.eduportal.repository.InstructorRepository;
import com.eduportal.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Override
    @Transactional
    public CourseDTO createCourse(CourseCreateRequest request) {
        if (courseRepository.existsByCourseName(request.getCourseName())) {
            throw new BusinessException("Bu isimde bir ders zaten mevcut");
        }

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + request.getInstructorId()));

        Course course = Course.builder()
                .courseName(request.getCourseName())
                .credits(request.getCredits())
                .instructor(instructor)
                .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Yeni ders oluşturuldu: {}", savedCourse.getCourseName());

        return convertToDTO(savedCourse);
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + id));
        return convertToDTO(course);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseCreateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + id));

        if (!course.getCourseName().equals(request.getCourseName()) &&
                courseRepository.existsByCourseName(request.getCourseName())) {
            throw new BusinessException("Bu isimde bir ders zaten mevcut");
        }

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + request.getInstructorId()));

        course.setCourseName(request.getCourseName());
        course.setCredits(request.getCredits());
        course.setInstructor(instructor);

        Course updatedCourse = courseRepository.save(course);
        log.info("Ders güncellendi: {}", updatedCourse.getCourseName());

        return convertToDTO(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + id));

        courseRepository.delete(course);
        log.info("Ders silindi: {}", course.getCourseName());
    }

    @Override
    @Transactional
    public CourseDTO assignInstructor(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + courseId));

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + instructorId));

        course.setInstructor(instructor);
        Course updatedCourse = courseRepository.save(course);
        log.info("Derse akademisyen atandı: {} -> {}", course.getCourseName(),
                instructor.getFirstName() + " " + instructor.getLastName());

        return convertToDTO(updatedCourse);
    }

    @Override
    public List<StudentDTO> getCourseStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + courseId));

        return course.getStudents().stream()
                .map(this::convertStudentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + instructorId));

        return instructor.getCourses().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CourseDTO convertToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .instructor(convertInstructorToDTO(course.getInstructor()))
                .students(course.getStudents().stream()
                        .map(this::convertStudentToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private InstructorDTO convertInstructorToDTO(Instructor instructor) {
        return InstructorDTO.builder()
                .id(instructor.getId())
                .firstName(instructor.getFirstName())
                .lastName(instructor.getLastName())
                .email(instructor.getEmail())
                .department(instructor.getDepartment())
                .build();
    }

    private StudentDTO convertStudentToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .major(student.getMajor())
                .build();
    }
}