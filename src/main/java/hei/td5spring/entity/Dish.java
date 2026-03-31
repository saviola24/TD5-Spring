package hei.td5spring.entity;

import java.util.List;

public record Dish (
        Long id,
        String name,
        double sellingPrice,
        List<Ingredient> ingredients
){}
