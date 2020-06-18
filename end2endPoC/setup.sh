#!/bin/bash

metricsComposeDir=$(pwd)

cd /tmp || exit
git clone https://github.com/r00ta/kogito-runtimes.git
cd kogito-runtimes || exit
git checkout trusty-kafka-feature
mvn clean install -DskipTests
cd ..
rm -rf kogito-runtimes
git clone git@github.com:kiegroup/drools.git
cd drools/kie-pmml-new || exit
mvn clean install -DskipTests
cd  ../..
rm -rf drools


cd "$metricsComposeDir" || exit

