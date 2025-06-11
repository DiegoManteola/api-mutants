############################
# ----- BUILD STAGE ------ #
############################
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Damos permiso de ejecución al wrapper y compilamos
RUN chmod +x mvnw \
 && ./mvnw -B -DskipTests clean package

############################
# ---- RUNTIME STAGE ----- #
############################
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el jar generado desde la etapa build
COPY --from=build /app/target/mutants-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
