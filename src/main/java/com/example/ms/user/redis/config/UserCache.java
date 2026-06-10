package com.example.ms.user.redis.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCache implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;
    private Long id;
    private String firstName;
    private String lastName;
}
