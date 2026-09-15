package br.com.fiap.campusgigs.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String cep;

    private String location;

    private String uf;

    @Pattern(regexp = "ADMIN|USER")
    private String role;
}