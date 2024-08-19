package com.abnamro.recipeapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recipe_ingredients", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"})
})
public class RecipeIngredient {

    @Id
    @Column(name = "recipe_ingredient_id", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeIngredientId;

    @ManyToOne
    @JoinColumn(
            name = "recipe_id",
            foreignKey = @ForeignKey(
                    name = "recipe_ingredient_id_fk"
            )
    )
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(
            name = "ingredient_id",
            foreignKey = @ForeignKey(
                    name = "ingredient_ingredient_id_fk"
            )
    )
    private Ingredient ingredient;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "unit", nullable = false)
    private String unit;

    public RecipeIngredient() {
    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, double amount, String unit) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    public Integer getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(Integer recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
