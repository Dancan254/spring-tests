package com.javaguy.testing.repository;

import com.javaguy.testing.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.school.name = :schoolName")
    List<Student> findBySchoolName(@Param("schoolName") String schoolName);

    @Query("SELECT s FROM Student s WHERE LOWER(s.firstname) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(s.lastname) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContaining(@Param("name") String name);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.school.id = :schoolId")
    Long countBySchoolId(@Param("schoolId") Long schoolId);

    void deleteByEmail(String email);
}
