# Stage 1: Build
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# Install Maven (no wrapper in repo)
RUN apk add --no-cache maven

COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2: Runtime (JRE only for smaller image)
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]
