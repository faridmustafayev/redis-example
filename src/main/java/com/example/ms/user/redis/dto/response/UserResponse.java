package com.example.ms.user.redis.dto.response;

import com.example.ms.user.redis.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserResponse {
    Long id;
    String firstName;
    UserStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
