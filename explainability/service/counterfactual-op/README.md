# counterfactual-op project

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

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `counterfactual-rest-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/counterfactual-rest-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/counterfactual-rest-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.