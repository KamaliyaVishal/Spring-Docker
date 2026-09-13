package com.docker_service.student.controller;

import com.docker_service.student.dto.StudentRequest;
import com.docker_service.student.dto.StudentResponse;
import com.docker_service.student.service.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentServiceImpl studentService;

    @GetMapping("/")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getStudent() {
        List<StudentResponse> studentResponse = studentService.getAllStudentsList();
        return new ResponseEntity<>(studentResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentByID(@PathVariable Integer id) {
        StudentResponse studentResponse = studentService.getStudentByID(id);
        return ResponseEntity.ok(studentResponse);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest studentRequest) {
        StudentResponse studentResponse = studentService.createStudent(studentRequest);
        return new ResponseEntity<>(studentResponse, HttpStatus.CREATED);
    }




}
