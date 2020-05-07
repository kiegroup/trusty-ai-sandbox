To launch the container locally on port _8080_ with a dummy model deployed simply run 

~~~
mvn exec:java
~~~

to obtain information about the model you can cURL an HTTP GET as follows:

~~~
 curl http://localhost:8080/dummy/info
~~~

you will get something like :

~~~
{
  "id":"f0dd7cf5-cabd-4938-bf93-22b6e5706e15",
  "inputShape":{
    "features":[{
      "name":"f1",
      "type":"NUMBER"
      ,"value":{}}]},
  "name":"dummy model",
  "outputShape":{
    "outputs":[{
      "score":1.0,
      "type":"NUMBER"
      ,"value":{}}]},
  "taskType":"REGRESSION",
  "trainingDataDistribution":{
     "featureDistributions":[{
       "max":10.0,
       "mean":5.0,
       "min":0.0,
       "stdDev":1.0}]},
  "version":"0.1"
}
~~~

to perform predictions you can cURL an HTTP POST as follows:

~~~
curl -H 'Content-Type: appd @data.json -H 'Content-Type: application/json' http://localhost:8080/dummy/predict
~~~

you will obtain a prediction result similar to the one below:

~~~
[{
  "outputs":[{
    "score":1.0,
    "type":"NUMBER",
    "value":{
      "underlyingObject":-4.516655566126994
    }
  }]
}]
~~~
