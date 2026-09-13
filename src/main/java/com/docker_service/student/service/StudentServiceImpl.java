package com.docker_service.student.service;

import com.docker_service.student.dto.StudentRequest;
import com.docker_service.student.dto.StudentResponse;
import com.docker_service.student.entity.Student;
import com.docker_service.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(cacheNames = "getAllStudentsList")
    public List<StudentResponse> getAllStudentsList() {
        /*return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());*/

        List<Student> studentList = studentRepository.findAll();

        return studentList.stream()
                .map(student -> modelMapper.map(student, StudentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "getStudentByID", key = "#id")
    public StudentResponse getStudentByID(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // return mapToResponse(student);

        return modelMapper.map(student, StudentResponse.class);
    }

    @Override
    public StudentResponse createStudent(StudentRequest studentRequest) {
        Student savedStudent = studentRepository.save(modelMapper.map(studentRequest, Student.class));
        return modelMapper.map(savedStudent, StudentResponse.class);
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail()
        );

    }


}
