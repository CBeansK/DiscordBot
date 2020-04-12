FROM openjdk:11

EXPOSE 8080

RUN mkdir /app

WORKDIR /app

ENV TOKEN=Njc5OTIxNDY1OTY5MzQ0NTEz.Xmwk9g.nUksjNS8XuF-79Leevyrf8qjOMU
ENV PREFIX=!!
ENV OWNER_ID=88356968187568128
ENV CHANNEL_ID=612562146999664653
ENV VOLUME=10

ADD build/libs/DiscordBot.jar DiscordBot.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "DiscordBot.jar"]