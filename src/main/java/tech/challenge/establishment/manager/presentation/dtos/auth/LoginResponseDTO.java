package tech.challenge.establishment.manager.presentation.dtos.auth;

public record LoginResponseDTO(
        String accessToken,
        String tokenType
) {
    public static LoginResponseDTO of(String accessToken) {
        return new LoginResponseDTO(accessToken, "Bearer");
    }
}