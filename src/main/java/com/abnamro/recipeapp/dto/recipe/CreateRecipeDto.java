package com.abnamro.recipeapp.dto.recipe;

public record CreateRecipeDto(
        String name,
        String instructions,
        boolean isVegetarian,
        int servings
) {}
