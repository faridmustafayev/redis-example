package com.example.ms.user.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.FieldDefaults;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@FieldDefaults(level = PRIVATE)
public class CacheConfiguration {
    @Value("${redis.server.url}")
    String redisServer;

//    @Bean
//    public RedissonClient redissonSingleClient() {
//        Config config = new Config();
//        config
//                .setCodec(new SerializationCodec())
//                .useSingleServer()
//                .setAddress(redisServer);
//        return Redisson.create(config);
//    }

    @Bean
    public RedissonClient redissonClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(objectMapper));

        config.useSingleServer()
                .setAddress(redisServer);

        return Redisson.create(config);
    }
}