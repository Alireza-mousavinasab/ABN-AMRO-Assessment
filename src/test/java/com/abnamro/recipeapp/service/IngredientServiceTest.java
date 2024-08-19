package com.abnamro.recipeapp.service;

import com.abnamro.exception.DuplicateResourceException;
import com.abnamro.exception.ResourceNotFoundException;
import com.abnamro.exception.ValidationException;
import com.abnamro.recipeapp.dto.ingredient.CreateIngredientDto;
import com.abnamro.recipeapp.dto.ingredient.IngredientDto;
import com.abnamro.recipeapp.entity.Ingredient;
import com.abnamro.recipeapp.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    private IngredientService underTest;

    @Mock
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        underTest = new IngredientService(ingredientRepository);
    }

    @Test
    @DisplayName("Should add a new ingredient when it does not already exist")
    void addIngredient_shouldAddNewIngredient() {
        // Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto("Tomato");
        Ingredient ingredient = new Ingredient();
        ingredient.setName(createIngredientDto.name());

        when(ingredientRepository.findByName(createIngredientDto.name())).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // When
        CreateIngredientDto result = underTest.addIngredient(createIngredientDto);

        // Then
        assertEquals(createIngredientDto.name(), result.name());

        // Verify save was called
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        assertEquals(createIngredientDto.name(), ingredientCaptor.getValue().getName());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when adding an ingredient that already exists")
    void addIngredient_shouldThrowDuplicateResourceException() {
        // Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto("Tomato");
        when(ingredientRepository.findByName(createIngredientDto.name())).thenReturn(Optional.of(new Ingredient()));

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> underTest.addIngredient(createIngredientDto));

        // Verify save was not called
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when adding an ingredient with an empty name")
    void addIngredient_shouldThrowValidationException_whenNameIsEmpty() {
        // Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto("");

        // When & Then
        assertThrows(ValidationException.class, () -> underTest.addIngredient(createIngredientDto));

        // Verify save was not called
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    @DisplayName("Should update an existing ingredient")
    void updateIngredient_shouldUpdateExistingIngredient() {
        // Given
        IngredientDto ingredientDto = new IngredientDto(1, "UpdatedName");
        Ingredient existingIngredient = new Ingredient();
        existingIngredient.setId(1);
        existingIngredient.setName("OldName");

        when(ingredientRepository.findById(1)).thenReturn(Optional.of(existingIngredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(existingIngredient);

        // When
        IngredientDto result = underTest.updateIngredient(ingredientDto);

        // Then
        assertNotNull(result);
        assertEquals(ingredientDto.id(), result.id());
        assertEquals(ingredientDto.name(), result.name());

        // Verify save was called
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        assertEquals(ingredientDto.name(), ingredientCaptor.getValue().getName());
    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent ingredient")
    void updateIngredient_shouldThrowResourceNotFoundException() {
        // Given
        IngredientDto ingredientDto = new IngredientDto(1, "UpdatedName");

        when(ingredientRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateIngredient(ingredientDto));

        // Verify update was not called
        verify(ingredientRepository, never()).update(any(Ingredient.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when updating an ingredient with an empty name")
    void updateIngredient_shouldThrowValidationException_whenNameIsEmpty() {
        // Given
        IngredientDto ingredientDto = new IngredientDto(1, "");

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateIngredient(ingredientDto));

        // Verify update was not called
        verify(ingredientRepository, never()).update(any(Ingredient.class));
    }

    @Test
    @DisplayName("Should delete an existing ingredient")
    void deleteById_shouldDeleteExistingIngredient() {
        // Given
        int id = 1;

        when(ingredientRepository.existsById(id)).thenReturn(true);

        // When
        underTest.deleteById(id);

        // Then
        verify(ingredientRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to delete a non-existent ingredient")
    void deleteById_shouldThrowResourceNotFoundException_whenIngredientDoesNotExist() {
        // Given
        int id = 1;
        when(ingredientRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.deleteById(id));

        // Verify deleteById was not called
        verify(ingredientRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Should return a list of all ingredients")
    void getAllIngredients_shouldReturnListOfIngredients() {
        // Given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Tomato");
        when(ingredientRepository.findAll()).thenReturn(List.of(ingredient));

        // When
        List<IngredientDto> result = underTest.getAllIngredients();

        // Then
        assertEquals(1, result.size());
        assertEquals(ingredient.getId(), result.getFirst().id());
        assertEquals(ingredient.getName(), result.getFirst().name());

        // Verify findAll was called
        verify(ingredientRepository).findAll();
    }

    @Test
    @DisplayName("Should return ingredient by its ID")
    void getIngredientById_shouldReturnIngredient() {
        // Given
        int id = 1;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName("Tomato");
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        // When
        IngredientDto result = underTest.getIngredientById(id);

        // Then
        assertEquals(id, result.id());
        assertEquals("Tomato", result.name());

        // Verify findById was called
        verify(ingredientRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ingredient with given ID does not exist")
    void getIngredientById_shouldThrowResourceNotFoundException_whenIngredientDoesNotExist() {
        // Given
        int id = 1;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getIngredientById(id));

        // Verify findById was called
        verify(ingredientRepository).findById(id);
    }

    @Test
    @DisplayName("Should return ingredient by its name")
    void getIngredientByName_shouldReturnIngredient() {
        // Given
        String name = "Tomato";
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName(name);
        when(ingredientRepository.findByName(name)).thenReturn(Optional.of(ingredient));

        // When
        IngredientDto result = underTest.getIngredientByName(name);

        // Then
        assertEquals(1, result.id());
        assertEquals(name, result.name());

        // Verify findByName was called
        verify(ingredientRepository).findByName(name);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ingredient with given name does not exist")
    void getIngredientByName_shouldThrowResourceNotFoundException_whenIngredientDoesNotExist() {
        // Given
        String name = "Tomato";
        when(ingredientRepository.findByName(name)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getIngredientByName(name));

        // Verify findByName was called
        verify(ingredientRepository).findByName(name);
    }
}
