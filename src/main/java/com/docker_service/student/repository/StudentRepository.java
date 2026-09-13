package com.docker_service.student.repository;

import com.docker_service.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByName(String name);

    Student findByNameAndEmail(String name, String email);

    List<Student> findByNameLike(String likename);

}
