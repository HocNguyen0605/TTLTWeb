FROM tomcat:11-jdk21
RUN rm -rf /usr/local/tomcat/webapps/*
COPY build/libs/ROOT.war /usr/local/tomcat/webapps/ROOT.war