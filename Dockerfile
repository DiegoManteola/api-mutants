############################
# ----- BUILD STAGE ------ #
############################
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

############################
# ---- RUNTIME STAGE ----- #
############################
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/mutants-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
