package com.petstore.api.util;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom filter for logging REST Assured request and response details.
 */
public class RequestResponseLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                          FilterableResponseSpecification responseSpec, 
                          FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        return response;
    }
    
    private String formatHeaders(FilterableRequestSpecification requestSpec) {
        StringBuilder sb = new StringBuilder();
        requestSpec.getHeaders().forEach(header -> 
            sb.append(header.getName()).append(": ").append(header.getValue()).append(", "));
        return sb.toString();
    }
    
    private String formatResponseHeaders(Response response) {
        StringBuilder sb = new StringBuilder();
        response.getHeaders().forEach(header -> 
            sb.append(header.getName()).append(": ").append(header.getValue()).append(", "));
        return sb.toString();
    }
    
    private String formatBody(FilterableRequestSpecification requestSpec) {
        Object body = requestSpec.getBody();
        if (body == null) {
            return "null";
        }
        return body.toString();
    }
} 