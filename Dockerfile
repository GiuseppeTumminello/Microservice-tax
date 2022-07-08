#FROM gradle:7.4-jdk11-alpine AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle bootJar
#
#FROM openjdk:11-jre-slim
#EXPOSE 8090
#COPY --from=build /home/gradle/src/build/libs/*.jar ./app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
#CMD java $JAVA_OPTIONS -jar app.jar


FROM openjdk:11-jre-slim
EXPOSE 8090
COPY  /build/libs/*.jar ./app.jar
CMD ["java","-jar","app.jar"]