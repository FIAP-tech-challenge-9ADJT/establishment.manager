package tech.challenge.establishment.manager.domain.valueobjects;

public record Price(double value) {
    public Price {
        if (value < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public static Price of(double value) {
        return new Price(value);
    }
}
