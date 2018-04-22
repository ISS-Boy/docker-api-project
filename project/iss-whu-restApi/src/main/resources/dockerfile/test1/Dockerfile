FROM anapsix/alpine-java:8u151b12_jdk
MAINTAINER just@issboy
EXPOSE 80
ADD ./kstream-app-template-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/
ENTRYPOINT ["java","-jar","-ea","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-Dcom.sun.management.jmxremote.port=80","/opt/kstream-app-template-1.0-SNAPSHOT-jar-with-dependencies.jar"]
