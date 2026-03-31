package hei.td5spring.controller;


import hei.td5spring.entity.Dish;
import hei.td5spring.entity.Ingredient;
import hei.td5spring.repository.DishRepository;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishRepository repository;

    public DishController(DishRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return repository.findAll();
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Object> updateDishIngredients(
            @PathVariable Long id,
            @RequestBody(required = false) @Nullable List<Ingredient> ingredients) {

        if (ingredients == null) {
            return ResponseEntity.badRequest()
                    .body("Le corps de la requête est obligatoire.");
        }

        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Dish.id=" + id + " is not found");
        }

        repository.updateIngredients(id, ingredients);
        return ResponseEntity.ok("Ingrédients mis à jour avec succès");
    }
}
