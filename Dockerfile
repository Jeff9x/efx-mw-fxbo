FROM eclipse-temurin:17-jdk-alpine AS jre-builder

WORKDIR /opt/app

RUN apk update && \
    apk add --no-cache tar binutils

COPY target/empirefx-fxbo-0.0.1-SNAPSHOT.jar .
COPY key.json .

RUN jar xvf empirefx-fxbo-0.0.1-SNAPSHOT.jar

RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 17  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    empirefx-fxbo-0.0.1-SNAPSHOT.jar > modules.txt

RUN $JAVA_HOME/bin/jlink \
        --verbose \
        --add-modules $(cat modules.txt) \
        --strip-debug \
        --no-man-pages \
        --no-header-files \
        --compress=2 \
        --output /optimized-jdk-17

FROM alpine:latest 

ENV JAVA_HOME=/opt/jdk/jdk-17
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=jre-builder /optimized-jdk-17 $JAVA_HOME

ARG USER=app

RUN addgroup --system $USER && \
    adduser --system $USER --ingroup $USER

RUN mkdir /app && chown -R $USER /app
COPY --chown=$USER:$USER target/empirefx-fxbo-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --chown=$USER:$USER key.json /app/key.json

WORKDIR /app
USER $USER

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

