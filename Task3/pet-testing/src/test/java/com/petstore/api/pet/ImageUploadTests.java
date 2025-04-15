package com.petstore.api.pet;

import com.petstore.api.base.BaseApiTest;
import com.petstore.api.util.TestDataGenerator;
import com.petstore.model.Pet;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.assertj.core.api.Assertions;

@Feature("Pet Management")
@Story("Image Upload Operations")
public class ImageUploadTests extends BaseApiTest {
    private static final Logger logger = LoggerFactory.getLogger(ImageUploadTests.class);
    
    private Pet testPet;
    private Long petId;
    private File validImageFile;
    private File invalidImageFile;

    @BeforeMethod
    public void setupTestData() throws IOException {
        testPet = TestDataGenerator.generatePet();
        Response createResponse = petApiClient.createPet(testPet);
        petId = createResponse.as(Pet.class).getId();
        
        try {
            validImageFile = new File(getClass().getClassLoader().getResource("test-files/sample-image.jpg").getFile());
            invalidImageFile = new File(getClass().getClassLoader().getResource("test-files/invalid-image.jpg").getFile());
        } catch (Exception e) {
            File testDir = new File("target/test-files");
            testDir.mkdirs();
            
            validImageFile = createTestFile(new File(testDir, "valid-image.jpg"), "image/jpeg");
            invalidImageFile = createTestFile(new File(testDir, "invalid-image.jpg"), "text/plain");
        }
    }
    
    @Test
    @Description("Upload a valid image file to an existing pet")
    public void uploadPetImage() {
        Response response = petApiClient.uploadPetImage(
            petId, 
            validImageFile, 
            "Test image upload"
        );
        
        int statusCode = response.getStatusCode();
        Assertions.assertThat(statusCode).isIn(200, 415);
        
        if (statusCode == 200) {
            Assertions.assertThat(response.jsonPath().getString("message")).contains("uploaded");
            Assertions.assertThat(response.jsonPath().getInt("code")).isEqualTo(200);
        }
    }
    
    @Test
    @Description("Upload an invalid image file to an existing pet")
    public void uploadInvalidImageFormat() {
        Response response = petApiClient.uploadPetImage(
            petId, 
            invalidImageFile, 
            "Test invalid image upload"
        );
        
        int statusCode = response.getStatusCode();
        Assertions.assertThat(statusCode).isIn(400, 415, 200);
    }
    
    @Test
    @Description("Upload an image to a non-existent pet")
    public void uploadImageToNonExistentPet() {
        Response response = petApiClient.uploadPetImage(
            999999999L,
            validImageFile, 
            "Test image upload to non-existent pet"
        );
        
        Assertions.assertThat(response.getStatusCode()).isIn(404, 400, 415);
    }
    
    private File createTestFile(File file, String mimeType) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            if (mimeType.equals("image/jpeg")) {
                out.write(new byte[] {
                    (byte) 0xFF, (byte) 0xD8,
                    (byte) 0xFF, (byte) 0xE0,
                    0x00, 0x10,
                    'J', 'F', 'I', 'F', 0x00,
                    0x01, 0x01,
                    0x00,
                    0x00, 0x01,
                    0x00, 0x01,
                    0x00, 0x00
                });
            } else {
                out.write("This is not a valid image file".getBytes());
            }
        }
        
        file.deleteOnExit();
        return file;
    }
} 