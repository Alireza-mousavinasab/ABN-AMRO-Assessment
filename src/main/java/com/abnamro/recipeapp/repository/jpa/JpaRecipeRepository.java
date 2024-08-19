package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaRecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByVegetarian(boolean vegetarian);

    List<Recipe> findByServings(int servings);

    List<Recipe> findByInstructionsContainingIgnoreCase(String text);

    @Query("SELECT i FROM Recipe i WHERE i.name = :name")
    Optional<Recipe> findByName(@Param("name") String name);
}
