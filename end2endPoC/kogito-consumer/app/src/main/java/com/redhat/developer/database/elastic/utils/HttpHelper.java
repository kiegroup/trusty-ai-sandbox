package com.redhat.developer.database.elastic.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelper {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private ObjectMapper mapper = new ObjectMapper();

    private String baseHost;

    public HttpHelper(String baseHost) {
        this.baseHost = baseHost;
    }

    public String doGet(String path) {
        HttpGet request = new HttpGet(baseHost + path);
        HttpResponse response = null;
        try {
            response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                LOGGER.debug("Get request returned " + result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doPost(String path, String params) {

        HttpPost post = new HttpPost(baseHost + path);
        LOGGER.debug("Going to post to: " + path + "\n with: " + params);
        try {
            post.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpclient.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            LOGGER.debug("I've got " + result);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
