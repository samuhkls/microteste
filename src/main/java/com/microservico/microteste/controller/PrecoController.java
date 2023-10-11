package com.microservico.microteste.controller;

import com.microservico.microteste.constantes.RabbitMQConstantes;
import com.microservico.microteste.dto.EstoqueDto;
import com.microservico.microteste.dto.PrecoDto;
import com.microservico.microteste.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "preco")
public class PrecoController {


    @Autowired
    private RabbitMQService rabbitMQService;

    @PutMapping
    private ResponseEntity alteraPreco(@RequestBody PrecoDto PrecoDto){
        System.out.println(PrecoDto.codigoproduto);
        System.out.println(PrecoDto.preco);
        this.rabbitMQService.enviaMensagem(RabbitMQConstantes.FILA_PRECO, PrecoDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
