package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.user.Users;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String provider;
    private String title;
    private String description;
    private String category;
    private Double price;
}
