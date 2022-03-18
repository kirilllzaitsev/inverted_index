FROM openjdk:8
COPY build/libs/InvertedIndexApp-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-cp", "app.jar", "com.parallel.computing.Main"]
EXPOSE 3333
