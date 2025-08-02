package tech.challenge.establishment.manager.dtos.error;

public record FieldErrorDTO(
        String field,
        Object rejectedValue,
        String message
) {
    public static FieldErrorDTO of(String field, Object rejectedValue, String message) {
        return new FieldErrorDTO(field, rejectedValue, message);
    }
}