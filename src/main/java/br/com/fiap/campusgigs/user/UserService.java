package br.com.fiap.campusgigs.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(
        url = "https://viacep.com.br",
        accept = "application/json"
)
public interface UserService {

    @GetExchange("/ws/{cep}/json/")
    public UserResponse getCep(@PathVariable String cep);
}
