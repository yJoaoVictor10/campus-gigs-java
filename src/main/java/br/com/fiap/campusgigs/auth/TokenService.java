package br.com.fiap.campusgigs.auth;


import br.com.fiap.campusgigs.user.UserRepository;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final UserRepository userRepository;

    public TokenService(JwtEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public String generateToken(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found with username: " + username)
        );
        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("fiap-gigs-api")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(username)
                .claim("role", user.getRole())
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
