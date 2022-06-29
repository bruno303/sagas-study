#!/bin/bash

set -e

cd order
./gradlew bootJar
cd ..

docker-compose up -d
