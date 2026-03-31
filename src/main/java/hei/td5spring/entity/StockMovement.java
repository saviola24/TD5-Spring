package hei.td5spring.entity;

import java.time.Instant;

public record StockMovement (
        String id,
        Instant createdAt,
        String unit,
        Double value,
        String type
){}
