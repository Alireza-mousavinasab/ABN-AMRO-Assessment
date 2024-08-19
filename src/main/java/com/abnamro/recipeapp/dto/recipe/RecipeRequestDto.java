package com.abnamro.recipeapp.dto.recipe;

import com.abnamro.recipeapp.dto.RecipeIngredientDto;

import java.util.List;

public record RecipeRequestDto(
        CreateRecipeDto recipe,
        List<RecipeIngredientDto> recipeIngredients
) {}