FROM openjdk:11-jdk-slim
ENV TZ='Asia/Shanghai'
EXPOSE 8081
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.github.junbaor.platelet.PlateletApplication"]