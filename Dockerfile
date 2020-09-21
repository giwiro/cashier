FROM openjdk:8-jdk-alpine as cashier-builder
ARG WORKDIR=/usr/local/src/cashier
RUN mkdir -p ${WORKDIR}
COPY . ${WORKDIR}
WORKDIR ${WORKDIR}
RUN ./gradlew build -x test

FROM openjdk:8-jdk-alpine
RUN apk --update add iputils curl #postgresql-client redis
RUN addgroup -S cashier && adduser -S cashier -G cashier
USER cashier:cashier
ARG WORKDIR=/home/cashier
WORKDIR ${WORKDIR}
COPY --from=cashier-builder /usr/local/src/cashier/build/libs/*.jar ${WORKDIR}/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
# ENTRYPOINT tail -f /dev/null
