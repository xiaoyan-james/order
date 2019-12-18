FROM hub.c.163.com/library/java:8-alpine

ADD server/target/*.jar order.jar

#设置时区
ENV TZ Asia/Shanghai

#EXPOSE 8080

ENTRYPOINT ["java","-jar","/order.jar"]
