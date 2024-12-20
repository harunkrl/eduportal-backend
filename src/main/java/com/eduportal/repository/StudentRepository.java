package com.eduportal.repository;

import com.eduportal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.coursesSelected WHERE s.id = :id")
    Optional<Student> findByIdWithCourses(@Param("id") Long id);

    @Query("SELECT COUNT(s) FROM Student s JOIN s.coursesSelected c WHERE c.id = :courseId")
    Long countStudentsByCourse(@Param("courseId") Long courseId);

    @Query("SELECT s FROM Student s WHERE s.major = :major")
    List<Student> findByMajor(@Param("major") String major);
}