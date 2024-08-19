package com.abnamro.recipeapp.controller;

import com.abnamro.recipeapp.dto.ingredient.CreateIngredientDto;
import com.abnamro.recipeapp.dto.ingredient.IngredientDto;
import com.abnamro.recipeapp.service.IngredientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredients")
@Tag(name = "Ingredients", description = "Operations related to ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private static final Logger logger = LoggerFactory.getLogger(IngredientController.class);

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<CreateIngredientDto> createIngredient(@RequestBody CreateIngredientDto ingredient) {
        logger.info("Request to create ingredient: {}", ingredient);
        CreateIngredientDto createdIngredient = ingredientService.addIngredient(ingredient);
        logger.info("Ingredient created successfully: {}", createdIngredient);
        return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(@RequestBody IngredientDto ingredient) {
        logger.info("Request to update ingredient with id {}: {}", ingredient.id(), ingredient);
        IngredientDto updatedIngredient = ingredientService.updateIngredient(ingredient);
        logger.info("Ingredient updated successfully: {}", updatedIngredient);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Integer id) {
        logger.info("Request to delete ingredient with id {}", id);
        ingredientService.deleteById(id);
        logger.info("Ingredient with id {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Integer id) {
        logger.info("Request to get ingredient by id {}", id);
        IngredientDto ingredient = ingredientService.getIngredientById(id);
        logger.info("Retrieved ingredient: {}", ingredient);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<IngredientDto> getIngredientByName(@PathVariable String name) {
        logger.info("Request to get ingredient by name {}", name);
        IngredientDto ingredient = ingredientService.getIngredientByName(name);
        logger.info("Retrieved ingredients: {}", ingredient);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        logger.info("Request to get all ingredients");
        List<IngredientDto> ingredients = ingredientService.getAllIngredients();
        logger.info("Retrieved ingredients: {}", ingredients);
        return ResponseEntity.ok(ingredients);
    }
}
