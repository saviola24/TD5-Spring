package hei.td5spring.repository;

import hei.td5spring.entity.StockMovement;
import hei.td5spring.entity.StockMovementCreate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface StockMovementRepository {
    List<StockMovement> findByIngredientIdAndDateRange(String ingredientId, Instant from, Instant to);

    List<StockMovement> saveAll(String ingredientId, List<StockMovementCreate> movements);
}

