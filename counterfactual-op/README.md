# counterfactual-op

REST server to generate counterfactuals for a credit card approval predict model.

## Usage

Build and start the counterfactual REST server:

```
$ mvn clean package
$ java -jar target/counterfactual-op-1.0.0-SNAPSHOT-runner.jar
```

### Predict

Predict a credit card approval with:

```
curl --request POST \
  --url http://0.0.0.0:8080/creditcard/prediction \
  --header 'content-type: application/json' \
  --data '{
	"age": 18,
	"income": 0,
	"children": 2,
	"daysEmployed": 0,
	"ownRealty": false,
	"workPhone": true,
	"ownCar": false
}'
```

### Counterfactual

Calculate the counterfactual using:

```
curl --request POST \
  --url http://0.0.0.0:8080/creditcard/counterfactual \
  --header 'content-type: application/json' \
  --data '{
	"age": 20,
	"income": 30000,
	"children": 0,
	"daysEmployed": 100,
	"ownRealty": false,
	"workPhone": true,
	"ownCar": false
}'
```

### Score

Explain OptaPlanner's counterfactual score using:

```
curl --request POST \
  --url http://0.0.0.0:8080/creditcard/breakdown \
  --header 'content-type: application/json' \
  --data '{
	"age": 20,
	"income": 30000,
	"children": 0,
	"daysEmployed": 100,
	"ownRealty": false,
	"workPhone": true,
	"ownCar": false
}'
```