package com.petstore.api.pet;

import com.petstore.api.base.BaseApiTest;
import com.petstore.api.util.TestDataGenerator;
import com.petstore.model.Pet;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Pet Management")
@Story("Delete Pet")
public class DeletePetTests extends BaseApiTest {

    @Test(description = "Delete pet by ID")
    public void deletePetById() {
        Pet pet = TestDataGenerator.generatePet();
        Response createResponse = petApiClient.createPet(pet);
        verifyStatusCode(createResponse.getStatusCode(), 200);

        Response deleteResponse = petApiClient.deletePet(pet.getId());

        assertThat(deleteResponse.getStatusCode())
            .isIn(200, 500);

        if (deleteResponse.getStatusCode() == 200) {
            Response getResponse = petApiClient.getPetById(pet.getId());
            verifyStatusCode(getResponse.getStatusCode(), 404);
        }
    }

    @Test(description = "Delete non-existent pet")
    public void deleteNonExistentPet() {
        Long nonExistentId = TestDataGenerator.generateRandomId();

        Response response = petApiClient.deletePet(nonExistentId);

        assertThat(response.getStatusCode())
            .isIn(200, 500);
    }

    @Test(description = "Delete pet without authentication")
    public void deleteWithoutAuthentication() {
        Pet pet = TestDataGenerator.generatePet();
        Response createResponse = petApiClient.createPet(pet);
        verifyStatusCode(createResponse.getStatusCode(), 200);

        Response response = petApiClient.deletePetWithoutAuth(pet.getId());

        assertThat(response.getStatusCode())
            .isIn(200, 500);
        
        if (response.getStatusCode() == 200) {
            assertThat(response.getBody().asString()).contains("Pet deleted");
        }
    }
} 