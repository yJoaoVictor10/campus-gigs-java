package br.com.fiap.campusgigs.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    List<Services> findByUserUsername(String username);
}
