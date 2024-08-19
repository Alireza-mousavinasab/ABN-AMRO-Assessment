CREATE TABLE recipe
(
    recipe_id     SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    instructions  TEXT         NOT NULL,
    is_vegetarian BOOLEAN,
    servings      INTEGER
);
