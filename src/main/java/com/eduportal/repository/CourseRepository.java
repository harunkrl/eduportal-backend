package com.eduportal.repository;

import com.eduportal.entity.Course;
import com.eduportal.entity.Instructor;
import com.eduportal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(Instructor instructor);
    List<Course> findByStudentsContaining(Student student);
    boolean existsByCourseName(String courseName);

    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId")
    List<Course> findCoursesByInstructorId(@Param("instructorId") Long instructorId);

    @Query("SELECT c FROM Course c WHERE :student MEMBER OF c.students")
    List<Course> findCoursesByStudent(@Param("student") Student student);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.instructor.id = :instructorId")
    Long countCoursesByInstructor(@Param("instructorId") Long instructorId);
}