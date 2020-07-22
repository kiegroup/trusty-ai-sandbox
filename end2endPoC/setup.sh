#!/bin/bash

metricsComposeDir=$(pwd)

cd /tmp || exit
git clone https://github.com/r00ta/kogito-runtimes.git
cd kogito-runtimes || exit
git checkout trusty-kafka-master
mvn clean install -DskipTests
cd ..
rm -rf kogito-runtimes

cd "$metricsComposeDir" || exit

