package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Ingredient;
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

class JpaIngredientRepositoryServiceTest {

    private JpaIngredientRepositoryService underTest;
    @Mock
    private JpaIngredientRepository jpaIngredientRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JpaIngredientRepositoryService(jpaIngredientRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void save() {
        // Given
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");
        Mockito.when(jpaIngredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // When
        Ingredient savedIngredient = underTest.save(ingredient);

        // Then
        Mockito.verify(jpaIngredientRepository).save(ingredient);
        assertEquals("Salt", savedIngredient.getName());
    }

    @Test
    void update() {
        // Given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Pepper");
        Mockito.when(jpaIngredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // When
        Ingredient updatedIngredient = underTest.update(ingredient);

        // Then
        Mockito.verify(jpaIngredientRepository).save(ingredient);
        assertEquals("Pepper", updatedIngredient.getName());
    }

    @Test
    void findById() {
        // Given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Sugar");
        Mockito.when(jpaIngredientRepository.findById(anyInt())).thenReturn(Optional.of(ingredient));

        // When
        Optional<Ingredient> foundIngredient = underTest.findById(1);

        // Then
        Mockito.verify(jpaIngredientRepository).findById(1);
        assertEquals("Sugar", foundIngredient.get().getName());
    }

    @Test
    void findByName() {
        // Given
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Flour");
        Mockito.when(jpaIngredientRepository.findByName(anyString())).thenReturn(Optional.of(ingredient));

        // When
        Optional<Ingredient> foundIngredient = underTest.findByName("Flour");

        // Then
        Mockito.verify(jpaIngredientRepository).findByName("Flour");
        assertEquals("Flour", foundIngredient.get().getName());
    }

    @Test
    void findAll() {
        // Given
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Oil");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Butter");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        Mockito.when(jpaIngredientRepository.findAll()).thenReturn(ingredients);

        // When
        List<Ingredient> allIngredients = underTest.findAll();

        // Then
        Mockito.verify(jpaIngredientRepository).findAll();
        assertEquals(2, allIngredients.size());
        assertEquals("Oil", allIngredients.get(0).getName());
        assertEquals("Butter", allIngredients.get(1).getName());
    }

    @Test
    void deleteById() {
        // When
        underTest.deleteById(1);

        // Then
        Mockito.verify(jpaIngredientRepository).deleteById(1);
    }

    @Test
    void existsById() {
        // Given
        Mockito.when(jpaIngredientRepository.existsById(anyInt())).thenReturn(true);

        // When
        boolean exists = underTest.existsById(1);

        // Then
        Mockito.verify(jpaIngredientRepository).existsById(1);
        assertTrue(exists);
    }
}