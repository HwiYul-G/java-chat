# Build Stage
FROM eclipse-temurin:17-jre AS builder

WORKDIR /app

# Copy JAR file into the container
ARG JAR_FILE=target/java-chat-0.0.1.jar
COPY ${JAR_FILE} app.jar

# Extract Spring Boot layers from the JAR file
RUN java -Djarmode=layertools -jar app.jar extract

# Download model file
RUN mkdir -p src/main/resources && \
    wget "https://drive.google.com/uc?export=download&id=1rRRN_0KcAvmQamEB-2bD5QZttj9zrRy9" -O src/main/resources/model.onnx

# Final Image
FROM eclipse-temurin:17-jre
ENV TZ="Asia/Seoul"
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/app/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]