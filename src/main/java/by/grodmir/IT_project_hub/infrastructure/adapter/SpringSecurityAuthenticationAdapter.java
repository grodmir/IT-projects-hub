package by.grodmir.IT_project_hub.infrastructure.adapter;

import by.grodmir.IT_project_hub.domain.model.AuthTokens;
import by.grodmir.IT_project_hub.domain.service.AuthenticationPort;
import by.grodmir.IT_project_hub.infrastructure.security.JwtService;
import by.grodmir.IT_project_hub.infrastructure.security.RefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityAuthenticationAdapter implements AuthenticationPort {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenServiceImpl refreshTokenService;

    public AuthTokens authenticate(String username, String rawPassword) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.issue(username).token();

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public AuthTokens refresh(String refreshToken) {
        var rotated = refreshTokenService.rotate(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(rotated.username());
        String newAccessToken = jwtService.generateToken(userDetails);

        return new AuthTokens(newAccessToken, rotated.token());
    }

    @Override
    public void revoke(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }
}
