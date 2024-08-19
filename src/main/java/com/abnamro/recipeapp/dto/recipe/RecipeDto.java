package com.abnamro.recipeapp.dto.recipe;

import com.abnamro.recipeapp.dto.RecipeIngredientDto;

import java.util.List;

public record RecipeDto(
        int id,
        String name,
        String instructions,
        boolean isVegetarian,
        int servings,
        List<RecipeIngredientDto> ingredients
) {}