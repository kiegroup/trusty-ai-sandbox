#!/bin/bash

metricsComposeDir=$(pwd)

cd /tmp
git clone https://github.com/r00ta/kogito-runtimes.git
cd kogito-runtimes
git checkout trusty-kafka-feature
mvn clean install -DskipTests


cd $metricsComposeDir

