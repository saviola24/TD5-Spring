package hei.td5spring.controller;

import hei.td5spring.entity.StockMovement;
import hei.td5spring.entity.StockMovementCreate;
import hei.td5spring.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Repository
@RestController
@RequestMapping("/ingredients/{id}/stockMovements")
public class IngredientController {
    private final StockMovementRepository repository;

    public IngredientController(StockMovementRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StockMovement> getMovements(
            @PathVariable String id,
            @RequestParam Instant from,
            @RequestParam Instant to) {

        if (!ingredientExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient.id=" + id + " is not found");
        }
        return repository.findByIngredientIdAndDateRange(id, from, to);
    }

    @PostMapping
    public List<StockMovement> createMovements(
            @PathVariable String id,
            @RequestBody List<StockMovementCreate> movements) {

        if (!ingredientExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient.id=" + id + " is not found");
        }
        return repository.saveAll(id, movements);
    }

    private boolean ingredientExists(String id) {
        return true;
    }
}