package com.microservico.microteste.connections;

import com.microservico.microteste.constantes.RabbitMQConstantes;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConnection {
    private static final String NOME_EXCHANGE = "amq.direct";
    // interface do amqpAdmin, que é responsavel por conectar ao rabbitMQ e fazer a construção das filas
    private AmqpAdmin amqpAdmin;
    public RabbitMQConnection(AmqpAdmin amqpAdmin){
        // instanciando a classe, o que vai nos permitir utilizar ela para criar as filas
        this.amqpAdmin = amqpAdmin;
    }

    private Queue fila(String nomeFila){ //criando a fila
        return new Queue(nomeFila, true, false, false);
    }

    private DirectExchange trocaDireta(){ // criando a exchange
        return new DirectExchange(NOME_EXCHANGE);
    }

    private Binding relacionamento(Queue fila, DirectExchange troca){ // criando o binding (precisa receber uma fila e a exchange)
        // (precisa de destino, o tipo de exchange, qual exchange será e a router key)
        return new Binding(fila.getName(), Binding.DestinationType.QUEUE, troca.getName(), fila.getName(), null);
    }
    @PostConstruct
    private void adiciona(){
        Queue filaEstoque = this.fila(RabbitMQConstantes.FILA_ESTOQUE);
        Queue filaPreco = this.fila(RabbitMQConstantes.FILA_PRECO);

        DirectExchange troca = this.trocaDireta();

        Binding ligacaoEstoque = this.relacionamento(filaEstoque, troca);
        Binding ligacaoPreco = this.relacionamento(filaPreco, troca);


        // criando as filas
        this.amqpAdmin.declareQueue(filaEstoque);
        this.amqpAdmin.declareQueue(filaPreco);

        // criando as exchanges
        this.amqpAdmin.declareExchange(troca);

        // declarando as bindings
        this.amqpAdmin.declareBinding(ligacaoEstoque);
        this.amqpAdmin.declareBinding(ligacaoPreco);

    }

}
