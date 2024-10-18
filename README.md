# ![RealWorld Example App](logo.png)

> ### Spring Boot codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.


### [Demo](https://demo.realworld.io/)&nbsp;&nbsp;&nbsp;&nbsp;[RealWorld](https://github.com/gothinkster/realworld)


This codebase was created to demonstrate a fully fledged fullstack application built with **Spring Boot** including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the **Spring Boot** community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.


# How it works
- Spring Boot for auto configuration of dependencies
- Spring Data JPA for persistence with MySQL
- Spring Security to secure application using self-signed JWTs generated with oauth2-resource-server

# Getting started

## Build
```
./mvnw clean install -U
```
Follow instructions [here](https://www.danvega.dev/blog/spring-security-jwt#rsa-public-private-keys) to generate public and private keys for signing JWTs.

Add MySQL credentials to env.properties file
```
DB_NAME=your_db_name
DB_USER=your_username
DB_PASSWORD=your_password
```

# References
[Spring Boot best practices by arsy786](https://github.com/arsy786/springboot-best-practices/tree/master)\
[How to Secure your REST APIs with Spring Security & JSON Web Tokens (JWTs)](https://www.danvega.dev/blog/spring-security-jwt)\
[OAuth 2.0 and OpenID Connect (in plain English)](https://www.youtube.com/watch?v=996OiexHze0)\
[Introduction to JSON Web Tokens](https://jwt.io/introduction)
