cd kogito-consumer/app
mvn clean package -DskipTests

cd ../../
cd loanEligibilityApp/app
mvn clean package
cp target/resources/dashboards/* ../../grafana/provisioning/dashboards/

cd ../../
sudo docker-compose build && sudo docker-compose up

