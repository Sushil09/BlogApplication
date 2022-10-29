package com.springboot.blog.springbootblogrestapi.payload;

import lombok.Data;

@Data
public class LogInDto {
    private String usernameOrEmail;
    private String password;
}
