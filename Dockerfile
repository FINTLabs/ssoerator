FROM gradle:7.5-jdk17 as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM gcr.io/distroless/java17-debian13:latest@sha256:cf7080f552165a1b5586349fcce84a62aa1ae9ac1a6811f9c515accbe9dcc125
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/*.jar /data/app.jar
CMD ["/data/app.jar"]
