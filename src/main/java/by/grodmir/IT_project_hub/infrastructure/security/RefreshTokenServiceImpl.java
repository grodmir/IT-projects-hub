package by.grodmir.IT_project_hub.infrastructure.security;

import by.grodmir.IT_project_hub.domain.model.RefreshToken;
import by.grodmir.IT_project_hub.domain.repository.RefreshTokenRepository;
import by.grodmir.IT_project_hub.domain.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpirationMillis;

    @Override
    public RefreshToken issue(String username) {
        RefreshToken token = new RefreshToken(
                null,
                UUID.randomUUID().toString(),
                username,
                Instant.now().plus(refreshExpirationMillis, ChronoUnit.MILLIS),
                false
        );
        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken rotate(String oldToken) {
        RefreshToken current = refreshTokenRepository.findByToken(oldToken)
                .filter(RefreshToken::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is invalid or expired"));

        refreshTokenRepository.save(new RefreshToken(
                current.id(), current.token(), current.username(), current.expiresAt(), true
        ));

        return issue(current.username());
    }

    @Override
    public void revoke(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(current ->
                refreshTokenRepository.save(new RefreshToken(
                        current.id(), current.token(), current.username(), current.expiresAt(), true
                ))
        );
    }
}
