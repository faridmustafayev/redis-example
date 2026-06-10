# Redis Integration with Spring Boot

## Overview

This project demonstrates how to integrate **Redis** with **Spring Boot** using **Redisson**. The implementation covers storing, retrieving, and deleting data from Redis while exploring different serialization approaches.

## Features

* Redis integration using Redisson
* Store and retrieve objects from Redis
* Cache management through a reusable utility class
* Object serialization using Java Serialization (`Serializable`)
* JSON serialization using Jackson
* Cache expiration (TTL) support
* Cache deletion support
* Redis inspection using `redis-cli`

---

## Technologies Used

* Java 17
* Spring Boot
* Redis
* Redisson
* Jackson
* Gradle

---

## Serialization Approaches

### 1. Java Native Serialization

The project initially used Java native serialization.

#### Requirements

The entity must implement `Serializable`.

```java
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
}
```

#### Notes

* Objects are stored as binary data.
* Redis CLI output is not human-readable.
* `serialVersionUID` is used for version control during deserialization.
* Suitable for Java-to-Java communication.

Example Redis output:

```text
\xac\xed\x00\x05sr...
```

---

### 2. JSON Serialization

The project was later configured to use Jackson-based JSON serialization.

```java
Config config = new Config();
config.setCodec(new JsonJacksonCodec());
```

#### Benefits

* Human-readable data
* Easier debugging
* Better interoperability between services
* Language-independent format

Example Redis output:

```json
{
  "id": 1,
  "firstName": "August",
  "status": "ACTIVE"
}
```

---

## Handling LocalDateTime

When using JSON serialization, `LocalDateTime` requires additional Jackson support.

### Dependency

```xml
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

### ObjectMapper Configuration

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JavaTimeModule());
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
```

---

## Cache Utility

A reusable cache utility was implemented to centralize Redis operations.

### Save to Cache

```java
cacheUtil.saveToCache(
        key,
        value,
        10L,
        ChronoUnit.MINUTES
);
```

### Read from Cache

```java
UserEntity user = cacheUtil.getBucket("USER:1");
```

### Delete from Cache

```java
cacheUtil.delete("USER:1");
```

Implementation:

```java
public boolean delete(String key) {
    return redissonClient.getKeys().delete(key) > 0;
}
```

---

## Redis Commands

### View All Keys

```bash
KEYS *
```

### Read Data

```bash
GET USER:1
```

### Delete a Key

```bash
DEL USER:1
```

### Clear Current Database

```bash
FLUSHDB
```

### Clear Entire Redis Instance

```bash
FLUSHALL
```

---

## What I Learned

* Redis fundamentals and cache management
* Redisson integration with Spring Boot
* Java native serialization using `Serializable`
* Purpose and usage of `serialVersionUID`
* JSON serialization with Jackson
* Handling Java 8+ date/time classes
* Redis key management and inspection
* Designing reusable cache utility classes

---

## Conclusion

This project provided hands-on experience with Redis caching in Spring Boot applications. Both Java native serialization and JSON serialization approaches were implemented and compared. The project also demonstrated cache expiration, cache deletion, object serialization, and Redis data management through Redisson.
