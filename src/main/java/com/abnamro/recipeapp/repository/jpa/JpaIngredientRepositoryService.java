package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Ingredient;
import com.abnamro.recipeapp.repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaIngredientRepositoryService implements IngredientRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaIngredientRepositoryService.class);
    private final JpaIngredientRepository jpaIngredientRepository;

    public JpaIngredientRepositoryService(JpaIngredientRepository jpaIngredientRepository) {
        this.jpaIngredientRepository = jpaIngredientRepository;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        logger.info("Saving ingredient: {}", ingredient);
        Ingredient savedIngredient = jpaIngredientRepository.save(ingredient);
        logger.info("Ingredient saved: {}", savedIngredient);
        return savedIngredient;
    }

    @Override
    public Ingredient update(Ingredient ingredient) {
        logger.info("Updating ingredient: {}", ingredient);
        Ingredient updatedIngredient = jpaIngredientRepository.save(ingredient);
        logger.info("Ingredient updated: {}", updatedIngredient);
        return updatedIngredient;
    }

    @Override
    public Optional<Ingredient> findById(Integer id) {
        logger.info("Finding ingredient by id: {}", id);
        Optional<Ingredient> ingredient = jpaIngredientRepository.findById(id);
        logger.info("Ingredient found: {}", ingredient);
        return ingredient;
    }

    @Override
    public Optional<Ingredient> findByName(String name) {
        logger.info("Finding ingredient by name: {}", name);
        Optional<Ingredient> ingredient = jpaIngredientRepository.findByName(name);
        logger.info("Ingredient found: {}", ingredient);
        return ingredient;
    }

    @Override
    public List<Ingredient> findAll() {
        logger.info("Finding all ingredients");
        List<Ingredient> ingredients = jpaIngredientRepository.findAll();
        logger.info("Ingredients found: {}", ingredients);
        return ingredients;
    }

    @Override
    public void deleteById(Integer id) {
        logger.info("Deleting ingredient by id: {}", id);
        jpaIngredientRepository.deleteById(id);
        logger.info("Ingredient with id {} deleted", id);
    }

    @Override
    public boolean existsById(Integer id) {
        logger.info("Checking if ingredient exists by id: {}", id);
        boolean exists = jpaIngredientRepository.existsById(id);
        logger.info("Ingredient exists: {}", exists);
        return exists;
    }
}
