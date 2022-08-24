FROM --platform=linux/amd64 amazoncorretto:11-alpine-jdk

COPY target/*.jar app.jar
ENV DB_HOST=DB_HOST \
    DB_PORT=DB_PORT \
    DB_NAME=DB_NAME \
    DB_USER=DB_USER \
    DB_PASS=DB_PASS
ENTRYPOINT java -DDB_HOST=$DB_HOST \
                -DDB_PORT=$DB_PORT \
                -DDB_NAME=$DB_NAME \
                -DDB_USER=$DB_USER \
                -DDB_PASS=$DB_PASS \
                -jar app.jar