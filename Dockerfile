FROM openjdk:18

COPY target/scala-2.13/teacherauditing-*.jar teacherauditing.jar

ENTRYPOINT java ${JAVA_OPTS} -jar teacherauditing.jar
