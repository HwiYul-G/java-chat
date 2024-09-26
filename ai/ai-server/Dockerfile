FROM eclipse-temurin:17-jre as builder
WORKDIR /app
ARG JAR_FILE=target/ai-server-0.0.1.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract


RUN mkdir -p src/main/resources && \
    wget "https://drive.google.com/uc?export=download&id=1rRRN_0KcAvmQamEB-2bD5QZttj9zrRy9" -O src/main/resources/model.onnx


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]