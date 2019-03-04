## see https://spring.io/guides/gs/spring-boot-docker/
FROM java:7
VOLUME /tmp
ADD target/visualCaptcha-java-rest-0.1.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]