package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Recipe;
import com.abnamro.recipeapp.repository.RecipeRepository;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaRecipeRepositoryService implements RecipeRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaRecipeRepositoryService.class);

    private final JpaRecipeRepository jpaRecipeRepository;

    public JpaRecipeRepositoryService(JpaRecipeRepository jpaRecipeRepository) {
        this.jpaRecipeRepository = jpaRecipeRepository;
    }

    @Override
    public void save(Recipe recipe) {
        logger.info("Saving recipe: {}", recipe);
        jpaRecipeRepository.save(recipe);
        logger.info("Recipe saved: {}", recipe);
    }

    @Override
    public Optional<Recipe> findById(Integer id) {
        logger.info("Finding recipe by id: {}", id);
        Optional<Recipe> recipe = jpaRecipeRepository.findById(id);
        logger.info("Recipe found: {}", recipe);
        return recipe;
    }

    @Override
    public boolean notExistsById(Integer id) {
        logger.info("Checking if recipe does not exist by id: {}", id);
        boolean notExists = !jpaRecipeRepository.existsById(id);
        logger.info("Recipe does not exist: {}", notExists);
        return notExists;
    }

    @Override
    public List<Recipe> findAll() {
        logger.info("Finding all recipes");
        List<Recipe> recipes = jpaRecipeRepository.findAll();
        logger.info("Recipes found: {}", recipes);
        return recipes;
    }

    @Override
    public void deleteById(Integer id) {
        logger.info("Deleting recipe by id: {}", id);
        jpaRecipeRepository.deleteById(id);
        logger.info("Recipe with id {} deleted", id);
    }

    @Override
    public void update(Recipe recipe) {
        logger.info("Updating recipe: {}", recipe);
        jpaRecipeRepository.save(recipe);
        logger.info("Recipe updated: {}", recipe);
    }

    @Override
    public List<Recipe> findByVegetarian(boolean vegetarian) {
        logger.info("Finding recipes by vegetarian status: {}", vegetarian);
        List<Recipe> recipes = jpaRecipeRepository.findByVegetarian(vegetarian);
        logger.info("Recipes found: {}", recipes);
        return recipes;
    }

    @Override
    public List<Recipe> findByServings(int servings) {
        logger.info("Finding recipes by servings: {}", servings);
        List<Recipe> recipes = jpaRecipeRepository.findByServings(servings);
        logger.info("Recipes found: {}", recipes);
        return recipes;
    }

    @Override
    public Optional<Recipe> findByName(String name) {
        logger.info("Finding recipe by name: {}", name);
        Optional<Recipe> recipe = jpaRecipeRepository.findByName(name);
        logger.info("Recipe found: {}", recipe);
        return recipe;
    }

    @Override
    public List<Recipe> findByInstructionsContaining(String text) {
        logger.info("Finding recipes by instructions containing: {}", text);
        List<Recipe> recipes = jpaRecipeRepository.findByInstructionsContainingIgnoreCase(text);
        logger.info("Recipes found: {}", recipes);
        return recipes;
    }
}
