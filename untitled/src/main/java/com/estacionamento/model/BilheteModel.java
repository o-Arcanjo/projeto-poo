package com.estacionamento.model;

import com.estacionamento.controller.BilheteController;
import com.estacionamento.model.Bilhete;

import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class BilheteModel {
    public Bilhete emitirBilhete(int idPag) {
        LocalDate data = LocalDate.now();
        LocalTime tempo = LocalTime.now();
        UUID id = UUID.randomUUID();
        String dataFormatada = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String tempoFormatado = tempo.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return new Bilhete(id, dataFormatada, tempoFormatado, idPag);
    }
    public Optional<Bilhete> procurarBilhete(List<Bilhete> listaDeBilhetes, int idPag) {
        return listaDeBilhetes.stream()
                .filter(bilhete -> bilhete.getIdPag() == idPag) // Usar == para comparar ints
                .findFirst();
    }
}
