FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openssh-server
RUN apt-get install -y vim git openjdk-17-jdk maven
RUN apt-get install -y sudo
RUN apt-get install -y systemctl

WORKDIR /root/project

ADD run.sh /root/project/

CMD ["/bin/bash"]