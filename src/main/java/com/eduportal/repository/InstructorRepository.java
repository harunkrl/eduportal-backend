package com.eduportal.repository;

import com.eduportal.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT i FROM Instructor i LEFT JOIN FETCH i.courses WHERE i.id = :id")
    Optional<Instructor> findByIdWithCourses(@Param("id") Long id);

    List<Instructor> findByDepartment(String department);

    @Query("SELECT i FROM Instructor i WHERE SIZE(i.courses) > 0")
    List<Instructor> findInstructorsWithCourses();
}