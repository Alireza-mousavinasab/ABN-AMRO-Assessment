package com.abnamro.recipeapp.controller;

import com.abnamro.recipeapp.dto.recipe.CreateRecipeDto;
import com.abnamro.recipeapp.dto.recipe.RecipeDto;
import com.abnamro.recipeapp.dto.recipe.RecipeRequestDto;
import com.abnamro.recipeapp.service.RecipeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipes", description = "Operations related to recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<CreateRecipeDto> addRecipe(@RequestBody RecipeRequestDto recipeRequest) {
        logger.info("Request to add recipe: {}", recipeRequest);
        CreateRecipeDto createdRecipe = recipeService.addRecipe(recipeRequest);
        logger.info("Recipe created successfully: {}", createdRecipe);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@RequestBody RecipeDto recipe) {
        logger.info("Request to update recipe with id {}: {}", recipe.id(), recipe);
        RecipeDto updatedRecipe = recipeService.updateRecipe(recipe);
        logger.info("Recipe updated successfully: {}", updatedRecipe);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id) {
        logger.info("Request to delete recipe with id {}", id);
        recipeService.deleteRecipe(id);
        logger.info("Recipe with id {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{name}")
    public RecipeDto getRecipeByName(@PathVariable String name) {
        logger.info("Request to get recipe by name {}", name);
        RecipeDto recipe = recipeService.getRecipeByName(name);
        logger.info("Retrieved recipe:{}",recipe);
        return recipe;
    }

    @GetMapping("/{id}")
    public RecipeDto getRecipeById(@PathVariable Integer id) {
        logger.info("Request to get recipe by id {}", id);
        RecipeDto recipe = recipeService.getRecipeById(id);
        logger.info("Retrieved recipe:{}",recipe);
        return recipe;
    }

    @GetMapping("/vegetarian")
    public List<RecipeDto> getVegetarianRecipes() {
        logger.info("Request to get vegetarian recipes");
        List<RecipeDto> recipes = recipeService.findByVegetarianRecipes(true);
        logger.info("Retrieved vegetarian recipes: {}", recipes);
        return recipes;
    }

    @GetMapping("/non-vegetarian")
    public List<RecipeDto> getNonVegetarianRecipes() {
        logger.info("Request to get non-vegetarian recipes");
        List<RecipeDto> recipes = recipeService.findByVegetarianRecipes(false);
        logger.info("Retrieved non-vegetarian recipes: {}", recipes);
        return recipes;
    }

    @GetMapping("/servings/{servings}")
    public List<RecipeDto> getRecipesByServings(@PathVariable Integer servings) {
        logger.info("Request to get recipes by servings {}", servings);
        List<RecipeDto> recipes = recipeService.findByServings(servings);
        logger.info("Retrieved recipes: {}", recipes);
        return recipes;
    }

    @GetMapping("/instruction/{text}")
    public List<RecipeDto> getRecipesByInstructions(@PathVariable String text) {
        logger.info("Request to get recipes by instruction containing {}", text);
        List<RecipeDto> recipes = recipeService.findByInstructionsContaining(text);
        logger.info("Retrieved recipes: {}", recipes);
        return recipes;
    }

    @GetMapping
    public List<RecipeDto> getAllRecipes() {
        logger.info("Request to get all recipes");
        List<RecipeDto> recipes = recipeService.getAllRecipes();
        logger.info("Retrieved recipes: {}", recipes);
        return recipes;
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDto>> searchRecipes(
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Integer servings,
            @RequestParam(required = false) List<Integer> includeIngredient,
            @RequestParam(required = false) List<Integer> excludeIngredient,
            @RequestParam(required = false) String instruction) {

        logger.info("Request to search recipes with parameters - vegetarian: {}, servings: {}, includeIngredients: {}, excludeIngredients: {}, instruction: {}",
                vegetarian, servings, includeIngredient, excludeIngredient, instruction);
        List<RecipeDto> recipes = recipeService.searchRecipes(vegetarian, servings, includeIngredient, excludeIngredient, instruction);
        logger.info("Retrieved recipes: {}", recipes);
        return ResponseEntity.ok(recipes);
    }
}