/*
 * Model to Explainability communication
 * Model to Explainability communication protocol
 *
 * OpenAPI spec version: 1.0.0
 * Contact: tteofili@redhat.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.kie.trusty.m2x.handler.auth;

import java.util.List;
import java.util.Map;

import com.squareup.okhttp.Credentials;
import org.kie.trusty.m2x.handler.Pair;

public class HttpBasicAuth implements Authentication {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
        if (username == null && password == null) {
            return;
        }
        headerParams.put("Authorization", Credentials.basic(
            username == null ? "" : username,
            password == null ? "" : password));
    }
}
