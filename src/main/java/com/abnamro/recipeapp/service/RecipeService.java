package com.abnamro.recipeapp.service;

import com.abnamro.exception.BadRequestException;
import com.abnamro.exception.ValidationException;
import com.abnamro.recipeapp.dto.recipe.CreateRecipeDto;
import com.abnamro.recipeapp.dto.recipe.RecipeDto;
import com.abnamro.recipeapp.dto.RecipeIngredientDto;
import com.abnamro.recipeapp.dto.recipe.RecipeRequestDto;
import com.abnamro.recipeapp.entity.Ingredient;
import com.abnamro.recipeapp.entity.Recipe;
import com.abnamro.recipeapp.entity.RecipeIngredient;
import com.abnamro.exception.ResourceNotFoundException;
import com.abnamro.recipeapp.repository.IngredientRepository;
import com.abnamro.recipeapp.repository.RecipeRepository;
import com.abnamro.recipeapp.repository.RecipeIngredientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RecipeService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public CreateRecipeDto addRecipe(RecipeRequestDto recipeRequest) {
        logger.info("Request to add recipe: {}", recipeRequest);
        CreateRecipeDto recipe = recipeRequest.recipe();
        List<RecipeIngredientDto> recipeIngredients = recipeRequest.recipeIngredients();

        if (recipe.servings() <= 0) {
            throw new BadRequestException("Servings must be greater than zero.");
        }

        if (recipeIngredients == null || recipeIngredients.isEmpty()) {
            throw new ValidationException("Recipe must have at least one ingredient.");
        }

        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipe.name());
        newRecipe.setInstructions(recipe.instructions());
        newRecipe.setServings(recipe.servings());
        newRecipe.setVegetarian(recipe.isVegetarian());
        recipeRepository.save(newRecipe);

        for (var recipeIngredient : recipeIngredients) {
            Ingredient ingredient = ingredientRepository.findById(recipeIngredient.ingredientId()).orElseThrow(() -> new ResourceNotFoundException("Ingredient with id: " + recipeIngredient.ingredientId() + " does not found!"));

            RecipeIngredient newRecipeIngredient = new RecipeIngredient();
            newRecipeIngredient.setRecipe(newRecipe);
            newRecipeIngredient.setIngredient(ingredient);
            newRecipeIngredient.setAmount(recipeIngredient.amount());
            newRecipeIngredient.setUnit(recipeIngredient.unit());

            if (newRecipeIngredient.getAmount() <= 0) {
                throw new ValidationException("Ingredient amount must be greater than zero.");
            }

            recipeIngredientRepository.save(newRecipeIngredient);
        }
        logger.info("Recipe added successfully: {}", recipe);
        return recipe;
    }

    @Transactional
    public RecipeDto updateRecipe(RecipeDto recipeDto) {
        if (recipeDto.servings() <= 0) {
            throw new BadRequestException("Servings must be greater than zero.");
        }

        if (recipeDto.ingredients() == null || recipeDto.ingredients().isEmpty()) {
            throw new ValidationException("Recipe must have at least one ingredient.");
        }

        logger.info("Request to update recipe with id {}: {}", recipeDto.id(), recipeDto);
        Recipe existingRecipe = recipeRepository.findById(recipeDto.id()).orElseThrow(() -> new ResourceNotFoundException("Recipe with id: " + recipeDto.id() + " does not found!"));

        logger.info("Deleting existing ingredients for recipe id {}", recipeDto.id());
        existingRecipe.getRecipeIngredients().clear();

        if (entityManager != null) {
            entityManager.flush();
            entityManager.clear();
        }

        existingRecipe.setServings(recipeDto.servings());
        existingRecipe.setVegetarian(recipeDto.isVegetarian());
        existingRecipe.setName(recipeDto.name());
        existingRecipe.setInstructions(recipeDto.instructions());
        recipeRepository.update(existingRecipe);

        for (var recipeIngredient : recipeDto.ingredients()) {
            Ingredient ingredient = ingredientRepository.findById(recipeIngredient.ingredientId()).orElseThrow(() -> new ResourceNotFoundException("Ingredient with id: " + recipeIngredient.ingredientId() + " does not found!"));

            RecipeIngredient newRecipeIngredient = new RecipeIngredient();
            newRecipeIngredient.setRecipe(existingRecipe);
            newRecipeIngredient.setIngredient(ingredient);
            newRecipeIngredient.setAmount(recipeIngredient.amount());
            newRecipeIngredient.setUnit(recipeIngredient.unit());

            if (newRecipeIngredient.getAmount() <= 0) {
                throw new ValidationException("Ingredient amount must be greater than zero.");
            }

            recipeIngredientRepository.save(newRecipeIngredient);
        }
        logger.info("Recipe updated successfully: {}", recipeDto);
        return recipeDto;
    }

    @Transactional
    public void deleteRecipe(Integer id) {
        logger.info("Request to delete recipe with id {}", id);
        if (recipeRepository.notExistsById(id))
            throw new ResourceNotFoundException("Recipe with id " + id + " not found");
        recipeRepository.deleteById(id);
        logger.info("Recipe with id {} deleted successfully", id);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> getAllRecipes() {
        logger.info("Request to get all recipes");
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeDto> recipeDto = recipes.stream()
                .map(this::convertToRecipeDto)
                .toList();
        logger.info("Retrieved recipes: {}", recipeDto);
        return recipeDto;
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> searchRecipes(Boolean vegetarian, Integer servings, List<Integer> includeIngredients, List<Integer> excludeIngredients, String instruction) {
        logger.info("Request to search recipes with parameters - vegetarian: {}, servings: {}, includeIngredients: {}, excludeIngredients: {}, instruction: {}",
                vegetarian, servings, includeIngredients, excludeIngredients, instruction);
        List<Recipe> recipes = recipeRepository.findAll();
        if (vegetarian != null) {
            recipes = recipes.stream().filter(r -> r.getVegetarian() == vegetarian).toList();
        }
        if (servings != null) {
            recipes = recipes.stream().filter(r -> r.getServings() == servings).toList();
        }
        if (includeIngredients != null && !includeIngredients.isEmpty()) {
            recipes = recipes.stream().filter(r ->
                    includeIngredients.stream().allMatch(ingredient ->
                            r.getRecipeIngredients().stream()
                                    .anyMatch(ri -> ri.getIngredient().getId().equals(ingredient))
                    )
            ).toList();
        }
        if (excludeIngredients != null && !excludeIngredients.isEmpty()) {
            recipes = recipes.stream().filter(r ->
                    excludeIngredients.stream().noneMatch(ingredient ->
                            r.getRecipeIngredients().stream()
                                    .anyMatch(ri -> ri.getIngredient().getId().equals(ingredient))
                    )
            ).toList();
        }
        if (instruction != null && !instruction.isEmpty()) {
            recipes = recipes.stream().filter(r -> r.getInstructions().toLowerCase().contains(instruction.toLowerCase())).toList();
        }
        List<RecipeDto> recipeDtos = recipes.stream().map(this::convertToRecipeDto).toList();
        logger.info("Retrieved recipes: {}", recipeDtos);
        return recipeDtos;
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> findByVegetarianRecipes(boolean isVegetarian) {
        logger.info("Request to find recipes by vegetarian status: {}", isVegetarian);
        List<RecipeDto> recipes = recipeRepository.findByVegetarian(isVegetarian).stream().map(this::convertToRecipeDto)
                .toList();
        logger.info("Retrieved vegetarian recipes: {}", recipes);
        return recipes;
    }

    @Transactional(readOnly = true)
    public RecipeDto getRecipeByName(String name) {
        logger.info("Request to find recipes by name {}", name);
        Recipe recipe = recipeRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Recipe with name: " + name + " does not found!"));
        logger.info("Retrieved recipe: {}", recipe);
        return convertToRecipeDto(recipe);
    }

    @Transactional(readOnly = true)
    public RecipeDto getRecipeById(Integer id) {
        logger.info("Request to find recipes by id {}", id);
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe with id: " + id + " does not found!"));
        logger.info("Retrieved recipe: {}", recipe);
        return convertToRecipeDto(recipe);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> findByServings(int servings) {
        logger.info("Request to find recipes by servings: {}", servings);
        List<RecipeDto> recipes = recipeRepository.findByServings(servings).stream().map(this::convertToRecipeDto)
                .toList();
        logger.info("Retrieved recipes: {}", recipes);
        return recipes;
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> findByInstructionsContaining(String text) {
        logger.info("Request to find recipes by instructions containing: {}", text);
        List<RecipeDto> recipes = recipeRepository.findByInstructionsContaining(text).stream().map(this::convertToRecipeDto)
                .toList();
        logger.info("Retrieved recipes: {}", recipes);
        return recipes;
    }

    private RecipeDto convertToRecipeDto(Recipe recipe) {
        return new RecipeDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getInstructions(),
                recipe.getVegetarian(),
                recipe.getServings(),
                recipe.getRecipeIngredients() != null ?
                        recipe.getRecipeIngredients().stream()
                                .map(this::convertToRecipeIngredientDto)
                                .toList() : Collections.emptyList()
        );
    }

    private RecipeIngredientDto convertToRecipeIngredientDto(RecipeIngredient ingredient) {
        return new RecipeIngredientDto(
                ingredient.getAmount(),
                ingredient.getUnit(),
                ingredient.getIngredient().getId()
        );
    }
}