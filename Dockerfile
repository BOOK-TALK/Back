# linux 기반 jdk 이미지
FROM openjdk:17-slim
RUN apt-get update && apt-get install -y iputils-ping # ping
ADD ./build/libs/backend-0.0.1-SNAPSHOT.jar spring-app.jar
ENTRYPOINT ["java", "-jar", "/spring-app.jar"]
