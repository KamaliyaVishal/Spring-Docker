package com.docker_service.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    Integer id;
    String name;
    String email;
}
