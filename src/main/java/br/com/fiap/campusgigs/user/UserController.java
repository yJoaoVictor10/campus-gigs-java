package br.com.fiap.campusgigs.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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
}
