FROM alpine:latest
RUN apk add --no-cache libc6-compat
COPY application/target/application /application
EXPOSE 8080
ENTRYPOINT ["/application"]
