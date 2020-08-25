## Trusty AI PoC

This project is a PoC for the Audit section of a XAI service

### Install dependencies

To install dependencies you need to have yarn installed globally and run in the terminal:

```
yarn install
```

### Run the project

Compile and run the project in development mode with:

```
yarn run start
```

Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br /> You will also see any lint errors in the console.

The project connects to APIs at `localhost:1336`. While the APIs are still not ready, you can run the following command in a different terminal tab to run a mocked api server:

```
yarn run mock-api
```

### Build the project

```
yarn run build
```

Builds the app for production to the `build` folder.<br /> It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br /> Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### Notes

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).
