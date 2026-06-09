package com.example.ms.user.redis.service.abstraction;

import com.example.ms.user.redis.dto.request.UserRequest;
import com.example.ms.user.redis.dto.response.UserResponse;

public interface UserService {
    UserResponse saveUser(UserRequest request);

    UserResponse findUserById(Long id);

    UserResponse updateUser(Long id, UserRequest request);
}
