package br.com.fiap.campusgigs.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Users save(@RequestBody Users user) {

        UserResponse response = userService.getCep(user.getCep());

        if (Boolean.TRUE.equals(response.error())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "CEP não encontrado"
            );
        }

        user.setLocation(response.location());
        user.setUf(response.uf());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public Users update(
            @PathVariable Long id,
            @RequestBody Users userData,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuário não encontrado"
                        )
                );

        if (!user.getUsername().equals(username)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você só pode atualizar seus próprios dados"
            );
        }

        UserResponse response;

        try {
            response = userService.getCep(userData.getCep());
        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de CEP indisponível no momento"
            );
        }

        if (Boolean.TRUE.equals(response.error())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "CEP não encontrado"
            );
        }

        user.setCep(userData.getCep());
        user.setLocation(response.location());
        user.setUf(response.uf());

        return userRepository.save(user);
    }
}
