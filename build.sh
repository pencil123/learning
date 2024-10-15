#!/bin/bash
git pull
mvn package
docker build -t learning:v1 .
docker stop learning
docker rm learning
docker run --name learning  -d -p 8000:8000 learning:v1
