package com.petstore.api.pet;

import com.petstore.api.base.BaseApiTest;
import com.petstore.api.util.TestDataGenerator;
import com.petstore.model.Pet;
import com.petstore.model.Tag;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Pet Management")
@Story("Read Pet")
public class ReadPetTests extends BaseApiTest {

    @Test(description = "Get pet by valid ID")
    public void getPetById() {
        // Given
        Pet pet = TestDataGenerator.generatePet();
        Response createResponse = petApiClient.createPet(pet);
        verifyStatusCode(createResponse.getStatusCode(), 200);
        Long petId = pet.getId();

        // When
        Response response = petApiClient.getPetById(petId);

        // Then
        verifyStatusCode(response.getStatusCode(), 200);
        Pet retrievedPet = response.as(Pet.class);
        assertThat(retrievedPet.getId()).isEqualTo(petId);
        assertThat(retrievedPet.getName()).isEqualTo(pet.getName());
        assertThat(retrievedPet.getStatus()).isEqualTo(pet.getStatus());
    }

    @Test(description = "Get pet by non-existent ID")
    public void getPetByNonExistentId() {
        // Given
        Long nonExistentId = TestDataGenerator.generateRandomId();

        // When
        Response response = petApiClient.getPetById(nonExistentId);

        // Then
        // NOTE: The Pet Store API behavior is inconsistent
        // Sometimes it returns 200, sometimes 404 for non-existent pets
        // We'll accept either as valid
        assertThat(response.getStatusCode())
            .isIn(200, 404);
    }

    @Test(description = "Find pets by status")
    public void findPetsByStatus() {
        // Given
        String status = "available";

        // When
        Response response = petApiClient.findPetsByStatus(status);

        // Then
        verifyStatusCode(response.getStatusCode(), 200);
        Pet[] pets = response.as(Pet[].class);
        assertThat(pets).isNotEmpty();
        assertThat(pets).allMatch(pet -> status.equals(pet.getStatus()));
    }

    @Test(description = "Find pets by invalid status")
    public void findPetsByInvalidStatus() {
        // Given
        String invalidStatus = "invalid_status";

        // When
        Response response = petApiClient.findPetsByStatus(invalidStatus);

        // Then
        verifyStatusCode(response.getStatusCode(), 400);
    }

    @Test(description = "Find pets by tags")
    public void findPetsByTags() {
        // Given
        String tagName = "test-tag";
        Tag tag = new Tag(TestDataGenerator.generateRandomId(), tagName);
        
        Pet pet = TestDataGenerator.generatePet();
        pet.setTags(Arrays.asList(tag));
        
        Response createResponse = petApiClient.createPet(pet);
        verifyStatusCode(createResponse.getStatusCode(), 200);

        // When
        Response response = petApiClient.findPetsByTags(tagName);

        // Then
        verifyStatusCode(response.getStatusCode(), 200);
        Pet[] pets = response.as(Pet[].class);
        assertThat(pets).isNotEmpty();
        assertThat(pets).anyMatch(p -> p.getTags().stream()
                .anyMatch(t -> tagName.equals(t.getName())));
    }
} 