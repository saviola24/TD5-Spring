CREATE TABLE IF NOT EXISTS ingredient (
                                          id          BIGSERIAL PRIMARY KEY,
                                          name        VARCHAR(100) NOT NULL,
                                          category    VARCHAR(50)  NOT NULL,
                                          price       NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS dish (
                                    id              BIGSERIAL PRIMARY KEY,
                                    name            VARCHAR(100) NOT NULL,
                                    selling_price   NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS dish_ingredient (
                                               dish_id       BIGINT NOT NULL,
                                               ingredient_id BIGINT NOT NULL,
                                               PRIMARY KEY (dish_id, ingredient_id),
                                               FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE,
                                               FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS stock_movement (
                                              id              BIGSERIAL PRIMARY KEY,
                                              ingredient_id   BIGINT NOT NULL,
                                              movement_at     TIMESTAMP NOT NULL,
                                              unit            VARCHAR(10) NOT NULL,
                                              quantity        NUMERIC(10,3) NOT NULL,
                                              type            VARCHAR(50) NOT NULL,
                                              FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

INSERT INTO ingredient (name, category, price)
SELECT 'Tomate', 'Légume', 1.50 WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = 'Tomate');
INSERT INTO ingredient (name, category, price)
SELECT 'Pâtes', 'Féculent', 2.20 WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = 'Pâtes');
INSERT INTO ingredient (name, category, price)
SELECT 'Boeuf haché', 'Viande', 8.90 WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = 'Boeuf haché');
INSERT INTO ingredient (name, category, price)
SELECT 'Oignon', 'Légume', 0.80 WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = 'Oignon');
INSERT INTO ingredient (name, category, price)
SELECT 'Fromage râpé', 'Produit laitier', 4.50 WHERE NOT EXISTS (SELECT 1 FROM ingredient WHERE name = 'Fromage râpé');

INSERT INTO dish (name, selling_price)
SELECT 'Spaghetti Bolognese', 12.50 WHERE NOT EXISTS (SELECT 1 FROM dish WHERE name = 'Spaghetti Bolognese');
INSERT INTO dish (name, selling_price)
SELECT 'Pizza Margherita', 10.00 WHERE NOT EXISTS (SELECT 1 FROM dish WHERE name = 'Pizza Margherita');