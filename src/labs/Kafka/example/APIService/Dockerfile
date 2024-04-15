FROM gradle:8.4.0-jdk17

WORKDIR /app
COPY . .

RUN gradle build
CMD ["java", "-jar", "build/libs/APIService-0.0.1-SNAPSHOT.jar"]
