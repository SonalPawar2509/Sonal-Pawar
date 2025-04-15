package com.petstore.api.pet;

import com.petstore.api.base.BaseApiTest;
import com.petstore.api.util.TestDataGenerator;
import com.petstore.model.Category;
import com.petstore.model.Pet;
import com.petstore.model.Tag;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Pet Management")
@Story("Create Pet")
public class CreatePetTests extends BaseApiTest {

    @Test(description = "Create a new pet with valid data")
    public void createPetWithValidData() {
        Pet pet = TestDataGenerator.generatePet();

        Response response = petApiClient.createPet(pet);

        verifyStatusCode(response.getStatusCode(), 200);
        
        Pet createdPet = response.as(Pet.class);
        assertThat(createdPet.getId()).isEqualTo(pet.getId());
        assertThat(createdPet.getName()).isEqualTo(pet.getName());
        assertThat(createdPet.getStatus()).isEqualTo(pet.getStatus());
    }

    @Test(description = "Create a pet with minimal required data")
    public void createPetWithMinimalData() {
        Pet pet = new Pet();
        pet.setId(TestDataGenerator.generateRandomId());
        pet.setName(TestDataGenerator.generateRandomName());

        Response response = petApiClient.createPet(pet);

        verifyStatusCode(response.getStatusCode(), 200);
        
        Pet createdPet = response.as(Pet.class);
        assertThat(createdPet.getId()).isEqualTo(pet.getId());
        assertThat(createdPet.getName()).isEqualTo(pet.getName());
    }

    @Test(description = "Create a pet with maximum allowed values")
    public void createPetWithMaxValues() {
        Pet pet = new Pet();
        pet.setId(Long.MAX_VALUE);
        pet.setName(generateLongString(100));
        pet.setStatus("available");

        List<String> photoUrls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            photoUrls.add(TestDataGenerator.generateRandomPhotoUrl());
        }
        pet.setPhotoUrls(photoUrls);

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tags.add(TestDataGenerator.generateTag(Long.MAX_VALUE - i, generateLongString(50)));
        }
        pet.setTags(tags);

        Category category = new Category();
        category.setId(Long.MAX_VALUE);
        category.setName(generateLongString(50));
        pet.setCategory(category);

        Response response = petApiClient.createPet(pet);

        verifyStatusCode(response.getStatusCode(), 200);
        
        Pet createdPet = response.as(Pet.class);
        assertThat(createdPet.getId()).isEqualTo(pet.getId());
        assertThat(createdPet.getName()).isEqualTo(pet.getName());
        assertThat(createdPet.getStatus()).isEqualTo(pet.getStatus());
        assertThat(createdPet.getPhotoUrls()).hasSize(10);
        assertThat(createdPet.getTags()).hasSize(10);
        assertThat(createdPet.getCategory().getId()).isEqualTo(category.getId());
        assertThat(createdPet.getCategory().getName()).isEqualTo(category.getName());
    }

    private String generateLongString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('a');
        }
        return sb.toString();
    }
} 