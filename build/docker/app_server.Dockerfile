FROM openjdk:8
LABEL maintainer=cyberpank317@gmail.com
WORKDIR /app
COPY libs libs/
COPY classes classes/
ENTRYPOINT ["java", "-cp", "/app/resources:/app/classes:/app/libs/*", "com.parallel.computing.Main"]
EXPOSE 3333