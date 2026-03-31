package hei.td5spring.repository;


import org.springframework.stereotype.Repository;
import hei.td5spring.entity.Dish;
import hei.td5spring.entity.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public class DishRepository {
    private final JdbcTemplate jdbc;
    private final IngredientRepository ingredientRepository;

    public DishRepository(JdbcTemplate jdbc, IngredientRepository ingredientRepository) {
        this.jdbc = jdbc;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Dish> findAll() {
        List<Dish> dishes = jdbc.query("SELECT * FROM dish", (rs, rowNum) ->
                new Dish(rs.getLong("id"), rs.getString("name"), rs.getDouble("selling_price"), List.of()));

        for (Dish dish : dishes) {
            List<Ingredient> ingredients = jdbc.query(
                    "SELECT i.* FROM ingredient i JOIN dish_ingredient di ON i.id = di.ingredient_id WHERE di.dish_id = ?",
                    ingredientRepository::ingredientRowMapper,
                    dish.id()
            );

        }
        return dishes.stream().map(d -> {
            List<Ingredient> ing = jdbc.query(
                    "SELECT i.* FROM ingredient i JOIN dish_ingredient di ON i.id = di.ingredient_id WHERE di.dish_id = ?",
                    ingredientRepository::ingredientRowMapper,
                    d.id()
            );
            return new Dish(d.id(), d.name(), d.sellingPrice(), ing);
        }).toList();
    }

    public Optional<Dish> findById(Long id) {
        return findAll().stream().filter(d -> d.id().equals(id)).findFirst();
    }

    public void updateIngredients(Long dishId, List<Ingredient> requestedIngredients) {
        jdbc.update("DELETE FROM dish_ingredient WHERE dish_id = ?", dishId);

        if (requestedIngredients == null || requestedIngredients.isEmpty()) return;

        List<Long> ids = requestedIngredients.stream()
                .map(Ingredient::id)
                .distinct()
                .toList();

        List<Long> existingIds = jdbc.query(
                "SELECT id FROM ingredient WHERE id IN (" +
                        ids.stream().map(String::valueOf).reduce((a,b)->a+","+b).orElse("0") + ")",
                (rs, rowNum) -> rs.getLong("id")
        );

        for (Long ingId : existingIds) {
            jdbc.update("INSERT INTO dish_ingredient (dish_id, ingredient_id) VALUES (?, ?)", dishId, ingId);
        }
    }

}
