package hei.td5spring.repository;

import hei.td5spring.entity.Ingredient;
import hei.td5spring.entity.Stock;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepository {

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredient";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapIngredient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients", e);
        }
        return list;
    }

    public Optional<Ingredient> findById(Long id) {
        String sql = "SELECT * FROM ingredient WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapIngredient(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'ingrédient id=" + id, e);
        }
        return Optional.empty();
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM ingredient WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur vérification existence ingrédient id=" + id, e);
        }
        return false;
    }

    public Optional<Stock> getStock(Long id, String at, String unit) {
        String sql = """
                SELECT COALESCE(SUM(quantity), 0) AS value
                FROM stock_movement
                WHERE ingredient_id = ?
                  AND movement_at <= ?
                  AND unit = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String cleanedAt = at.replace("T", " ").replace("Z", "");
            ps.setLong(1, id);
            ps.setTimestamp(2, Timestamp.valueOf(cleanedAt));
            ps.setString(3, unit.toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double value = rs.getDouble("value");
                    return Optional.of(new Stock(unit.toUpperCase(), value));
                }
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private Ingredient mapIngredient(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price")
        );
    }
}