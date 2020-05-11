# LocalApi

All URIs are relative to *https://kie.org/m2x*

Method | HTTP request | Description
------------- | ------------- | -------------
[**predict**](LocalApi.md#predict) | **POST** /model/predict | Execute model prediction function


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
//import io.swagger.client.api.LocalApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: trusty_auth
OAuth trusty_auth = (OAuth) defaultClient.getAuthentication("trusty_auth");
trusty_auth.setAccessToken("YOUR ACCESS TOKEN");

LocalApi apiInstance = new LocalApi();
List<PredictionInput> body = Arrays.asList(new PredictionInput()); // List<PredictionInput> | Prediction inputs
try {
    List<PredictionOutput> result = apiInstance.predict(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling LocalApi#predict");
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

