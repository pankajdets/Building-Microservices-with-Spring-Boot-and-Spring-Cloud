FROM eclipse-temurin:17

#Meta Deta
LABEL mentainer = "pankajdets@gmail.com"

#whenever we run the container. app directory wii be created in the container
WORKDIR /app

#copy this jar file to app folder in the container and rename the jar filr to springboot-docker-demo.jar
COPY target/jarfilename.jar  /app/springboot-docker-demo.jar

#entrypoint to run the jar file 
ENTRYPOINT [ "java", "-jar", "springboot-docker-demo.jar"]