FROM gradle:7.5-jdk17 AS build
WORKDIR /app
COPY . .
run gradle build --no--daemon



FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from =build /app/build/libs/*.jar /app/usuarios.jar

EXPOSE 1974

CMD ["java", "-jar", "/app/usuarios.jar"]