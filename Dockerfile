FROM openjdk:8

COPY build/libs/chat-fat-*.jar /opt/chat/app.jar

ENTRYPOINT java -jar /opt/chat/app.jar