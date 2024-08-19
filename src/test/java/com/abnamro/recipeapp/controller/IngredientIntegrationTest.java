package com.abnamro.recipeapp.controller;

import com.abnamro.recipeapp.dto.ingredient.CreateIngredientDto;
import com.abnamro.recipeapp.dto.ingredient.IngredientDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private String baseUrl;
    private Integer testIngredientId;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/v1/ingredients";

        // Create a test ingredient
        CreateIngredientDto request = new CreateIngredientDto("Test Ingredient");
        IngredientDto response = webTestClient.post()
                .uri(baseUrl)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IngredientDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Test Ingredient");

        // Retrieve the ingredient by name to get its ID
        IngredientDto ingredient = webTestClient.get()
                .uri(baseUrl + "/name/Test Ingredient")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(IngredientDto.class)
                .returnResult()
                .getResponseBody()
                .stream()
                .findFirst()
                .orElse(null);

        assertThat(ingredient).isNotNull();
        testIngredientId = ingredient.id();
    }

    @AfterEach
    void tearDown() {
        // Remove test record
        if (testIngredientId != null) {
            webTestClient.delete()
                    .uri(baseUrl + "/{id}", testIngredientId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNoContent();
        }
    }

    @Test
    @DisplayName("Update an existing ingredient successfully")
    void updateIngredient() {
        IngredientDto updatedIngredient = new IngredientDto(testIngredientId, "Updated Ingredient");
        webTestClient.put()
                .uri(baseUrl + "/" + testIngredientId)
                .bodyValue(updatedIngredient)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IngredientDto.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.name()).isEqualTo("Updated Ingredient");
                });
    }

    @Test
    @DisplayName("Get an ingredient by its ID")
    void getIngredientById() {
        webTestClient.get()
                .uri(baseUrl + "/" + testIngredientId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IngredientDto.class)
                .value(ingredient -> {
                    assertThat(ingredient).isNotNull();
                    assertThat(ingredient.id()).isEqualTo(testIngredientId);
                });
    }

    @Test
    @DisplayName("Get an ingredient by its name")
    void getIngredientByName() {
        webTestClient.get()
                .uri(baseUrl + "/name/Test Ingredient")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(IngredientDto.class)
                .value(ingredients -> {
                    assertThat(ingredients).isNotEmpty();
                    assertThat(ingredients).anyMatch(i -> i.name().equals("Test Ingredient"));
                });
    }

    @Test
    @DisplayName("Get all ingredients")
    void getAllIngredients() {
        webTestClient.get()
                .uri(baseUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(IngredientDto.class)
                .value(ingredients -> assertThat(ingredients).isNotEmpty());
    }

    @Test
    @DisplayName("Fail to update a non-existent ingredient")
    void updateNonExistentIngredient() {
        // Use a non-existent ID
        int nonExistentId = 9999;
        IngredientDto updatedIngredient = new IngredientDto(nonExistentId, "Updated Ingredient");

        webTestClient.put()
                .uri(baseUrl + "/" + nonExistentId)
                .bodyValue(updatedIngredient)
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 Not Found
                .expectBody()
                .consumeWith(response -> assertThat(response.getStatus()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Fail to delete a non-existent ingredient")
    void deleteNonExistentIngredient() {
        int nonExistentId = 9999;
        webTestClient.delete()
                .uri(baseUrl + "/" + nonExistentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 Not Found
                .expectBody()
                .consumeWith(response -> assertThat(response.getStatus()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Fail to get an ingredient by a non-existent ID")
    void getIngredientByNonExistentId() {
        int nonExistentId = 9999;
        webTestClient.get()
                .uri(baseUrl + "/" + nonExistentId)
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 Not Found
                .expectBody()
                .consumeWith(response -> assertThat(response.getStatus()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Fail to get an ingredient by an invalid name")
    void getIngredientByInvalidName() {
        String invalidName = "Non-Existent Ingredient";
        webTestClient.get()
                .uri(baseUrl + "/name/" + invalidName)
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 Not Found
                .expectBody()
                .consumeWith(response -> assertThat(response.getStatus()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }
}