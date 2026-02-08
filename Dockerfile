FROM eclipse-temurin:25 AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x gradlew \
 && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=demo

ENTRYPOINT ["java", "-jar", "app.jar"]