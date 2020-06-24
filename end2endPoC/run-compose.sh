cd explanation-service/ || exit
mvn clean package -DskipTests
rc=$?
if [ $rc -ne 0 ] ; then
  echo Could not package explanation service, exit code [$rc]; exit $rc
fi
cd ../

cd kogito-consumer/app || exit
mvn clean package -DskipTests
rc=$?
if [ $rc -ne 0 ] ; then
  echo Could not package trusty service, exit code [$rc]; exit $rc
fi

cd ../../
cd loanEligibilityApp/app || exit
mvn clean package
rc=$?
if [ $rc -ne 0 ] ; then
  echo Could not package kogito application, exit code [$rc]; exit $rc
fi
cp target/resources/dashboards/* ../../grafana/provisioning/dashboards/

cd ../../

chmod 644 prometheus/prometheus.yml

sudo /usr/local/bin/docker-compose build && sudo /usr/local/bin/docker-compose up $1

