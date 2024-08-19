package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.RecipeIngredient;
import com.abnamro.recipeapp.repository.RecipeIngredientRepository;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class JpaRecipeIngredientRepositoryService implements RecipeIngredientRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaRecipeIngredientRepositoryService.class);

    private final JpaRecipeIngredientRepository jpaRecipeIngredientRepository;

    public JpaRecipeIngredientRepositoryService(JpaRecipeIngredientRepository jpaRecipeIngredientRepository) {
        this.jpaRecipeIngredientRepository = jpaRecipeIngredientRepository;
    }

    @Override
    public void save(RecipeIngredient recipeIngredient) {
        logger.info("Saving recipe ingredient: {}", recipeIngredient);
        jpaRecipeIngredientRepository.save(recipeIngredient);
        logger.info("Recipe ingredient saved: {}", recipeIngredient);
    }
}
