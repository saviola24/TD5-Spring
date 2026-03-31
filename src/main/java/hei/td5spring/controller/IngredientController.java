package hei.td5spring.controller;


import hei.td5spring.entity.Ingredient;
import hei.td5spring.repository.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientRepository repository;

    public IngredientController(IngredientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getIngredientById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ingredient -> ResponseEntity.ok().body(ingredient))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Ingredient.id=" + id + " is not found"));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Object> getStock(
            @PathVariable Long id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit) {

        if (at == null || unit == null) {
            return ResponseEntity.badRequest()
                    .body("Either mandatory query parameter `at` or `unit` is not provided.");
        }

        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ingredient.id=" + id + " is not found");
        }

        return repository.getStock(id, at, unit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Ingredient.id=" + id + " is not found"));
    }
}
