package com.eduportal.service.impl;

import com.eduportal.entity.Course;
import com.eduportal.entity.Instructor;
import com.eduportal.entity.Student;
import com.eduportal.exception.ResourceNotFoundException;
import com.eduportal.repository.CourseRepository;
import com.eduportal.repository.InstructorRepository;
import com.eduportal.repository.StudentRepository;
import com.eduportal.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public Map<String, Object> getInstructorStatistics(Long instructorId) {
        Map<String, Object> stats = new HashMap<>();

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Akademisyen bulunamadı: " + instructorId));

        long totalStudents = instructor.getCourses().stream()
                .mapToLong(course -> course.getStudents().size())
                .sum();

        int totalCredits = instructor.getCourses().stream()
                .mapToInt(Course::getCredits)
                .sum();

        stats.put("totalCourses", instructor.getCourses().size());
        stats.put("totalStudents", totalStudents);
        stats.put("totalCredits", totalCredits);
        stats.put("averageStudentsPerCourse", instructor.getCourses().isEmpty() ? 0 :
                (double) totalStudents / instructor.getCourses().size());

        return stats;
    }

    @Override
    public Map<String, Object> getCourseStatistics(Long courseId) {
        Map<String, Object> stats = new HashMap<>();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı: " + courseId));

        stats.put("totalStudents", course.getStudents().size());
        stats.put("credits", course.getCredits());
        stats.put("instructor", String.format("%s %s",
                course.getInstructor().getFirstName(),
                course.getInstructor().getLastName()));

        return stats;
    }

    @Override
    public Map<String, Object> getStudentStatistics(Long studentId) {
        Map<String, Object> stats = new HashMap<>();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci bulunamadı: " + studentId));

        int totalCredits = student.getCoursesSelected().stream()
                .mapToInt(Course::getCredits)
                .sum();

        stats.put("totalCourses", student.getCoursesSelected().size());
        stats.put("totalCredits", totalCredits);
        stats.put("averageCreditsPerCourse", student.getCoursesSelected().isEmpty() ? 0 :
                (double) totalCredits / student.getCoursesSelected().size());

        return stats;
    }

    @Override
    public Map<String, Object> getGeneralStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalStudents = studentRepository.count();
        long totalInstructors = instructorRepository.count();
        long totalCourses = courseRepository.count();

        stats.put("totalStudents", totalStudents);
        stats.put("totalInstructors", totalInstructors);
        stats.put("totalCourses", totalCourses);
        stats.put("averageCoursesPerInstructor", totalInstructors == 0 ? 0 :
                (double) totalCourses / totalInstructors);
        stats.put("averageStudentsPerCourse", totalCourses == 0 ? 0 :
                (double) totalStudents / totalCourses);

        return stats;
    }
}