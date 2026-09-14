package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.user.UserRepository;
import br.com.fiap.campusgigs.user.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/freela")
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public ServiceController(
            ServiceRepository serviceRepository,
            UserRepository userRepository
    ) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Services> findAll() {
        return serviceRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Services save(
            @RequestBody Services service,
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

        // O dono do serviço sempre será
        // o usuário autenticado pelo JWT.
        service.setUser(user);

        return serviceRepository.save(service);
    }

    @PutMapping("/{id}")
    public Services update(
            @PathVariable Long id,
            @RequestBody Services serviceData,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();

        Services service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Serviço não encontrado"
                        )
                );

        // Somente o dono pode editar.
        if (!service.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você só pode editar seus próprios serviços"
            );
        }

        service.setTitle(serviceData.getTitle());
        service.setDescription(serviceData.getDescription());
        service.setCategory(serviceData.getCategory());
        service.setPrice(serviceData.getPrice());
        service.setProvider(serviceData.getProvider());

        return serviceRepository.save(service);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();

        Services service = serviceRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Serviço não encontrado"
                        )
                );

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuário não encontrado"
                        )
                );

        boolean isOwner = service.getUser()
                .getUsername()
                .equals(username);

        boolean isAdmin = "ADMIN".equals(user.getRole());

        // O dono pode encerrar.
        // ADMIN pode encerrar qualquer serviço.
        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode encerrar este serviço"
            );
        }

        serviceRepository.delete(service);
    }
}