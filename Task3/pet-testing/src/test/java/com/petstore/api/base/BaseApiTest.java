package com.petstore.api.base;

import com.petstore.api.client.PetApiClient;
import com.petstore.api.config.ApiConfig;
import io.qameta.allure.Step;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseApiTest {
    protected PetApiClient petApiClient;

    @BeforeSuite
    public void setupSuite() {
    }

    @BeforeMethod
    public void setupTest() {
        petApiClient = new PetApiClient(ApiConfig.getBaseUri());
    }

    @AfterMethod
    public void teardownTest() {
    }

    @Step("Verify response status code is {expectedStatusCode}")
    protected void verifyStatusCode(int actualStatusCode, int expectedStatusCode) {
        assert actualStatusCode == expectedStatusCode :
                String.format("Expected status code %d but got %d", expectedStatusCode, actualStatusCode);
    }
} 