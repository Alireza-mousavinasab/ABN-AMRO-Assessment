ALTER TABLE ingredient
    ADD CONSTRAINT unique_ingredient_name UNIQUE (name);

ALTER TABLE recipe_ingredients
    ADD CONSTRAINT unique_recipe_ingredient UNIQUE (recipe_id, ingredient_id);
