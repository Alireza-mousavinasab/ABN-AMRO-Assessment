package com.abnamro.recipeapp.controller;

import com.abnamro.recipeapp.dto.RecipeIngredientDto;
import com.abnamro.recipeapp.dto.recipe.CreateRecipeDto;
import com.abnamro.recipeapp.dto.recipe.RecipeDto;
import com.abnamro.recipeapp.dto.recipe.RecipeRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private String baseUrl;
    private Integer testRecipeId;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/v1/recipes";

        CreateRecipeDto createRecipeRequest = new CreateRecipeDto(
                "Test Recipe",
                "Test instructions",
                true,
                4
        );
        List<RecipeIngredientDto> recipeIngredients = List.of(
                new RecipeIngredientDto(1.0, "cup", 18), // Assuming ingredient ID 10 exists
                new RecipeIngredientDto(2.0, "tbsp", 19)  // Assuming ingredient ID 11 exists
        );
        RecipeRequestDto recipeRequest = new RecipeRequestDto(createRecipeRequest, recipeIngredients);

        RecipeDto response = webTestClient.post()
                .uri(baseUrl)
                .bodyValue(recipeRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Test Recipe");

        // Retrieve the recipe by name to get its ID
        RecipeDto recipe = webTestClient.get()
                .uri(baseUrl + "/name/Test Recipe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(recipe).isNotNull();
        testRecipeId = recipe.id();
    }

    @AfterEach
    void tearDown() {
        if (testRecipeId != null) {
            webTestClient.delete()
                    .uri(baseUrl + "/{id}", testRecipeId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNoContent();

            // Check if the recipe has been deleted to confirm
            webTestClient.get()
                    .uri(baseUrl + "/" + testRecipeId)
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Test
    @DisplayName("Update an existing recipe successfully")
    void updateRecipe() {
        List<RecipeIngredientDto> updatedIngredients = List.of(
                new RecipeIngredientDto(1.5, "cup", 18), // Adjusted quantity
                new RecipeIngredientDto(3.0, "tbsp", 19)  // Adjusted quantity
        );
        RecipeDto updatedRecipe = new RecipeDto(
                testRecipeId,
                "Updated Recipe",
                "Updated instructions",
                true,
                4,
                updatedIngredients
        );

        webTestClient.put()
                .uri(baseUrl + "/" + testRecipeId)
                .bodyValue(updatedRecipe)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RecipeDto.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.name()).isEqualTo("Updated Recipe");
                    assertThat(response.ingredients()).hasSize(2);
                });
    }

    @Test
    @DisplayName("Get vegetarian recipes")
    void getVegetarianRecipes() {
        webTestClient.get()
                .uri(baseUrl + "/vegetarian")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDto.class)
                .value(recipes -> assertThat(recipes).isNotEmpty());
    }

    @Test
    @DisplayName("Get non-vegetarian recipes")
    void getNonVegetarianRecipes() {
        webTestClient.get()
                .uri(baseUrl + "/non-vegetarian")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDto.class)
                .value(recipes -> assertThat(recipes).isNotEmpty());
    }

    @Test
    @DisplayName("Get recipes by servings")
    void getRecipesByServings() {
        webTestClient.get()
                .uri(baseUrl + "/servings/4")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDto.class)
                .value(recipes -> assertThat(recipes).isNotEmpty());
    }

    @Test
    @DisplayName("Get recipes by instruction text")
    void getRecipesByInstructions() {
        webTestClient.get()
                .uri(baseUrl + "/instruction/Test")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDto.class)
                .value(recipes -> assertThat(recipes).isNotEmpty());
    }

    @Test
    @DisplayName("Get a recipe by its name")
    void getRecipeByName() {
        webTestClient.get()
                .uri(baseUrl + "/name/Test Recipe")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RecipeDto.class)
                .value(recipe -> {
                    assertThat(recipe).isNotNull();
                    assertThat(recipe.name()).isEqualTo("Test Recipe");
                    assertThat(recipe.ingredients()).isNotEmpty();
                });
    }

    @Test
    @DisplayName("Get all recipes")
    void getAllRecipes() {
        webTestClient.get()
                .uri(baseUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDto.class)
                .value(recipes -> assertThat(recipes).isNotEmpty());
    }

    @Test
    @DisplayName("Search recipes with various criteria")
    void searchRecipes() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(baseUrl + "/search")
                        .queryParam("vegetarian", true)
                        .queryParam("servings", 4)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecipeDto.class)
                .value(recipes -> assertThat(recipes).isNotEmpty());
    }

    @Test
    @DisplayName("Fail to update a non-existent recipe")
    void updateNonExistentRecipe() {
        int nonExistentId = 9999;
        List<RecipeIngredientDto> updatedIngredients = List.of(
                new RecipeIngredientDto(1.5, "cup", 18),
                new RecipeIngredientDto(3.0, "tbsp", 19)
        );
        RecipeDto updatedRecipe = new RecipeDto(
                nonExistentId,
                "Updated Recipe",
                "Updated instructions",
                true,
                4,
                updatedIngredients
        );
        webTestClient.put()
                .uri(baseUrl + "/" + nonExistentId)
                .bodyValue(updatedRecipe)
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 Not Found
                .expectBody()
                .consumeWith(response -> assertThat(response.getStatus()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Fail to delete a non-existent recipe")
    void deleteNonExistentRecipe() {
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
    @DisplayName("Fail to get a recipe by an invalid name")
    void getRecipeByInvalidName() {
        String invalidName = "Non-Existent Recipe";
        webTestClient.get()
                .uri(baseUrl + "/name/" + invalidName)
                .exchange()
                .expectStatus().isNotFound() // Expecting 404 Not Found
                .expectBody()
                .consumeWith(response -> assertThat(response.getStatus()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }
}