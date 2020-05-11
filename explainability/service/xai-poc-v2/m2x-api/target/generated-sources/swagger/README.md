# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import org.kie.trusty.xai.handler.*;
import org.kie.trusty.xai.handler.auth.*;
import org.kie.trusty.xai.model.*;
import io.swagger.client.api.GlobalApi;

import java.io.File;
import java.util.*;

public class GlobalApiExample {

    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        
        // Configure OAuth2 access token for authorization: trusty_auth
        OAuth trusty_auth = (OAuth) defaultClient.getAuthentication("trusty_auth");
        trusty_auth.setAccessToken("YOUR ACCESS TOKEN");

        GlobalApi apiInstance = new GlobalApi();
        try {
            DataDistribution result = apiInstance.dataDistribution();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling GlobalApi#dataDistribution");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://kie.org/m2x*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*GlobalApi* | [**dataDistribution**](docs/GlobalApi.md#dataDistribution) | **GET** /model/data/distribution | Get the distribution of the data used to train the model
*GlobalApi* | [**info**](docs/GlobalApi.md#info) | **GET** /model/info | Get general information about the model
*GlobalApi* | [**predict**](docs/GlobalApi.md#predict) | **POST** /model/predict | Execute model prediction function
*LocalApi* | [**predict**](docs/LocalApi.md#predict) | **POST** /model/predict | Execute model prediction function


## Documentation for Models

 - [DataDistribution](docs/DataDistribution.md)
 - [Feature](docs/Feature.md)
 - [FeatureDistribution](docs/FeatureDistribution.md)
 - [ModelInfo](docs/ModelInfo.md)
 - [Output](docs/Output.md)
 - [PredictionInput](docs/PredictionInput.md)
 - [PredictionOutput](docs/PredictionOutput.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### api_key

- **Type**: API key
- **API key parameter name**: api_key
- **Location**: HTTP header

### trusty_auth

- **Type**: OAuth
- **Flow**: implicit
- **Authorization URL**: http://kie.org/oauth/dialog
- **Scopes**: 
  - write:pred: execute model predictions
  - read:info: read info about the model


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

tteofili@redhat.com

