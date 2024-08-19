package com.abnamro.recipeapp.repository.jpa;

import com.abnamro.recipeapp.entity.Ingredient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaIngredientRepositoryTest {

    @Autowired
    private JpaIngredientRepository underTest;

    @Test
    void findByName() {
        // given
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Mayo");
        underTest.save(ingredient);

        // when
        Optional<Ingredient> foundIngredient = underTest.findByName("Mayo");

        // then
        assertThat(foundIngredient).isPresent();
        assertThat(foundIngredient.get().getName()).isEqualTo("Mayo");
    }
}