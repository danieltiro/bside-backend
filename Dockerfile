ARG APP_NAME=demo

FROM openjdk:21-jdk as builder
ARG APP_NAME

WORKDIR /app

COPY ./pom.xml /app/pom.xml
COPY ./.mvn /app/.mvn
COPY ./mvnw /app/mvnw
COPY ./src /app/src

RUN chmod +x /app/mvnw
RUN pwd
RUN ls -lah
RUN /app/mvnw -Dmaven.test.skip=true -DskipTests --threads 12 clean package

FROM openjdk:21-jdk
ARG APP_NAME
WORKDIR /app
RUN mkdir ./logs

COPY --from=builder /app/target/$APP_NAME-0.0.1-SNAPSHOT.jar .
ARG APP_NAME
RUN pwd
RUN ls -lah
ARG JAR_FILE=/$APP_NAME-0.0.1-SNAPSHOT.jar 
RUN echo ${JAR_FILE}
RUN pwd
RUN ls
ENV JAVA_OPTS=""
ENV APP_JAR=$APP_NAME-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "sh", "-c", "java -jar ./$APP_JAR" ]