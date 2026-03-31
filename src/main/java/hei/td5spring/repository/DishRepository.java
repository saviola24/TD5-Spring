package hei.td5spring.repository;

import hei.td5spring.entity.StockMovement;
import hei.td5spring.entity.StockMovementCreate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockMovementRepository {

    public List<StockMovement> findByIngredientIdAndDateRange(Long ingredientId, Instant from, Instant to) {
        List<StockMovement> list = new ArrayList<>();
        String sql = """
                SELECT id, movement_at, unit, quantity, type
                FROM stock_movement
                WHERE ingredient_id = ?
                  AND movement_at >= ?
                  AND movement_at <= ?
                ORDER BY movement_at
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ingredientId);
            ps.setTimestamp(2, Timestamp.from(from));
            ps.setTimestamp(3, Timestamp.from(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMovement(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des mouvements de stock", e);
        }
        return list;
    }

    public List<StockMovement> saveAll(Long ingredientId, List<StockMovementCreate> movements) {
        String insertSql = """
                INSERT INTO stock_movement (ingredient_id, movement_at, unit, quantity, type)
                VALUES (?, NOW(), ?, ?, ?)
                RETURNING id, movement_at, unit, quantity, type
                """;

        List<StockMovement> saved = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            for (StockMovementCreate m : movements) {
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setLong(1, ingredientId);
                    ps.setString(2, m.unit().toUpperCase());
                    ps.setDouble(3, m.value());
                    ps.setString(4, m.type());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            saved.add(mapMovement(rs));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement des mouvements de stock", e);
        }
        return saved;
    }

    private StockMovement mapMovement(ResultSet rs) throws SQLException {
        return new StockMovement(
                rs.getLong("id"),
                rs.getTimestamp("movement_at").toInstant(),
                rs.getString("unit"),
                rs.getDouble("quantity"),
                rs.getString("type")
        );
    }
}