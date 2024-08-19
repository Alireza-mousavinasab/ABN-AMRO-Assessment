CREATE TABLE recipe_ingredients
(
    recipe_ingredient_id SERIAL PRIMARY KEY,
    recipe_id            INTEGER          NOT NULL,
    ingredient_id        INTEGER          NOT NULL,
    amount               DOUBLE PRECISION NOT NULL,
    unit                 VARCHAR(255)     NOT NULL,
    CONSTRAINT fk_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (recipe_id) ON DELETE CASCADE,
    CONSTRAINT fk_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient (ingredient_id) ON DELETE CASCADE
);
