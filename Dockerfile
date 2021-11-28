FROM openjdk:8-jdk-alpine
COPY management-tool.jar management-tool.jar
COPY employees_db.db employees_db.db
ENTRYPOINT ["java","-jar","/management-tool.jar"]