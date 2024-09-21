FROM eclipse-temurin:17-jre AS builder
WORKDIR /app
ARG JAR_FILE=target/java-chat-0.0.1.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre
ENV TZ="Asia/Seoul"
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/app/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]