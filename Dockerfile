FROM eclipse-temurin:17

LABEL mentainer="thongtranr27@gmail.com"

WORKDIR /app

COPY target/springboot-chatapp-rest-api-0.0.1-SNAPSHOT.jar /app/springboot-chatapp-rest-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "springboot-chatapp-rest-api-0.0.1-SNAPSHOT.jar"]