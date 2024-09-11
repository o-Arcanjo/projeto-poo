package com.estacionamento.controller;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.estacionamento.exception.PagamentoNaoRealizadoException;
import com.estacionamento.exception.ErroInternoNoGerenciamentoException;
import com.estacionamento.model.Bilhete;
import com.estacionamento.view.CatracaView;

public class CatracaController {
    private CatracaView catracaView;
    private BilheteController bilheteController;

    public CatracaController(BilheteController bilheteController) {
        this.catracaView = new CatracaView(this);
        this.bilheteController = bilheteController;
    }

    private void salvar() throws IOException, ClassNotFoundException {
        BilheteController.salvar();
    }

    public void abrirJanelaCatraca() {
        catracaView.show();
    }

   public void confirmarPagamento(int idPag) throws IOException, ClassNotFoundException,PagamentoNaoRealizadoException, ErroInternoNoGerenciamentoException {
        Bilhete bilhete = bilheteController.buscarBilhete(idPag);
        if (bilhete == null) {
            throw new NullPointerException("Bilhete não Encontrado para o ID " + idPag);
        }
        if (!bilhete.getStatus() || bilhete.getComprovantePagamento() == null) {
            throw new PagamentoNaoRealizadoException("Pagamento não realizado para o bilhete com ID " + idPag);
        }
        try {
            bilheteController.removerBilhete(bilhete);
            String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String tempoFormatado = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            salvar();
            // Chama o método para salvar o bilhete no CSV
            salvarBilheteEmCSV(bilhete, dataFormatada, tempoFormatado);

        } catch (Exception e) {
            throw new ErroInternoNoGerenciamentoException(e.getMessage());
        }
    }

    private void salvarBilheteEmCSV(Bilhete bilhete, String dataFormatada, String tempoFormatado) throws IOException {
        File arquivoCSV = new File("bilhetes.csv");

              try (FileWriter fw = new FileWriter(arquivoCSV, true);
             BufferedWriter bw = new BufferedWriter(fw)) {


            if (arquivoCSV.length() == 0) {
                String cabecalho = "IDBilhete,DataEmissao,HoraEmissao,ComprovantePagamento,DataSaida,HoraSaida";
                bw.write(cabecalho);
                bw.newLine();
            }

            String linha = bilhete.getId() + "," + bilhete.getDate() + "," + bilhete.getTime() + "," +
                    bilhete.getComprovantePagamento() + "," + dataFormatada + "," + tempoFormatado;

            bw.write(linha);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar bilhete no CSV: " + e.getMessage());
            throw e;
        }
    }

}
