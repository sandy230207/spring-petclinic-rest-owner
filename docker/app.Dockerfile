FROM openjdk:8-jdk-slim

WORKDIR /application
COPY . .
RUN ./mvnw clean install
EXPOSE 9966

ENTRYPOINT ./mvnw spring-boot:run