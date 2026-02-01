package com.casestudy.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String role;
    private String gender;
    private int age;
    private String number;
}
