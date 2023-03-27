FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openssh-server
RUN apt-get -y install vim git openjdk-17-jdk maven

WORKDIR /root/project

ADD run.sh /root/project/run.sh
CMD ["/bin/bash"]