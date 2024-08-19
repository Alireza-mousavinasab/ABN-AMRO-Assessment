package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {
}