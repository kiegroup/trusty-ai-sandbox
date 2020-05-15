cd explanation-service/ || exit
mvn clean package -DskipTests
cd ../

cd kogito-consumer/app || exit
mvn clean package -DskipTests

cd ../../
cd loanEligibilityApp/app || exit
mvn clean package
cp target/resources/dashboards/* ../../grafana/provisioning/dashboards/

cd ../../

chmod 644 prometheus/prometheus.yml

sudo docker-compose build && sudo docker-compose up $1

