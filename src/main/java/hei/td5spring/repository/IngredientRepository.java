package hei.td5spring.repository;

import hei.td5spring.entity.Ingredient;
import hei.td5spring.entity.Stock;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class IngredientRepository {
    private final JdbcTemplate jdbc;

    public IngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Ingredient> ingredientRowMapper = (rs, rowNum) ->
            new Ingredient(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price")
            );

    public List<Ingredient> findAll() {
        return jdbc.query("SELECT * FROM ingredient", ingredientRowMapper);
    }

    public Optional<Ingredient> findById(Long id) {
        return jdbc.query("SELECT * FROM ingredient WHERE id = ?", ingredientRowMapper, id)
                .stream().findFirst();
    }

    public Optional<Stock> getStock(Long id, String at, String unit) {
        // Logique métier exacte des TD JDBC précédents (somme des mouvements jusqu'à la date)
        String sql = """
            SELECT COALESCE(SUM(quantity), 0) AS value 
            FROM stock_movement 
            WHERE ingredient_id = ? 
              AND movement_at <= ? 
              AND unit = ?
            """;

        try {
            Timestamp timestamp = Timestamp.valueOf(at);
            Double value = jdbc.queryForObject(sql, Double.class, id, timestamp, unit);
            return Optional.of(new Stock(unit, value != null ? value : 0.0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
