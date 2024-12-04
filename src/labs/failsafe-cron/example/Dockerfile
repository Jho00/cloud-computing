FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package

FROM openjdk:17-alpine

COPY --from=build /app/target/FailsafeCRON-0.0.1-SNAPSHOT.jar ./root
COPY run.sh /root/run.sh

RUN chmod +x /root/run.sh
RUN apk add sudo
RUN apk add --update apk-cron && rm -rf /var/cache/apk/*
RUN apk add busybox-initscripts
RUN echo "*/1 * * * * /root/run.sh" > /etc/crontabs/root
RUN echo "Hello, world!"

CMD ["crond", "-f"]