package com.petstore.api.client;

import com.petstore.api.util.RequestResponseLoggingFilter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ApiClientBase {
    protected static final Logger logger = LoggerFactory.getLogger(ApiClientBase.class);
    protected RequestSpecification requestSpec;
    private final String baseUri;
    private final RequestResponseLoggingFilter loggingFilter;

    public ApiClientBase(String baseUri) {
        this.baseUri = baseUri;
        this.loggingFilter = new RequestResponseLoggingFilter();
        this.requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .build();
    }

    protected String getBaseUri() {
        return baseUri;
    }

    protected RequestResponseLoggingFilter getLoggingFilter() {
        return loggingFilter;
    }

    protected Response get(String path) {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .filter(loggingFilter)
                .when()
                .get(path);
        return response;
    }

    protected Response get(String path, Map<String, Object> pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec).filter(loggingFilter);
        pathParams.forEach(request::pathParam);
        return request.when().get(path);
    }

    protected Response post(String path, Object body) {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .filter(loggingFilter)
                .body(body)
                .when()
                .post(path);
        return response;
    }

    protected Response post(String path, Object body, Map<String, Object> pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec).filter(loggingFilter);
        pathParams.forEach(request::pathParam);
        return request.body(body).when().post(path);
    }

    protected Response postFormData(String path, Map<String, String> formParams, Map<String, Object> pathParams) {
        RequestSpecification request = RestAssured.given()
                .baseUri(baseUri)
                .filter(loggingFilter)
                .contentType("application/x-www-form-urlencoded");
        
        if (pathParams != null) {
            pathParams.forEach(request::pathParam);
        }
        
        formParams.forEach(request::formParam);
        
        return request.when().post(path);
    }

    protected Response postMultipartData(String path, Map<String, Object> multipartData, Map<String, Object> pathParams) {
        RequestSpecification request = RestAssured.given()
                .baseUri(baseUri)
                .filter(loggingFilter);
        
        if (pathParams != null) {
            pathParams.forEach(request::pathParam);
        }
        
        multipartData.forEach((key, value) -> {
            if (value == null) {
                return;
            } else if (value instanceof File) {
                File file = (File) value;
                if (file.exists() && file.canRead()) {
                    try {
                        request.multiPart(key, file);
                    } catch (Exception e) {
                        request.multiPart(key, "Error loading file: " + file.getName());
                    }
                } else {
                    request.multiPart(key, "FILE_NOT_AVAILABLE");
                }
            } else if (value instanceof String) {
                String stringValue = (String) value;
                request.multiPart(key, stringValue);
            } else {
                request.multiPart(key, String.valueOf(value));
            }
        });
        
        return request.when().post(path);
    }

    protected Response put(String path, Object body) {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .filter(loggingFilter)
                .body(body)
                .when()
                .put(path);
        return response;
    }

    protected Response put(String path, Object body, Map<String, Object> pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec).filter(loggingFilter);
        pathParams.forEach(request::pathParam);
        return request.body(body).when().put(path);
    }

    protected Response delete(String path) {
        Response response = RestAssured.given()
                .spec(requestSpec)
                .filter(loggingFilter)
                .when()
                .delete(path);
        return response;
    }

    protected Response delete(String path, Map<String, Object> pathParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpec).filter(loggingFilter);
        pathParams.forEach(request::pathParam);
        return request.when().delete(path);
    }
} 