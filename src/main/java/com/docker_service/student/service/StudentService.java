package com.docker_service.student.service;


import com.docker_service.student.dto.StudentRequest;
import com.docker_service.student.dto.StudentResponse;

import java.util.List;

public interface StudentService {
    List<StudentResponse> getAllStudentsList();

    StudentResponse getStudentByID(Integer id);

    StudentResponse createStudent(StudentRequest studentRequest);
}


