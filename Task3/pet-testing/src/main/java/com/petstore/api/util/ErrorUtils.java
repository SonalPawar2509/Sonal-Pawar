package com.petstore.api.util;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling API error responses.
 */
public class ErrorUtils {
    private static final Logger logger = LoggerFactory.getLogger(ErrorUtils.class);
    
    private static final Map<Integer, String> ERROR_MESSAGES = new HashMap<>();
    
    static {
        ERROR_MESSAGES.put(400, "Bad Request - The request was invalid or cannot be served");
        ERROR_MESSAGES.put(401, "Unauthorized - Authentication credentials are required");
        ERROR_MESSAGES.put(403, "Forbidden - The server understood the request but refuses to authorize it");
        ERROR_MESSAGES.put(404, "Not Found - The requested resource could not be found");
        ERROR_MESSAGES.put(405, "Method Not Allowed - The method specified is not allowed for the resource");
        ERROR_MESSAGES.put(415, "Unsupported Media Type - The media format is not supported");
        ERROR_MESSAGES.put(429, "Too Many Requests - Rate limit exceeded");
        ERROR_MESSAGES.put(500, "Internal Server Error - Something went wrong on the server");
        ERROR_MESSAGES.put(503, "Service Unavailable - The server is temporarily unable to handle the request");
    }
    
    /**
     * Get a descriptive error message based on the response status code.
     * 
     * @param response The REST Assured response
     * @return A formatted error message
     */
    public static String getErrorMessage(Response response) {
        int statusCode = response.getStatusCode();
        String errorDescription = ERROR_MESSAGES.getOrDefault(statusCode, "Unknown error");
        String responseBody = response.getBody().asString();
        
        return String.format("API Error - Status Code: %d (%s), Response: %s", 
                             statusCode, errorDescription, responseBody);
    }
    
    /**
     * Logs an error message based on the response and returns the response.
     * Useful for logging errors in a fluent API style.
     * 
     * @param response The REST Assured response
     * @return The same response for method chaining
     */
    public static Response logError(Response response) {
        if (response.getStatusCode() >= 400) {
        }
        return response;
    }
    
    /**
     * Determines if a response represents an error.
     * 
     * @param response The REST Assured response
     * @return true if the response has an error status code (400+)
     */
    public static boolean isError(Response response) {
        return response.getStatusCode() >= 400;
    }
    
    /**
     * Extracts error details from a response, if available.
     * 
     * @param response The REST Assured response
     * @return A map containing error details or an empty map if none found
     */
    public static Map<String, Object> extractErrorDetails(Response response) {
        Map<String, Object> errorDetails = new HashMap<>();
        
        try {
            if (response.getContentType().contains("application/json")) {
                // Try to extract standard error fields
                if (response.jsonPath().get("message") != null) {
                    errorDetails.put("message", response.jsonPath().get("message"));
                }
                
                if (response.jsonPath().get("code") != null) {
                    errorDetails.put("code", response.jsonPath().get("code"));
                }
                
                if (response.jsonPath().get("type") != null) {
                    errorDetails.put("type", response.jsonPath().get("type"));
                }
            }
        } catch (Exception e) {
        }
        
        // Always include status code
        errorDetails.put("statusCode", response.getStatusCode());
        errorDetails.put("statusLine", response.getStatusLine());
        
        return errorDetails;
    }
} 