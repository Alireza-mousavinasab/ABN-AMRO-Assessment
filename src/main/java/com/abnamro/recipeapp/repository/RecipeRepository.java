package com.abnamro.recipeapp.repository;

import com.abnamro.recipeapp.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {

    void save(Recipe recipe);

    Optional<Recipe> findById(Integer id);

    boolean notExistsById(Integer id);

    List<Recipe> findAll();

    void deleteById(Integer id);

    void update(Recipe recipe);

    Optional<Recipe> findByName(String name);

    List<Recipe> findByVegetarian(boolean vegetarian);

    List<Recipe> findByServings(int servings);

    List<Recipe> findByInstructionsContaining(String text);
}