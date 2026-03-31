CREATE TABLE ingredient (
                            id          BIGSERIAL PRIMARY KEY,
                            name        VARCHAR(100) NOT NULL,
                            category    VARCHAR(50)  NOT NULL,
                            price       NUMERIC(10,2) NOT NULL
);

CREATE TABLE dish (
                      id              BIGSERIAL PRIMARY KEY,
                      name            VARCHAR(100) NOT NULL,
                      selling_price   NUMERIC(10,2) NOT NULL
);

CREATE TABLE dish_ingredient (
                                 dish_id       BIGINT NOT NULL,
                                 ingredient_id BIGINT NOT NULL,
                                 PRIMARY KEY (dish_id, ingredient_id),
                                 FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE,
                                 FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

CREATE TABLE stock_movement (
                                id              BIGSERIAL PRIMARY KEY,
                                ingredient_id   BIGINT NOT NULL,
                                movement_at     TIMESTAMP NOT NULL,
                                unit            VARCHAR(10) NOT NULL,   -- PCS, KG, L
                                quantity        NUMERIC(10,3) NOT NULL,
                                FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);

INSERT INTO ingredient (name, category, price) VALUES
                                                   ('Tomate', 'Légume', 1.50),
                                                   ('Pâtes', 'Féculent', 2.20),
                                                   ('Boeuf haché', 'Viande', 8.90),
                                                   ('Oignon', 'Légume', 0.80),
                                                   ('Fromage râpé', 'Produit laitier', 4.50);

INSERT INTO dish (name, selling_price) VALUES
                                           ('Spaghetti Bolognese', 12.50),
                                           ('Pizza Margherita', 10.00);

INSERT INTO dish_ingredient (dish_id, ingredient_id) VALUES
                                                         (1, 1), (1, 2), (1, 3), (1, 4),   -- Spaghetti
                                                         (2, 1), (2, 5);                   -- Pizza

INSERT INTO stock_movement (ingredient_id, movement_at, unit, quantity) VALUES
                                                                            (1, '2025-03-20 08:00:00', 'KG', 50.0),
                                                                            (1, '2025-03-25 10:00:00', 'KG', -10.0),
                                                                            (2, '2025-03-22 09:00:00', 'KG', 100.0),
                                                                            (3, '2025-03-24 14:00:00', 'KG', 20.0);