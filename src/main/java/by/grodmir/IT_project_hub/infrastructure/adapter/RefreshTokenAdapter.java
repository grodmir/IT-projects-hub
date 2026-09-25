package by.grodmir.IT_project_hub.infrastructure.adapter;

import by.grodmir.IT_project_hub.domain.model.RefreshToken;
import by.grodmir.IT_project_hub.domain.repository.RefreshTokenRepository;
import by.grodmir.IT_project_hub.infrastructure.mapper.RefreshTokenMapper;
import by.grodmir.IT_project_hub.infrastructure.repository.RefreshTokenJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenMapper mapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        var entity = mapper.toEntity(refreshToken);
        var saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toModel);
    }

    @Override
    @Transactional
    public void revokeAllByUsername(String username) {
        jpaRepository.deleteAllByUsername(username);
    }
}
