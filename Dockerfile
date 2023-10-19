FROM eclipse-temurin:17.0.3_7-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:17.0.3_7-jdk-alpine
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-Xmx1536m","-XX:CompressedClassSpaceSize=200m","-XX:+UseContainerSupport","-XshowSettings:vm","-jar","/app.jar"]
