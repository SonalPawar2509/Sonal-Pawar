package com.petstore.api.client;

import com.petstore.api.util.RequestResponseLoggingFilter;
import com.petstore.model.Pet;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PetApiClient extends ApiClientBase {
    private static final String PET_PATH = "/pet";
    private static final String PET_BY_ID_PATH = PET_PATH + "/{petId}";
    private static final String PET_BY_STATUS_PATH = PET_PATH + "/findByStatus";
    private static final String PET_BY_TAGS_PATH = PET_PATH + "/findByTags";
    private static final String PET_UPLOAD_IMAGE_PATH = PET_BY_ID_PATH + "/uploadImage";
    private final RequestResponseLoggingFilter loggingFilter;

    public PetApiClient(String baseUri) {
        super(baseUri);
        this.loggingFilter = new RequestResponseLoggingFilter();
    }

    public Response createPet(Pet pet) {
        return post(PET_PATH, pet);
    }

    public Response getPetById(Long id) {
        Map<String, Object> pathParams = Collections.singletonMap("petId", id);
        return get(PET_BY_ID_PATH, pathParams);
    }

    public Response updatePet(Pet pet) {
        return put(PET_PATH, pet);
    }

    public Response updatePetWithFormData(Long id, String name, String status) {
        Map<String, Object> pathParams = Collections.singletonMap("petId", id);
        Map<String, String> formParams = new HashMap<>();
        formParams.put("name", name);
        formParams.put("status", status);
        
        return postFormData(PET_BY_ID_PATH, formParams, pathParams);
    }

    public Response deletePet(Long id) {
        Map<String, Object> pathParams = Collections.singletonMap("petId", id);
        return delete(PET_BY_ID_PATH, pathParams);
    }

    public Response deletePetWithoutAuth(Long id) {
        return io.restassured.RestAssured.given()
                .baseUri(getBaseUri())
                .contentType("application/json")
                .pathParam("petId", id)
                .filter(getLoggingFilter())
                .when()
                .delete(PET_BY_ID_PATH);
    }

    public Response findPetsByStatus(String status) {
        return get(PET_BY_STATUS_PATH + "?status=" + status);
    }

    public Response findPetsByTags(String tags) {
        return get(PET_BY_TAGS_PATH + "?tags=" + tags);
    }

    public Response uploadPetImage(Long petId, File imageFile, String additionalMetadata) {
        Map<String, Object> pathParams = Collections.singletonMap("petId", petId);
        Map<String, Object> multipartData = new HashMap<>();
        
        if (additionalMetadata != null) {
            multipartData.put("additionalMetadata", additionalMetadata);
        }
        
        File fileToUpload = imageFile;
        if (fileToUpload == null || !fileToUpload.exists() || !fileToUpload.canRead()) {
            try {
                fileToUpload = new File(getClass().getClassLoader().getResource("test-files/sample-image.jpg").getFile());
            } catch (Exception e) {
                multipartData.put("file", "");
                return postMultipartData(PET_UPLOAD_IMAGE_PATH, multipartData, pathParams);
            }
        }
        
        multipartData.put("file", fileToUpload);
        
        return postMultipartData(PET_UPLOAD_IMAGE_PATH, multipartData, pathParams);
    }
} 