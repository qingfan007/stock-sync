FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/stock-sync-*.jar app.jar

RUN mkdir -p /tmp/vendor-b

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]