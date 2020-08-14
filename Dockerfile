FROM alpine:3.11
LABEL owner="Giancarlos Salas"
LABEL maintainer="giansalex@gmail.com"

ENV JAVA_HOME=/usr/lib/jvm/default-jvm/jre

RUN apk upgrade --update-cache; \
    apk add openjdk8-jre; \
    rm -rf /tmp/* /var/cache/apk/*

WORKDIR /app
COPY cpe-validator.jar cpe-validator.jar
COPY sunat_archivos/ sunat_archivos/

ENTRYPOINT ["java", "-jar", "cpe-validator.jar"]
