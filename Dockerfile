FROM hub.c.163.com/library/java:8-alpine

ADD server/target/*.jar order.jar

#EXPOSE 8080

ENTRYPOINT ["java","-jar","/order.jar"]
