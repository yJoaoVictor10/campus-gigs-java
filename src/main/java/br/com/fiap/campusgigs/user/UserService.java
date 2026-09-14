package br.com.fiap.campusgigs.user;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(
        url = "https://viacep.com.br/ws/04747140/json/",
        accept = "application/json"
)
public interface UserService {

    @GetExchange
    public UserResponse getCep();
}
