package com.estacionamento.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BilheteListModel implements Serializable {
    private List<Bilhete> listBilhete = new ArrayList<>();
    private String dailyList;

    public BilheteListModel() {
        DateTimeFormatter formatarDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.dailyList = LocalDateTime.now().format(formatarDataHora);
    }
    public List<Bilhete> getListBilhete() {
        return listBilhete;
    }

    public String getDailyList(){
        return dailyList;
    }

    public void setListBilhete(List<Bilhete> listBilhete) {
        this.listBilhete = listBilhete;
    }
}
