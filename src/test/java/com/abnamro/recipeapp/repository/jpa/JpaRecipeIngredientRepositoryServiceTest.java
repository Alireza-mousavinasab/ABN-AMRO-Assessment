package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.RecipeIngredient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class JpaRecipeIngredientRepositoryServiceTest {

    private JpaRecipeIngredientRepositoryService underTest;
    @Mock
    private JpaRecipeIngredientRepository jpaRecipeIngredientRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JpaRecipeIngredientRepositoryService(jpaRecipeIngredientRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void save() {
        // Given
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setAmount(100.0); // Set any necessary properties

        // When
        underTest.save(recipeIngredient);

        // Then
        verify(jpaRecipeIngredientRepository).save(recipeIngredient);
    }
}