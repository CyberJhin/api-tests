package org.example.apitests.service.security;

import jakarta.transaction.Transactional;
import org.example.apitests.model.RefreshToken;
import org.example.apitests.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.example.apitests.jwt.JwtCore;
import java.time.Instant;

@Service
public class RefreshTokenService {

    private JwtCore jwtCore;
    private final RefreshTokenRepository repository;

    @Value("${testing.app.refreshLifetime}")
    private int refreshLifetime; // в миллисекундах

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public RefreshToken createToken(String userUuid) {
        // Удаляем предыдущие токены (опционально)
        repository.deleteByUserUuid(userUuid);

        RefreshToken token = new RefreshToken();
        token.setToken(jwtCore.generateRefreshToken(userUuid));
        token.setUserUuid(userUuid);
        token.setExpiryDate(Instant.now().plusMillis(refreshLifetime));

        return repository.save(token);
    }

    public boolean validate(String token) {
        return repository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(Instant.now()))
                .isPresent();
    }

    public String getUserUuid(String token) {
        return repository.findByToken(token)
                .map(RefreshToken::getUserUuid)
                .orElse(null);
    }

    public void delete(String token) {
        repository.findByToken(token).ifPresent(repository::delete);
    }
}
