package com.abnamro.recipeapp.repository;

import com.abnamro.recipeapp.entity.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Ingredient save(Ingredient ingredient);

    Ingredient update(Ingredient ingredient);

    Optional<Ingredient> findById(Integer id);

    Optional<Ingredient> findByName(String name);

    List<Ingredient> findAll();

    void deleteById(Integer id);

    boolean existsById(Integer id);
}
