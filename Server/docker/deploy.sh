#!/bin/bash
cd ..
./gradlew build
cd docker
rm aggregator-1.0-SNAPSHOT.war
cp ../build/libs/aggregator-1.0-SNAPSHOT.war .
docker build -t aggregator .
MONGO_RUNNING=$$(docker ps -q --filter="name=mongo")
if [ -z "$MONGO_RUNNING" ];
then
    docker run -v "$(pwd)":/data --name mongo -d mongo mongod --smallfiles
fi
docker run -p 8080:8080 --link mongo:mongo --privileged=true -d aggregator
