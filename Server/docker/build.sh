#!/bin/bash
cd ..
./gradlew build
cd docker
docker build -t aggregator .
docker run -v "$(pwd)":/data --name mongo -d mongo mongod --smallfiles
docker run -p 8888:8888 -p 8080:8080 -it --link mongo:mongo aggregator
