package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class JpaRecipeRepositoryServiceTest {

    private JpaRecipeRepositoryService underTest;
    @Mock
    private JpaRecipeRepository jpaRecipeRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JpaRecipeRepositoryService(jpaRecipeRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void save() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setName("Spaghetti");
        Mockito.when(jpaRecipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // When
        underTest.save(recipe);

        // Then
        Mockito.verify(jpaRecipeRepository).save(recipe);
    }

    @Test
    void findById() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Pizza");
        Mockito.when(jpaRecipeRepository.findById(anyInt())).thenReturn(Optional.of(recipe));

        // When
        Optional<Recipe> foundRecipe = underTest.findById(1);

        // Then
        Mockito.verify(jpaRecipeRepository).findById(1);
        assertTrue(foundRecipe.isPresent());
        assertEquals("Pizza", foundRecipe.get().getName());
    }

    @Test
    void notExistsById() {
        // Given
        Mockito.when(jpaRecipeRepository.existsById(anyInt())).thenReturn(false);

        // When
        boolean notExists = underTest.notExistsById(1);

        // Then
        Mockito.verify(jpaRecipeRepository).existsById(1);
        assertTrue(notExists);
    }

    @Test
    void findAll() {
        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Cake");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Pasta");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Mockito.when(jpaRecipeRepository.findAll()).thenReturn(recipes);

        // When
        List<Recipe> allRecipes = underTest.findAll();

        // Then
        Mockito.verify(jpaRecipeRepository).findAll();
        assertEquals(2, allRecipes.size());
        assertEquals("Cake", allRecipes.get(0).getName());
        assertEquals("Pasta", allRecipes.get(1).getName());
    }

    @Test
    void deleteById() {
        // When
        underTest.deleteById(1);

        // Then
        Mockito.verify(jpaRecipeRepository).deleteById(1);
    }

    @Test
    void update() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Salad");
        Mockito.when(jpaRecipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // When
        underTest.update(recipe);

        // Then
        Mockito.verify(jpaRecipeRepository).save(recipe);
    }

    @Test
    void findByVegetarian() {
        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Vegan Salad");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Tofu Stir Fry");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Mockito.when(jpaRecipeRepository.findByVegetarian(true)).thenReturn(recipes);

        // When
        List<Recipe> vegetarianRecipes = underTest.findByVegetarian(true);

        // Then
        Mockito.verify(jpaRecipeRepository).findByVegetarian(true);
        assertEquals(2, vegetarianRecipes.size());
        assertEquals("Vegan Salad", vegetarianRecipes.get(0).getName());
        assertEquals("Tofu Stir Fry", vegetarianRecipes.get(1).getName());
    }

    @Test
    void findByServings() {
        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Soup");
        recipe1.setServings(4);
        Recipe recipe2 = new Recipe();
        recipe2.setName("Stew");
        recipe2.setServings(4);
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Mockito.when(jpaRecipeRepository.findByServings(4)).thenReturn(recipes);

        // When
        List<Recipe> recipesWithServings = underTest.findByServings(4);

        // Then
        Mockito.verify(jpaRecipeRepository).findByServings(4);
        assertEquals(2, recipesWithServings.size());
        assertEquals("Soup", recipesWithServings.get(0).getName());
        assertEquals("Stew", recipesWithServings.get(1).getName());
    }

    @Test
    void findByName() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setName("Pancakes");
        Mockito.when(jpaRecipeRepository.findByName(anyString())).thenReturn(Optional.of(recipe));

        // When
        Optional<Recipe> foundRecipe = underTest.findByName("Pancakes");

        // Then
        Mockito.verify(jpaRecipeRepository).findByName("Pancakes");
        assertTrue(foundRecipe.isPresent());
        assertEquals("Pancakes", foundRecipe.get().getName());
    }

    @Test
    void findByInstructionsContaining() {
        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Scrambled Eggs");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Egg Salad");
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Mockito.when(jpaRecipeRepository.findByInstructionsContainingIgnoreCase(anyString())).thenReturn(recipes);

        // When
        List<Recipe> recipesContainingText = underTest.findByInstructionsContaining("Egg");

        // Then
        Mockito.verify(jpaRecipeRepository).findByInstructionsContainingIgnoreCase("Egg");
        assertEquals(2, recipesContainingText.size());
        assertEquals("Scrambled Eggs", recipesContainingText.get(0).getName());
        assertEquals("Egg Salad", recipesContainingText.get(1).getName());
    }
}