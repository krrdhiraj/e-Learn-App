#base image from dockerhub
FROM openjdk:21-slim-buster

#Maintainer
MAINTAINER dhiraj@gmail.com

#coping the jar file to the images
COPY target/notification-service-0.0.1-SNAPSHOT.jar notification-service-0.0.1-SNAPSHOT.jar

# to run the application
ENTRYPOINT ["java", "-jar", "notification-service-0.0.1-SNAPSHOT.jar"]
