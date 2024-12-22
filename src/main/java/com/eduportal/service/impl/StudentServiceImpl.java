package com.eduportal.service.impl;

import com.eduportal.dto.CourseDTO;
import com.eduportal.dto.InstructorDTO;
import com.eduportal.dto.StudentDTO;
import com.eduportal.dto.request.StudentCreateRequest;
import com.eduportal.dto.request.StudentUpdateRequest;
import com.eduportal.entity.Course;
import com.eduportal.entity.Instructor;
import com.eduportal.entity.Student;
import com.eduportal.exception.BusinessException;
import com.eduportal.exception.ResourceNotFoundException;
import com.eduportal.repository.CourseRepository;
import com.eduportal.repository.StudentRepository;
import com.eduportal.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public StudentDTO createStudent(StudentCreateRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten kullanımda");
        }

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .major(request.getMajor())
                .build();

        Student savedStudent = studentRepository.save(student);
        log.info("Yeni öğrenci oluşturuldu: {} {}",
                savedStudent.getFirstName(), savedStudent.getLastName());

        return convertToDTO(savedStudent);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + id));
        return convertToDetailedDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentDTO updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + id));

        if (!student.getEmail().equals(request.getEmail()) &&
                studentRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Bu e-posta adresi zaten kullanımda");
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setMajor(request.getMajor());

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            student.setPassword(request.getPassword());
        }

        Student updatedStudent = studentRepository.save(student);
        log.info("Öğrenci güncellendi: {} {}",
                updatedStudent.getFirstName(), updatedStudent.getLastName());

        return convertToDTO(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + id));

        student.getCoursesSelected().clear();
        studentRepository.delete(student);

        log.info("Öğrenci silindi: {} {}", student.getFirstName(), student.getLastName());
    }

    @Override
    @Transactional
    public void enrollCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + courseId));

        if (student.getCoursesSelected().contains(course)) {
            throw new BusinessException("Öğrenci bu derse zaten kayıtlı");
        }

        student.getCoursesSelected().add(course);
        studentRepository.save(student);

        log.info("Öğrenci {} {} derse kaydoldu: {}",
                student.getFirstName(), student.getLastName(), course.getCourseName());
    }

    @Override
    @Transactional
    public void withdrawCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + courseId));

        if (!student.getCoursesSelected().contains(course)) {
            throw new BusinessException("Öğrenci bu derse kayıtlı değil");
        }

        student.getCoursesSelected().remove(course);
        studentRepository.save(student);

        log.info("Öğrenci {} {} dersten çıktı: {}",
                student.getFirstName(), student.getLastName(), course.getCourseName());
    }

    @Override
    public List<CourseDTO> getStudentCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + studentId));

        return student.getCoursesSelected().stream()
                .map(this::convertCourseToDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .major(student.getMajor())
                .build();
    }

    private StudentDTO convertToDetailedDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .major(student.getMajor())
                .coursesSelected(student.getCoursesSelected().stream()
                        .map(this::convertCourseToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private CourseDTO convertCourseToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .instructor(convertInstructorToDTO(course.getInstructor()))
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
}