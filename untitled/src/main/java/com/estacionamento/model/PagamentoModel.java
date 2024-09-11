package com.estacionamento.model;
import com.estacionamento.structure.ComprovantePagamento;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PagamentoModel {
    private final String descricao = "Comprovante de Pagamento de Estacionamento de Veículos";
    private final String metodoPagamento = "Dinheiro";
    public ComprovantePagamento gerarComprovantePagamento(int valorPagamento){
        UUID id = UUID.randomUUID();
        LocalDateTime dataHora = LocalDateTime.now();
        String dataHoraFormatada = dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return new ComprovantePagamento(id, descricao, valorPagamento,metodoPagamento, dataHoraFormatada);
    }
}
