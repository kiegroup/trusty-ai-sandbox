# trusty-PoC
PoC DMN Event visualizer

## Requirements
1) java 11+ installed 
2) Maven 3.6.0+ installed
3) `docker` and `docker-compose` properly set up
4) Linux OS if you want to run the scripts `setup.sh` and `run-compose.sh` that will deploy all the services. 
5) *The script `run-compose.sh` requires `sudo` permission, but it depends on your settings.. If you don't need, remove `sudo` from the scripts.*

## Install and run
Install the patched kogito-runtimes with 
```bash
./setup.sh
```

Build and run the containers with 
```bash
./run-compose.sh
```
This script will build the jar artifacts on your local host and then will run `docker-compose`. 

## Architecture overview
The architecture is more or less like this 
![Untitled Diagram](https://user-images.githubusercontent.com/18282531/76609097-189fcb00-6517-11ea-8395-f59bdb5da146.png)

1) A producer will generate events (DMN Model register, DMN decisions) and send them to kafka (implementation copied from https://github.com/kostola/kogito-examples/tree/dmn-quarkus-listener-example/dmn-quarkus-listener-example)
2) A consumer (trusty service) will get the decisions, store them with elastic search, create grafana dashboards and make them available.

If you run `run-compose.sh` script, there will be many containers allocated: 
1) the producer
2) the consumer (alias trusty service)
3) grafana instance
4) prometheus instance
6) elastic search instance
7) kibana instance 
8) zookeeper instance
9) kafdrop instance

## Make simple requests to evaluate the DMN models
In the folder `sample-requests` there are 2 sample scripts that run sample requests to `localhost:8080` (to the producer). 

## Make random requests automatically
If you want to generate some random requests to the producer, you can run 
```bash
python3 data-generator/generator.py
```
It will generate 10 random requests for the loan-eligibility DMN.

## Visualize the decisions
After that you make some requests, you can navigate to 
1) `localhost:3000` for the grafana dashboard automatically generated at run time (depending on the rules that have been fired). 
![Screenshot from 2020-02-10 12-00-06](https://user-images.githubusercontent.com/18282531/74144370-e68c0800-4bfc-11ea-9217-8c98f305bc2f.png)
2) `localhost:8180/executions.html` to check out a debug UX (it will show all the events stored on elastic search and if you click on one item, that item will be fetched from the database and displayed).
`localhost:8180/swagger-ui/` to have a look at the API. 
![Screenshot from 2020-02-10 11-59-24](https://user-images.githubusercontent.com/18282531/74144316-cf4d1a80-4bfc-11ea-9228-26656667f464.png)
3) `localhost:5601` for the kibana dashboard (here an example of dashboard on kibana)
![Screenshot from 2020-02-10 11-57-42](https://user-images.githubusercontent.com/18282531/74144245-ac226b00-4bfc-11ea-9dc9-16de809b0e02.png)

