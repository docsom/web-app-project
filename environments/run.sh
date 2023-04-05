#!/bin/sh

git clone https://github.com/yongmandooo/23Spring_SoftwareEngineering_Group5.git
cd 23Spring_SoftwareEngineering_Group5/cse364-project
mvn package
java -jar target/cse364-project-0.0.1-SNAPSHOT.jar
