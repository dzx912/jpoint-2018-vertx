FROM openjdk:8

COPY build/libs/chat-fat-*.jar /opt/chat/main-app.jar

ENTRYPOINT java -jar /opt/chat/main-app.jar