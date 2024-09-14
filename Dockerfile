# Use the official OpenJDK Alpine image for Java 17
FROM openjdk:17

EXPOSE 8080
#RUN apk --no-cache add curl=8.5.0-r0 && \
#    addgroup -g 1000 -S piuser && adduser -u 1000 -S piuser -G piuser && \
#    chown -R piuser:piuser /usr/

#HEALTHCHECK --interval=45s --timeout=15s CMD curl -k --fail https://localhost:8080/actuator/health || exit 1
#USER piuser
# Create an application directory
WORKDIR /usr/src/app

# Copy the JAR file to the container
COPY target/empirefx-fxbo-0.0.1-SNAPSHOT.jar /usr/src/app/empirefx-fxbo-0.0.1-SNAPSHOT.jar

# Set environment variables (optional)
ENV JAVA_OPTS=""

# Command to run the JAR file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /usr/src/app/empirefx-fxbo-0.0.1-SNAPSHOT.jar"]

