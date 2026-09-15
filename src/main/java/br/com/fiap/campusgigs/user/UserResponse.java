package br.com.fiap.campusgigs.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
        String cep,
        @JsonProperty("localidade")
        String location,
        String uf,
        @JsonProperty("erro")
        Boolean error
){}