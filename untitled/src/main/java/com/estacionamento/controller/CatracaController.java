package com.estacionamento.controller;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.estacionamento.exception.PagamentoNaoRealizadoException;
import com.estacionamento.exception.ErroInternoNoGerenciamentoException;
import com.estacionamento.model.Bilhete;
import com.estacionamento.view.CatracaView;

import javax.swing.*;

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
            salvarBilheteEmCSV(bilhete, dataFormatada, tempoFormatado);

        } catch (Exception e) {
            throw new ErroInternoNoGerenciamentoException(e.getMessage());
        }
    }

    private void salvarBilheteEmCSV(Bilhete bilhete, String dataFormatada, String tempoFormatado) throws IOException {
        File arquivoCSV = new File("bilhetes.csv");
        StringBuilder novaLinha = new StringBuilder();
        boolean dataEncontrada = false;

        // Verifica se o arquivo existe e cria-o se não existir, adicionando o cabeçalho
        if (!arquivoCSV.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCSV))) {
                // Adiciona o cabeçalho
                String cabecalho = "IDBilhete,DataEmissao,HoraEmissao,ComprovantePagamento,DataSaida,HoraSaida,QuantidadeVeiculos";
                bw.write(cabecalho);
                bw.newLine();
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            // Lê o cabeçalho e adiciona ao StringBuilder
            String cabecalho = br.readLine(); // Cabeçalho
            if (cabecalho != null) {
                novaLinha.append(cabecalho).append(System.lineSeparator());
            }
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] colunas = linha.split(",");
                if (colunas.length > 4 && colunas[4].equals(dataFormatada)) {
                    int ultimaColuna = colunas.length - 1;
                    int quantidadeDeVeiculos = Integer.parseInt(colunas[ultimaColuna]) + 1;
                    // Atualiza a quantidade na linha
                    colunas[ultimaColuna] = String.valueOf(quantidadeDeVeiculos);
                    linha = String.join(",", colunas); // Reconstrói a linha
                    dataEncontrada = true;
                }
                // Adiciona a linha (modificada ou não) ao StringBuilder
                novaLinha.append(linha).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
            throw e;
        }

        // Se a data não foi encontrada, adiciona uma nova linha
        if (!dataEncontrada) {
            String novaLinhaDeDados = bilhete.getId() + "," + bilhete.getDate() + "," + bilhete.getTime() + "," +
                    bilhete.getComprovantePagamento() + "," + dataFormatada + "," + tempoFormatado + ",1"; // 1 para a nova quantidade
            novaLinha.append(novaLinhaDeDados).append(System.lineSeparator());
        }

        // Grava de volta no arquivo CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoCSV))) {
            bw.write(novaLinha.toString());
        } catch (IOException e) {
            System.out.println("Erro ao salvar bilhete no CSV: " + e.getMessage());
            throw e;
        }
    }


}
