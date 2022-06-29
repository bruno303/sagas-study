FROM eclipse-temurin:17-jre-alpine

RUN apk --no-cache add curl

WORKDIR /app

COPY build/libs/order-*SNAPSHOT.jar app.jar
COPY docker/init_local.sh init.sh

EXPOSE 8080
CMD ["sh", "init.sh"]
