# 1단계: 빌드 (Gradle로 실행 가능한 jar 생성)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle ./
COPY common common
COPY domain domain
COPY application application
COPY infra infra
COPY presentation presentation
COPY bootstrap bootstrap
RUN chmod +x gradlew
RUN ./gradlew :bootstrap:bootJar -x test --no-daemon

# 2단계: 실행 (빌드 도구 없이 jar만 실행)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/bootstrap/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
