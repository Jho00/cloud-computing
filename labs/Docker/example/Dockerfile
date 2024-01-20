FROM maven:3.6.3-openjdk-17
COPY ./ ./
RUN mvn clean package -Dmaven.test.skip=true
CMD ["java", "-jar", "target/Library-0.0.1-SNAPSHOT.jar"]