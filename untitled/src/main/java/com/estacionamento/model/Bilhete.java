package com.estacionamento.model;
import com.estacionamento.structure.ComprovantePagamento;

import java.io.Serializable;
import java.util.UUID;


public class Bilhete implements Serializable {
    private UUID id;
    private String date;
    private String time;
    private int idPag;
    private boolean status;
    private ComprovantePagamento comprovantePagamento;

    public Bilhete(UUID id, String formattedDate, String formattedTime, int idPag) {
        this.id = id;
        this.date = formattedDate;
        this.time = formattedTime;
        this.idPag = idPag;
        this.status = false;
        this.comprovantePagamento = null;
    }

    public UUID getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getIdPag(){
        return idPag;
    }

    public void setComprovantePagamento(ComprovantePagamento comprovantePagamento) {
        this.comprovantePagamento = comprovantePagamento;
    }

    public boolean getStatus() {
        return status;
    }

    public ComprovantePagamento getComprovantePagamento() {
        return comprovantePagamento;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setIdPag(int idPag) {
        this.idPag = idPag;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setStatus(boolean status){
        this.status = status;
    }
}
