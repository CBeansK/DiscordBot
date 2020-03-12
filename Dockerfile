FROM openjdk:11

EXPOSE 8080

RUN mkdir /app

COPY build/libs/DiscordBot.jar /app/DiscordBot.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app/DiscordBot.jar"]