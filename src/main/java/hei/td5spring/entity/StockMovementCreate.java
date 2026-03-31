package hei.td5spring.entity;

public record StockMovementCreate (
        String unit,
        Double value,
        String type
){}
