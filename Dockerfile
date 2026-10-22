FROM maven:3.9.9-eclipse-temurin-21-jammy AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN groupadd --system spring && useradd --system -g spring spring
USER spring:spring

COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT:-8080} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
