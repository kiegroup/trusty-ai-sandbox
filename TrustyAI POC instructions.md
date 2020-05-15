# TrustyAI POC README

## Requirements
- `git`
- `mvn`
- Java 11
- `docker`
- `docker-compose`
- MongoDB for persistence (see [installation guide](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-red-hat/) if needed)
- `npm`

## Installation Steps
### Common Steps
- Clone `https://github.com/kiegroup/trusty-ai-sandbox`
- Checkout branch `FAI-133-demo`

### Backend
- Open `end2endPoC` folder
- Execute `./setup.sh` script (it clones and install a patched version of `kogito-runtimes`)
- Execute `./run-compose.sh` and wait startup of all images (it could be needed to remove `sudo` from last two `docker-compose` commands depending on your `docker-compose` local installation)
- In another Terminal (still `end2endPoC` folder) execute `mongo admin -u root -p password --host localhost:27017 < ./pipelines/add-mongo-user.js` to create user in MongoDB
- Open `demo-payloads` folder
- Execute `./create-model.sh ./data/create-risk-model-request.json` (to register risk model)
- Execute `./create-model.sh ./data/create-fraud-model-request.json` (to register fraud model)
- Press `CTRL+C` to stop `run-compose.sh`

### Frontend
- Open `front-end-poc`
- Execute `npm install`

### Execute the Demo
- Open `end2endPoC` folder
- Execute `./run-compose.sh`
- From another terminal, go to `front-end-poc` folder
- Execute `npm run start`
- Press `CTRL+C` in both terminal to shutdown the demo

### Service URLs
| Service name        | URL           |
| ------------- |:-------------:|
| KogitoApp      | [localhost:1337/swagger-ui](http://localhost:1337/swagger-ui) |
| Audit UI      | [localhost:3000](http://localhost:3000) | 
| Grafana      | [localhost:3001](http://localhost:3001) | 
| Kafdrop      | [localhost:9000](http://localhost:9000) | 

### Sample Data
- Open `end2endPoC/sample-requests`
- Execute `./query-dmn-risk.sh` to evaluate risk model
- Execute `./query-dmn-fraud.sh` to evaluate fraud model

### Dashboards
- `myMortgage endpoint metrics` is the dashboard for risk model
- `fraud-scoring endpoint metrics` is dashboard for fraud model

## Additional notes
### Daily activities
- Run `setup.sh` once every day because it is a snapshot version of Kogito that lasts one day
- Optional: Execute again `npm install` to refresh Frontend code

### Clean MongoDB data
- `mongo admin -u root -p password --host localhost:27017`
- `use myMongoDb`
- `db.dmnmodeldata.drop()` -> model data
- `db.dmneventdata.drop()` -> tracing events
- `db.localexplanation.drop()` -> explaination data
