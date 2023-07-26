FROM openjdk:17-alpine

WORKDIR /app/msvc-users

COPY ./pom.xml /app
COPY ./msvc-users .

RUN ./mvnw clean package -DSkipTests

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "./target/msvc-users-0.0.1-SNAPSHOT.jar"]