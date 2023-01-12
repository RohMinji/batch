FROM ghcr.io/shclub/openjdk:17-alpine AS MAVEN_BUILD

RUN mkdir -p build
WORKDIR /build

COPY pom.xml ./
COPY src ./src                             
COPY mvnw ./         
COPY . ./
#RUN ./mvnw spring-boot:run
#RUN ./mvnw clean package
RUN ./mvnw clean -Dmaven.test.skip=true

#FROM eclipse-temurin:17.0.2_8-jre-alpine
#FROM ghcr.io/shclub/jre17-runtime:v1.0.0

#COPY --from=MAVEN_BUILD /build/target/*.jar app.jar

#ENV TZ Asia/Seoul
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#ENV SPRING_PROFILES_ACTIVE dev

#ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"
#ENV JAVA_OPTS="${JAVA_OPTS} -XX:+UseG1GC -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark -XX:InitiatingHeapOccupancyPercent=35 -XX:G1ConcRefinementThreads=20"

#EXPOSE 80

#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar  app.jar "]
ENTRYPOINT ["sh", "-c", "java -jar  app.jar "]
#ENTRYPOINT ["sh", "test/run.sh"]