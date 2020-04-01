curl -X POST \
  http://localhost:8080/dmn-bikes \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 2a22b2f2-3fee-4a11-811b-97bd8794818b' \
  -H 'cache-control: no-cache' \
  -d '{
	"Person": { "age" : 18}, "Bike" : {"engineSize": 100, "power": 100}
}'
