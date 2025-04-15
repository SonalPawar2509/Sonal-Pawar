package com.petstore.api.util;

import com.github.javafaker.Faker;
import com.petstore.model.Category;
import com.petstore.model.Pet;
import com.petstore.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static Pet generatePet() {
        Pet pet = new Pet();
        pet.setId(generateRandomId());
        pet.setName(generateRandomName());
        pet.setCategory(generateCategory());
        pet.setPhotoUrls(generatePhotoUrls());
        pet.setTags(generateTags());
        pet.setStatus("available");
        return pet;
    }

    public static Long generateRandomId() {
        return faker.number().randomNumber();
    }

    public static String generateRandomName() {
        return faker.funnyName().name();
    }

    public static String generateRandomPhotoUrl() {
        return faker.internet().image();
    }

    public static Tag generateTag(Long id, String name) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        return tag;
    }

    private static Category generateCategory() {
        Category category = new Category();
        category.setId(faker.number().randomNumber());
        category.setName(faker.animal().name());
        return category;
    }

    private static List<String> generatePhotoUrls() {
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(faker.internet().image());
        return photoUrls;
    }

    private static List<Tag> generateTags() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setId(faker.number().randomNumber());
        tag.setName(faker.lorem().word());
        tags.add(tag);
        return tags;
    }
} 