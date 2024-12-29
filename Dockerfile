FROM gradle:7.6.3-jdk17 AS build
COPY --chown=gradle:gradle . /buildgradle
WORKDIR /buildgradle
RUN gradle buildFatJar --no-daemon --stacktrace

FROM amazoncorretto:17
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/LonelyBotKt-all.jar /app/LonelyBotApp.jar
ENTRYPOINT ["java", "-jar", "/app/LonelyBotApp.jar"]