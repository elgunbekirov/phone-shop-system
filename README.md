# Phone Shopping System #

## API Gateway ##
- Netflix Zuul      - `git checkout master`
- KONG API gateway  - `git checkout kong-api-gateway`
- Spring Cloud Gateway  - `git checkout spring-cloud-gateway`

## Technologies ##
-  Java 11
-  Gradle
-  Spring Boot
-  Spring Security
-  Spring Cloud Stream Kafka
-  JUnit
-  PostgreSQL
-  Redis
-  Docker Compose


## Getting Started ##

- `start.sh` - start app
- `stop.sh`  - stop app

- `http://localhost:8080/` - Swagger (Swagger UI)

- `http://localhost:9000/` - Kafdrop (Kafka UI)

Note: You should have Docker and Java 11 installed

## Microservices ##
-  MS-Identity    authenticate user, authorize user, create, delete, get user  
-  MS-Order       book, return, accept product
