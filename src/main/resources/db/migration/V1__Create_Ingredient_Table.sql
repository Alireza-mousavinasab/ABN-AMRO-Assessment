CREATE TABLE ingredient
(
    ingredient_id SERIAL PRIMARY KEY,
    name          VARCHAR(255) UNIQUE NOT NULL
);
