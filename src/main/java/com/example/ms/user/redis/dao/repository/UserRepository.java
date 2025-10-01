package com.example.ms.user.redis.dao.repository;

import com.example.ms.user.redis.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
