package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaRecipeRepositoryTest {

    @Autowired
    private JpaRecipeRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void findByVegetarian() {
        // given
        Recipe vegetarianRecipe = new Recipe();
        vegetarianRecipe.setName("Vegetable Soup");
        vegetarianRecipe.setVegetarian(true);
        vegetarianRecipe.setServings(4);
        vegetarianRecipe.setInstructions("Cook vegetables in water.");
        underTest.save(vegetarianRecipe);

        Recipe nonVegetarianRecipe = new Recipe();
        nonVegetarianRecipe.setName("Chicken Soup");
        nonVegetarianRecipe.setVegetarian(false);
        nonVegetarianRecipe.setServings(4);
        nonVegetarianRecipe.setInstructions("Cook chicken in water.");
        underTest.save(nonVegetarianRecipe);

        // when
        List<Recipe> vegetarianRecipes = underTest.findByVegetarian(true);
        List<Recipe> nonVegetarianRecipes = underTest.findByVegetarian(false);

        // then
        assertThat(vegetarianRecipes).hasSize(1);
        assertThat(vegetarianRecipes.getFirst().getName()).isEqualTo("Vegetable Soup");

        assertThat(nonVegetarianRecipes).hasSize(1);
        assertThat(nonVegetarianRecipes.getFirst().getName()).isEqualTo("Chicken Soup");
    }

    @Test
    void findByServings() {
        // given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe One");
        recipe1.setVegetarian(true);
        recipe1.setServings(4);
        recipe1.setInstructions("Instructions for recipe one.");
        underTest.save(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe Two");
        recipe2.setVegetarian(true);
        recipe2.setServings(4);
        recipe2.setInstructions("Instructions for recipe two.");
        underTest.save(recipe2);

        // when
        List<Recipe> recipesWithFourServings = underTest.findByServings(4);
        List<Recipe> recipesWithFiveServings = underTest.findByServings(5);

        // then
        assertThat(recipesWithFourServings).hasSize(2);
        assertThat(recipesWithFourServings).extracting(Recipe::getName)
                .containsExactlyInAnyOrder("Recipe One", "Recipe Two");

        assertThat(recipesWithFiveServings).isEmpty();
    }

    @Test
    void findByInstructionsContainingIgnoreCase() {
        // given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe One");
        recipe1.setVegetarian(true);
        recipe1.setServings(4);
        recipe1.setInstructions("Instructions for spicy recipe.");
        underTest.save(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe Two");
        recipe2.setVegetarian(true);
        recipe2.setServings(4);
        recipe2.setInstructions("Instructions for mild recipe.");
        underTest.save(recipe2);

        // when
        List<Recipe> recipesWithSpicy = underTest.findByInstructionsContainingIgnoreCase("spicy");
        List<Recipe> recipesWithMild = underTest.findByInstructionsContainingIgnoreCase("mild");

        // then
        assertThat(recipesWithSpicy).hasSize(1);
        assertThat(recipesWithSpicy.getFirst().getName()).isEqualTo("Recipe One");

        assertThat(recipesWithMild).hasSize(1);
        assertThat(recipesWithMild.getFirst().getName()).isEqualTo("Recipe Two");
    }

    @Test
    void findByName() {
        // given
        Recipe recipe = new Recipe();
        recipe.setName("Unique Recipe");
        recipe.setVegetarian(true);
        recipe.setServings(4);
        recipe.setInstructions("Instructions for unique recipe.");
        underTest.save(recipe);

        // when
        Optional<Recipe> foundRecipe = underTest.findByName("Unique Recipe");

        // then
        assertThat(foundRecipe).isPresent();
        assertThat(foundRecipe.get().getName()).isEqualTo("Unique Recipe");
    }
}