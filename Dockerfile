# Use the official OpenJDK Alpine image for Java 17
FROM eclipse-temurin:17.0.9_9-jre-alpine

EXPOSE 8080 443 8778 9779
RUN apk --no-cache add curl=8.5.0-r0 && \
    addgroup -g 1000 -S piuser && adduser -u 1000 -S piuser -G piuser && \
    chown -R piuser:piuser /usr/

HEALTHCHECK --interval=45s --timeout=15s CMD curl -k --fail https://localhost:8080/actuator/health || exit 1
USER piuser

ENV JAVA_OPTS=""
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} /usr/app.jar
WORKDIR /usr/
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /usr/app.jar"]