package br.com.fiap.campusgigs.contratation;

import br.com.fiap.campusgigs.service.ServiceRepository;
import br.com.fiap.campusgigs.service.Services;
import br.com.fiap.campusgigs.user.UserRepository;
import br.com.fiap.campusgigs.user.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/contratation")
public class ContratationController {

    private final ContratationRepository contratationRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public ContratationController(
            ContratationRepository contratationRepository,
            ServiceRepository serviceRepository,
            UserRepository userRepository
    ) {
        this.contratationRepository = contratationRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Contratation create(
            @PathVariable Long serviceId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuário não encontrado"
                        )
                );

        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Serviço não encontrado"
                        )
                );

        // Verifica se o usuário está tentando
        // contratar o próprio serviço.
        boolean isOwner = service.getUser()
                .getUsername()
                .equals(username);

        if (isOwner) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode contratar seu próprio serviço"
            );
        }

        Contratation contratation = new Contratation();

        contratation.setUser(user);
        contratation.setService(service);

        return contratationRepository.save(contratation);
    }
}