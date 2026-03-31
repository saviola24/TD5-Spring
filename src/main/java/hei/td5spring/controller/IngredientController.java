package hei.td5spring.controller;

import hei.td5spring.entity.StockMovement;
import hei.td5spring.entity.StockMovementCreate;
import hei.td5spring.repository.IngredientRepository;
import hei.td5spring.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients/{id}/stockMovements")
public class IngredientController {

    private final StockMovementRepository stockMovementRepository;
    private final IngredientRepository ingredientRepository;

    public IngredientController(StockMovementRepository stockMovementRepository,
                                IngredientRepository ingredientRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.ingredientRepository    = ingredientRepository;
    }

    @GetMapping
    public List<StockMovement> getMovements(
            @PathVariable Long id,
            @RequestParam Instant from,
            @RequestParam Instant to) {

        if (!ingredientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient.id=" + id + " is not found");
        }
        return stockMovementRepository.findByIngredientIdAndDateRange(id, from, to);
    }

    @PostMapping
    public List<StockMovement> createMovements(
            @PathVariable Long id,
            @RequestBody List<StockMovementCreate> movements) {

        if (!ingredientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient.id=" + id + " is not found");
        }
        return stockMovementRepository.saveAll(id, movements);
    }
}