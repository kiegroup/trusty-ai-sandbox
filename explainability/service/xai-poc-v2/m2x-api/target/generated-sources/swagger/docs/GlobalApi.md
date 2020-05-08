# GlobalApi

All URIs are relative to *https://kie.org/m2x*

Method | HTTP request | Description
------------- | ------------- | -------------
[**dataDistribution**](GlobalApi.md#dataDistribution) | **GET** /model/data/distribution | Get the distribution of the data used to train the model
[**info**](GlobalApi.md#info) | **GET** /model/info | Get general information about the model
[**predict**](GlobalApi.md#predict) | **POST** /model/predict | Execute model prediction function


<a name="dataDistribution"></a>
# **dataDistribution**
> DataDistribution dataDistribution()

Get the distribution of the data used to train the model



### Example
```java
// Import classes:
//import org.kie.trusty.xai.handler.ApiClient;
//import org.kie.trusty.xai.handler.ApiException;
//import org.kie.trusty.xai.handler.Configuration;
//import org.kie.trusty.xai.handler.auth.*;
//import io.swagger.client.api.GlobalApi;

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
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**DataDistribution**](DataDistribution.md)

### Authorization

[trusty_auth](../README.md#trusty_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="info"></a>
# **info**
> ModelInfo info()

Get general information about the model



### Example
```java
// Import classes:
//import org.kie.trusty.xai.handler.ApiClient;
//import org.kie.trusty.xai.handler.ApiException;
//import org.kie.trusty.xai.handler.Configuration;
//import org.kie.trusty.xai.handler.auth.*;
//import io.swagger.client.api.GlobalApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: trusty_auth
OAuth trusty_auth = (OAuth) defaultClient.getAuthentication("trusty_auth");
trusty_auth.setAccessToken("YOUR ACCESS TOKEN");

GlobalApi apiInstance = new GlobalApi();
try {
    ModelInfo result = apiInstance.info();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GlobalApi#info");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ModelInfo**](ModelInfo.md)

### Authorization

[trusty_auth](../README.md#trusty_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="predict"></a>
# **predict**
> List&lt;PredictionOutput&gt; predict(body)

Execute model prediction function



### Example
```java
// Import classes:
//import org.kie.trusty.xai.handler.ApiClient;
//import org.kie.trusty.xai.handler.ApiException;
//import org.kie.trusty.xai.handler.Configuration;
//import org.kie.trusty.xai.handler.auth.*;
//import io.swagger.client.api.GlobalApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: trusty_auth
OAuth trusty_auth = (OAuth) defaultClient.getAuthentication("trusty_auth");
trusty_auth.setAccessToken("YOUR ACCESS TOKEN");

GlobalApi apiInstance = new GlobalApi();
List<PredictionInput> body = Arrays.asList(new PredictionInput()); // List<PredictionInput> | Prediction inputs
try {
    List<PredictionOutput> result = apiInstance.predict(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GlobalApi#predict");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;PredictionInput&gt;**](PredictionInput.md)| Prediction inputs |

### Return type

[**List&lt;PredictionOutput&gt;**](PredictionOutput.md)

### Authorization

[trusty_auth](../README.md#trusty_auth)

### HTTP request headers

 - **Content-Type**: application/json, application/xml
 - **Accept**: application/xml, application/json

