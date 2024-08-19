package com.abnamro.recipeapp.service;

import com.abnamro.exception.BadRequestException;
import com.abnamro.exception.ResourceNotFoundException;
import com.abnamro.exception.ValidationException;
import com.abnamro.recipeapp.dto.recipe.CreateRecipeDto;
import com.abnamro.recipeapp.dto.recipe.RecipeDto;
import com.abnamro.recipeapp.dto.RecipeIngredientDto;
import com.abnamro.recipeapp.dto.recipe.RecipeRequestDto;
import com.abnamro.recipeapp.entity.Ingredient;
import com.abnamro.recipeapp.entity.Recipe;
import com.abnamro.recipeapp.entity.RecipeIngredient;
import com.abnamro.recipeapp.repository.IngredientRepository;
import com.abnamro.recipeapp.repository.RecipeRepository;
import com.abnamro.recipeapp.repository.RecipeIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private RecipeService underTest;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @BeforeEach
    void setUp() {
        underTest = new RecipeService(recipeRepository, recipeIngredientRepository, ingredientRepository);
    }

    @Test
    @DisplayName("Should add a new recipe with valid data")
    void addRecipe_shouldAddNewRecipe() {
        // Given
        CreateRecipeDto createRecipeDto = new CreateRecipeDto("Pasta", "Boil pasta and mix with sauce.", true, 2);
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(createRecipeDto, List.of(new RecipeIngredientDto(200, "grams", 1)));

        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Pasta");
        recipe.setInstructions("Boil pasta and mix with sauce.");
        recipe.setVegetarian(true);
        recipe.setServings(2);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Test Ingredient");

        when(ingredientRepository.findById(any(Integer.class))).thenReturn(Optional.of(ingredient));
        recipeRepository.save(recipe);

        // When
        CreateRecipeDto result = underTest.addRecipe(recipeRequestDto);

        // Then
        assertNotNull(result);
        assertEquals("Pasta", result.name());
        assertEquals("Boil pasta and mix with sauce.", result.instructions());
        assertTrue(result.isVegetarian());
        assertEquals(2, result.servings());
    }


    @Test
    @DisplayName("Should throw BadRequestException when servings is zero or negative")
    void addRecipe_shouldThrowBadRequestExceptionWhenServingsInvalid() {
        // Given
        CreateRecipeDto createRecipeDto = new CreateRecipeDto("Pasta", "Boil pasta and mix with sauce.", true, 0);
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(createRecipeDto, List.of(new RecipeIngredientDto(200, "grams", 1)));

        // When & Then
        assertThrows(BadRequestException.class, () -> underTest.addRecipe(recipeRequestDto));
    }

    @Test
    @DisplayName("Should throw ValidationException when recipe has no ingredients")
    void addRecipe_shouldThrowValidationExceptionWhenNoIngredients() {
        // Given
        CreateRecipeDto createRecipeDto = new CreateRecipeDto("Pasta", "Boil pasta and mix with sauce.", true, 2);
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(createRecipeDto, List.of());

        // When & Then
        assertThrows(ValidationException.class, () -> underTest.addRecipe(recipeRequestDto));
    }

    @Test
    @DisplayName("Should update an existing recipe with valid data")
    void updateRecipe_shouldUpdateExistingRecipe() {
        // Given
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1);
        existingRecipe.setName("Old Pasta");
        existingRecipe.setInstructions("Old instructions");
        existingRecipe.setVegetarian(true);
        existingRecipe.setServings(2);
        existingRecipe.setRecipeIngredients(new ArrayList<>());

        RecipeDto updatedRecipeDto = new RecipeDto(1, "Updated Pasta", "Updated instructions", true, 4,
                List.of(new RecipeIngredientDto(200, "grams", 1)));

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Test Ingredient");

        when(recipeRepository.findById(1)).thenReturn(Optional.of(existingRecipe));
        when(ingredientRepository.findById(any(Integer.class))).thenReturn(Optional.of(ingredient));
        recipeRepository.update(existingRecipe);

        // When
        RecipeDto result = underTest.updateRecipe(updatedRecipeDto);

        // Then
        assertNotNull(result);
        assertEquals("Updated Pasta", result.name());
        assertEquals("Updated instructions", result.instructions());
        assertTrue(result.isVegetarian());
        assertEquals(4, result.servings());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent recipe")
    void updateRecipe_shouldThrowResourceNotFoundExceptionWhenRecipeNotFound() {
        // Given
        RecipeDto recipeDto = new RecipeDto(1, "Updated Pasta", "Updated instructions", true, 4, List.of(new RecipeIngredientDto(200, "grams", 1)));

        when(recipeRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateRecipe(recipeDto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when updating with zero or negative servings")
    void updateRecipe_shouldThrowBadRequestExceptionWhenServingsInvalid() {
        // Given
        RecipeDto recipeDto = new RecipeDto(1, "Updated Pasta", "Updated instructions", true, 0, List.of(new RecipeIngredientDto(200, "grams", 1)));

        // When & Then
        assertThrows(BadRequestException.class, () -> underTest.updateRecipe(recipeDto));
    }

    @Test
    @DisplayName("Should delete an existing recipe")
    void deleteRecipe_shouldDeleteExistingRecipe() {
        // Given
        int id = 1;

        when(recipeRepository.notExistsById(id)).thenReturn(false);

        // When
        underTest.deleteRecipe(id);

        // Then
        verify(recipeRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting a non-existent recipe")
    void deleteRecipe_shouldThrowResourceNotFoundExceptionWhenRecipeNotFound() {
        // Given
        int id = 1;

        when(recipeRepository.notExistsById(id)).thenReturn(true);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.deleteRecipe(id));
    }

    @Test
    @DisplayName("Should return all recipes")
    void getAllRecipes_shouldReturnListOfRecipes() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Pasta");

        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        // When
        List<RecipeDto> result = underTest.getAllRecipes();

        // Then
        assertEquals(1, result.size());
        verify(recipeRepository).findAll();
    }

    @Test
    @DisplayName("Should return recipe by ID")
    void getRecipeById_shouldReturnRecipe() {
        // Given
        int id = 1;
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName("Pasta");

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));

        // When
        RecipeDto result = underTest.getRecipeById(id);

        // Then
        assertEquals(id, result.id());
        assertEquals("Pasta", result.name());
        verify(recipeRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when recipe with given ID does not exist")
    void getRecipeById_shouldThrowResourceNotFoundExceptionWhenRecipeNotFound() {
        // Given
        int id = 1;

        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getRecipeById(id));
    }

    @Test
    @DisplayName("Should return recipe by name")
    void getRecipeByName_shouldReturnRecipe() {
        // Given
        String name = "Pasta";
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName(name);

        when(recipeRepository.findByName(name)).thenReturn(Optional.of(recipe));

        // When
        RecipeDto result = underTest.getRecipeByName(name);

        // Then
        assertEquals(1, result.id());
        assertEquals(name, result.name());
        verify(recipeRepository).findByName(name);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when recipe with given name does not exist")
    void getRecipeByName_shouldThrowResourceNotFoundExceptionWhenRecipeNotFound() {
        // Given
        String name = "Pasta";

        when(recipeRepository.findByName(name)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getRecipeByName(name));
    }

    @Test
    @DisplayName("Should return recipes by vegetarian status")
    void findByVegetarianRecipes_shouldReturnRecipesByVegetarianStatus() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setVegetarian(true);

        when(recipeRepository.findByVegetarian(true)).thenReturn(List.of(recipe));

        // When
        List<RecipeDto> result = underTest.findByVegetarianRecipes(true);

        // Then
        assertEquals(1, result.size());
        verify(recipeRepository).findByVegetarian(true);
    }

    @Test
    @DisplayName("Should return recipes by servings")
    void findByServings_shouldReturnRecipesByServings() {
        // Given
        int servings = 4;
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setServings(servings);

        when(recipeRepository.findByServings(servings)).thenReturn(List.of(recipe));

        // When
        List<RecipeDto> result = underTest.findByServings(servings);

        // Then
        assertEquals(1, result.size());
        verify(recipeRepository).findByServings(servings);
    }

    @Test
    @DisplayName("Should return recipes by instructions containing text")
    void findByInstructionsContaining_shouldReturnRecipesByInstructionsContainingText() {
        // Given
        String text = "Boil";
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setInstructions("Boil pasta and mix with sauce.");

        when(recipeRepository.findByInstructionsContaining(text)).thenReturn(List.of(recipe));

        // When
        List<RecipeDto> result = underTest.findByInstructionsContaining(text);

        // Then
        assertEquals(1, result.size());
        verify(recipeRepository).findByInstructionsContaining(text);
    }

    @Test
    @DisplayName("Should search recipes with specified criteria")
    void searchRecipes_shouldReturnRecipesMatchingCriteria() {
        // Given
        boolean vegetarian = true;
        int servings = 2;
        List<Integer> includeIngredients = List.of(1);
        List<Integer> excludeIngredients = List.of(2);
        String instruction = "Boil";

        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setVegetarian(vegetarian);
        recipe.setServings(servings);
        recipe.setInstructions("Boil pasta and mix with sauce.");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2);

        RecipeIngredient recipeIngredient1 = new RecipeIngredient();
        recipeIngredient1.setIngredient(ingredient1);
        recipeIngredient1.setRecipe(recipe);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient();
        recipeIngredient2.setIngredient(ingredient2);
        recipeIngredient2.setRecipe(recipe);

        recipe.setRecipeIngredients(List.of(recipeIngredient1, recipeIngredient2));

        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        // When
        List<RecipeDto> result = underTest.searchRecipes(vegetarian, servings, includeIngredients, excludeIngredients, instruction);

        // Then
        assertEquals(0, result.size()); // Since excludeIngredients has ingredient with id 2
        verify(recipeRepository).findAll();
    }
}
