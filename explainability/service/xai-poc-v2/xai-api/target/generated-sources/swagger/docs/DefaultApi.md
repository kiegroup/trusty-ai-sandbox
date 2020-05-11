# DefaultApi

All URIs are relative to *https://kie.org/xai*

Method | HTTP request | Description
------------- | ------------- | -------------
[**head**](DefaultApi.md#head) | **HEAD** /info | 


<a name="head"></a>
# **head**
> Prediction head()



### Example
```java
// Import classes:
//import org.kie.trusty.xai.handler.ApiException;
//import io.swagger.client.api.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
try {
    Prediction result = apiInstance.head();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#head");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Prediction**](Prediction.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

