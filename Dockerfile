FROM openjdk:8-jdk-alpine AS build
WORKDIR /app
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar


FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /app

COPY wait-for-it.sh .
COPY --from=build /app/build/libs/*.jar app.jar

CMD ./wait-for-it.sh projektdb:5432 --timeout=0 -- java -jar app.jar





