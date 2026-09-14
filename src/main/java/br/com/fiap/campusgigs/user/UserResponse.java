package br.com.fiap.campusgigs.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse (
        @JsonProperty("cep_response")
        CepResponse cepResponse
){

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CepResponse(
            @JsonProperty("localidade")
            String localidade,
            @JsonProperty("uf")
            String uf
    ){}
}