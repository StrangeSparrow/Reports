FROM bellsoft/liberica-openjdk-alpine:latest
ADD build/libs/invoice-core-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["sh", "-c", "java --enable-preview -jar /app.jar"]