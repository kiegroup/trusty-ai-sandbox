## Trusty AI PoC

This project is a PoC for the Audit section of a XAI service

### Build local dependencies

First build the local dependencies for this project.
- Run `yarn clean:build` in the `./front-end-poc-pmml-linear-model-viewer` folder

### Install dependencies

To install dependencies you need to have npm (or yarn) installed globally and run in the terminal:
```
npm install
```
### Run the project

Compile and run the project in development mode with:
```$xslt
npm run start
```

Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

The project connects to APIs at `localhost:4000`. While the APIs are still not ready, you can run the following command in a different terminal tab to run a mocked api server:
```
npm run mock-api
```

### Build the project
```$xslt
npm run build
```
Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.


### Notes

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).