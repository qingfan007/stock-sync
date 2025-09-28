FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/stock-sync-*.jar app.jar

RUN mkdir -p /tmp/vendor-b

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]