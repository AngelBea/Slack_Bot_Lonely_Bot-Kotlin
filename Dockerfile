FROM amazoncorretto
RUN mkdir /app
ENV PORT=8080
ENV SLACK_BOT_TOKEN=xxx
ENV SLACK_BOT_TOKEN_TEST=xxx
ENV SLACK_SIGNING_SECRET =xxx
ENV NOTION_TOKEN=xxx
EXPOSE 8080
COPY ./build/libs/LonelyBotKt-all.jar /app/LonelyBotApp.jar
ENTRYPOINT ["java", "-jar", "/app/LonelyBotApp.jar"]

