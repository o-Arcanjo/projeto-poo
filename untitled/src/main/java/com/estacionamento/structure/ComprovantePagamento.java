package com.estacionamento.structure;
import java.io.Serializable;
import java.util.UUID;

public class ComprovantePagamento implements Serializable {
    private UUID id;
    private String descricao;
    private int valorPagamento;
    private String metodoPagamento;
    private String horaPagamento;

    public ComprovantePagamento(UUID id, String descricao, int valorPagamento, String metodoPagamento, String horaPagamento) {
        this.id = id;
        this.descricao = descricao;
        this.valorPagamento = valorPagamento;
        this.metodoPagamento = metodoPagamento;
        this.horaPagamento = horaPagamento;
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getValorPagamento() {
        return valorPagamento;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public String getHoraPagamento() {
        return horaPagamento;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValorPagamento(int valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setHoraPagamento(String horaPagamento) {
        this.horaPagamento = horaPagamento;
    }
}
