package com.example.ms.user.redis.service.concrete;

import com.example.ms.user.redis.config.UserCache;
import com.example.ms.user.redis.dao.entity.UserEntity;
import com.example.ms.user.redis.dao.repository.UserRepository;
import com.example.ms.user.redis.dto.request.UserRequest;
import com.example.ms.user.redis.dto.response.UserResponse;
import com.example.ms.user.redis.service.abstraction.UserService;
import com.example.ms.user.redis.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

import static com.example.ms.user.redis.enums.UserStatus.ACTIVE;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceHandler implements UserService {
    UserRepository userRepository;
    CacheUtil cacheUtil;

    @Override
    public UserResponse saveUser(UserRequest request) {

        UserEntity userEntity = UserEntity.builder()
                .firstName(request.getFirstName())
                .status(ACTIVE)
                .build();

        userRepository.save(userEntity);
//        cacheUtil.saveToCache(getKey(userEntity.getId()), userEntity, 1L, ChronoUnit.HOURS);

        UserCache cache = new UserCache(userEntity.getId(), userEntity.getFirstName(), "Mustafayev");
        cacheUtil.saveToCache(getKey(cache.getId()), cache, 1L, ChronoUnit.HOURS);

        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .createdAt(userEntity.getCreatedAt())
                .status(userEntity.getStatus())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    @Override
    public UserResponse findUserById(Long id) {
        UserCache cachedUser = cacheUtil.getBucket(getKey(id));
        if (cachedUser != null) {
            System.out.println("🔴 Redis-dən oxundu!");
            return UserResponse.builder()
                    .id(cachedUser.getId())
                    .firstName(cachedUser.getFirstName())
//                    .createdAt(cachedUser.getCreatedAt())
//                    .status(cachedUser.getStatus())
//                    .updatedAt(cachedUser.getUpdatedAt())
                    .build();
        }


        UserEntity userEntity = fetchUserIfExist(id);
//        cacheUtil.saveToCache(getKey(id), userEntity, 1L, ChronoUnit.HOURS);

        UserCache cache = new UserCache(userEntity.getId(), userEntity.getFirstName(), "Mustafayev");
        cacheUtil.saveToCache(getKey(cache.getId()), cache, 1L, ChronoUnit.HOURS);
        System.out.println("🟢 DB-dən oxundu və Redis-ə yazıldı!");

        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .createdAt(userEntity.getCreatedAt())
                .status(userEntity.getStatus())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        UserEntity cachedUser = cacheUtil.getBucket(getKey(id));

        if (cachedUser != null) {
            cacheUtil.delete(getKey(cachedUser.getId()));
            System.out.println("🔴 Redis-dən silindi!");
        }

        UserEntity userEntity = fetchUserIfExist(id);
        userEntity.setFirstName(request.getFirstName());
        cacheUtil.saveToCache(getKey(id), userEntity, 10L, ChronoUnit.MINUTES);
        System.out.println("🟢 DB-dən oxundu və Redis-ə yazıldı!");
        userRepository.save(userEntity);
        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .createdAt(userEntity.getCreatedAt())
                .status(userEntity.getStatus())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    private UserEntity fetchUserIfExist(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found: " + id));
    }

    private String getKey(Long id) {
        return "USER:" + id;
    }

}
