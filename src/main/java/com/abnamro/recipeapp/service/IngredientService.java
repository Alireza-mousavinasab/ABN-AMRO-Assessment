package com.abnamro.recipeapp.service;

import com.abnamro.exception.DuplicateResourceException;
import com.abnamro.exception.ValidationException;
import com.abnamro.recipeapp.dto.ingredient.CreateIngredientDto;
import com.abnamro.recipeapp.dto.ingredient.IngredientDto;
import com.abnamro.recipeapp.entity.Ingredient;
import com.abnamro.exception.ResourceNotFoundException;
import com.abnamro.recipeapp.repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService {

    private static final Logger logger = LoggerFactory.getLogger(IngredientService.class);

    IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public CreateIngredientDto addIngredient(CreateIngredientDto ingredientDto) {
        logger.info("Request to add ingredient: {}", ingredientDto);
        if (ingredientRepository.findByName(ingredientDto.name()).isPresent()) {
            throw new DuplicateResourceException("Ingredient with name " + ingredientDto.name() + " already exists.");
        }

        if (ingredientDto.name() == null || ingredientDto.name().trim().isEmpty()) {
            throw new ValidationException("Ingredient name must not be empty.");
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.name());
        CreateIngredientDto createdIngredient = convertToCreateIngredientDto(ingredientRepository.save(ingredient));
        logger.info("Ingredient added successfully: {}", createdIngredient);
        return createdIngredient;
    }

    @Transactional
    public IngredientDto updateIngredient(IngredientDto ingredient) {
        logger.info("Request to update ingredient with id {}: {}", ingredient.id(), ingredient);
        Ingredient existingIngredient = ingredientRepository.findById(ingredient.id())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id " + ingredient.id() + " not found!"));

        if (ingredient.name() == null || ingredient.name().trim().isEmpty()) {
            throw new ValidationException("Ingredient name must not be empty.");
        }

        existingIngredient.setName(ingredient.name());
        IngredientDto updatedIngredient = convertToIngredientDto(ingredientRepository.save(existingIngredient));
        logger.info("Ingredient updated successfully: {}", updatedIngredient);
        return updatedIngredient;
    }

    @Transactional
    public void deleteById(int id) {
        logger.info("Request to delete ingredient with id {}", id);
        if (!ingredientRepository.existsById(id))
            throw new ResourceNotFoundException("Ingredient with id " + id + " does not exist!");
        ingredientRepository.deleteById(id);
        logger.info("Ingredient with id {} deleted successfully", id);
    }

    @Transactional(readOnly = true)
    public List<IngredientDto> getAllIngredients() {
        logger.info("Request to get all ingredients");
        List<IngredientDto> ingredients = ingredientRepository.findAll().stream().map(this::convertToIngredientDto).toList();
        logger.info("Retrieved ingredients: {}", ingredients);
        return ingredients;
    }

    @Transactional(readOnly = true)
    public IngredientDto getIngredientById(int id) {
        logger.info("Request to get ingredient by id {}", id);
        IngredientDto ingredientDto = ingredientRepository.findById(id).map(this::convertToIngredientDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with id " + id + " does not found!"));
        logger.info("Retrieved ingredient: {}", ingredientDto);
        return ingredientDto;
    }

    @Transactional(readOnly = true)
    public IngredientDto getIngredientByName(String name) {
        logger.info("Request to get ingredients by name containing {}", name);
        return ingredientRepository.findByName(name).map(this::convertToIngredientDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient with name " + name + " does not found!"));
    }

    private IngredientDto convertToIngredientDto(Ingredient ingredient) {
        return new IngredientDto(ingredient.getId(), ingredient.getName());
    }

    private CreateIngredientDto convertToCreateIngredientDto(Ingredient ingredient) {
        return new CreateIngredientDto(ingredient.getName());
    }
}
