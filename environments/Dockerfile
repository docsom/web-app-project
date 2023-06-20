FROM ubuntu:22.04

ENV HOME_DIR /root

RUN apt-get update && apt-get install -y openssh-server
RUN apt-get install -y vim git openjdk-17-jdk maven
RUN apt-get install -y sudo
RUN apt-get install -y systemctl
RUN apt-get install -y curl



RUN cd ${HOME_DIR} && wget http://apache.mirror.cdnetworks.com/tomcat/tomcat-9/v9.0.76/bin/apache-tomcat-9.0.76.tar.gz
RUN cd ${HOME_DIR} && tar -xzf apache-tomcat-9.0.76.tar.gz
RUN cd ${HOME_DIR} && rm apache-tomcat-9.0.76.tar.gz && mv apache-tomcat-9.0.76 tomcat
# 작업 디렉토리 설정
WORKDIR /root/tomcat


ADD ./cse364-project-0.0.1-SNAPSHOT.war  ./webapps
## 루트 지우기
RUN rm -rf ./webapps/ROOT
## 루트 만들기
RUN mv ./webapps/cse364-project-0.0.1-SNAPSHOT.war ./webapps/ROOT.war

# 포트 열기
EXPOSE 8080

# 톰캣 실행
WORKDIR /root/project


ADD run.sh /root/project/




CMD [ "/root/tomcat/bin/catalina.sh", "run", "/bin/bash"]

