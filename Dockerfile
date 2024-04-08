FROM openjdk:21
VOLUME /tmp
EXPOSE 8085
ADD ./target/apirest-0.0.1-SNAPSHOT.jar restaurant.jar
ENTRYPOINT ["java", "-jar", "restaurant.jar"]