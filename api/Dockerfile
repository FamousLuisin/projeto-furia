FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
WORKDIR /opt/app

COPY  gradlew .
COPY ./gradle ./gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew --no-daemon dependencies
COPY ./src ./src
RUN ./gradlew --no-daemon bootJar --stacktrace

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/build/libs/*.jar /opt/app/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]