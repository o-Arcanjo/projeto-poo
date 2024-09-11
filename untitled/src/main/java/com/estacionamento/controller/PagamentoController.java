package com.estacionamento.controller;
import com.estacionamento.view.PagamentoView;
import com.estacionamento.model.PagamentoModel;
import com.estacionamento.model.Bilhete;
import com.estacionamento.enums.Meses;
import com.estacionamento.structure.ComprovantePagamento;
import com.estacionamento.exception.PagamentoJaRealizadoException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PagamentoController{
    private final PagamentoView pagamentoView;
    private final PagamentoModel pagamentoModel;
    private final BilheteController bilheteController;

    public PagamentoController(BilheteController bilheteController) throws IOException, ClassNotFoundException {
        this.pagamentoView = new PagamentoView(this);
        this.pagamentoModel = new PagamentoModel();
        this.bilheteController = bilheteController;
    }

    public void realizarPagamento(int idPag)  throws IOException, ClassNotFoundException,PagamentoJaRealizadoException {
        Bilhete bilhete = bilheteController.buscarBilhete(idPag);
        if(bilhete == null) {
            throw new NullPointerException("Bilhete não Encontrado para o ID " + idPag);
        }
        if(bilhete.getStatus() == true && bilhete.getComprovantePagamento() != null){
            throw new PagamentoJaRealizadoException("Pagamento já realizado para o bilhete com ID " + idPag);
        }
        int valorPagamento = (int) calcularValorPagamento(bilhete, 3, 20, 10);
        ComprovantePagamento comprovantePagamento = pagamentoModel.gerarComprovantePagamento(valorPagamento); // valor pagamento e metodo pagamento

        bilhete.setStatus(true);
        bilhete.setComprovantePagamento(comprovantePagamento);
        salvar();
    }


    private void salvar() throws IOException, ClassNotFoundException {
        BilheteController.salvar();
    }

    private int calcularDias(int mes, int ano) {
     Meses mesEnum = Meses.values()[mes-1];
     return mesEnum.getDias(ano);
    }

    private long calcularValorPagamento(Bilhete bilhete, int tempoToleranciaHora, int valorMulta, int valorPadraoPagamento) {
        int diaEstacionamento = obterDiaEstacionamento(bilhete);
        int mesEstacionamento = obterMesEstacionamento(bilhete);
        int anoEstacionamento = obterAnoEstacionamento(bilhete);

        int diaRetirada = LocalDate.now().getDayOfMonth();
        int mesRetirada = LocalDate.now().getMonthValue();
        int anoRetirada = LocalDate.now().getYear();

        long totalDiasMilissegundos = calcularDiasEmMilissegundos(diaEstacionamento, mesEstacionamento, anoEstacionamento, diaRetirada, mesRetirada, anoRetirada);
        // Converte tudo para milissegundos
        long milissegundosPosInsercaoDiaria = calcularMilissegundosPosInsercaoDiaria(bilhete, diaEstacionamento, diaRetirada, mesEstacionamento, mesRetirada, anoEstacionamento, anoRetirada);

        final long tempoToleranciaMilissegundos = (long) tempoToleranciaHora * 3_600_000;

        long diferencaTempo = calcularDiferencaTempo(totalDiasMilissegundos, milissegundosPosInsercaoDiaria, tempoToleranciaMilissegundos);

        return calcularValorFinal(diferencaTempo, valorPadraoPagamento, valorMulta);
    }

  private LocalDate obterDataEstacionamento (Bilhete bilhete){
        return LocalDate.parse(bilhete.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

    private int obterAnoEstacionamento(Bilhete bilhete){
         return obterDataEstacionamento(bilhete).getYear();
    }

    private int obterMesEstacionamento(Bilhete bilhete){
        return obterDataEstacionamento(bilhete).getMonthValue();
  }

    private int obterDiaEstacionamento(Bilhete bilhete){
        return obterDataEstacionamento(bilhete).getDayOfMonth();
    }

    private long calcularDiasEmMilissegundos(int diaEstacionamento, int mesEstacionamento, int anoEstacionamento, int diaRetirada, int mesRetirada, int anoRetirada) {
        int diasRestantesNoMesEstacionamento = 0;
        int diasRestantesNoMesRetirada = 0;
        int diasIntermediarios = 0;
        long milissegundosRetirada = 0;

      if(mesEstacionamento == mesRetirada && anoEstacionamento == anoRetirada){
            diasRestantesNoMesEstacionamento = diaRetirada - diaEstacionamento;
        }else{
           int m = mesEstacionamento + 1;
           int j = m;
           int ano = anoEstacionamento;

           while(m < (anoRetirada - ano) * 12 + (mesRetirada - mesEstacionamento)){
               diasIntermediarios += calcularDias(j, ano);
               if(j > 12){
                   j = 1;
                   ano++;
               }
               m++;
               j++;
               System.out.println("eita" + diasRestantesNoMesEstacionamento);
           }
           int diaNoMesEstacionamento = calcularDias(mesEstacionamento, anoEstacionamento);
           diasRestantesNoMesEstacionamento = diaNoMesEstacionamento - diaEstacionamento;
           diasRestantesNoMesRetirada = (diaRetirada - 1);
           milissegundosRetirada = calcularMilissegundosRetirada();
        }

        // Calcula os dias completos até o dia anterior à retirada (sem contar o dia atual)
        return (long) ((diasRestantesNoMesEstacionamento + diasIntermediarios + diasRestantesNoMesRetirada) * 24 * 60 * 60 * 1000) + milissegundosRetirada;

    }

    private long calcularMilissegundosPosInsercaoDiaria(Bilhete bilhete, int diaEstacionamento, int diaRetirada, int mesEstacionamento, int mesRetirada, int anoEstacionamento, int anoRetirada) {
        long milissegundosInsercao = calcularInsercaoEstacionamento(bilhete);

        if (diaEstacionamento == diaRetirada && mesEstacionamento == mesRetirada && anoEstacionamento == anoRetirada) {
            return calcularMilissegundosRetirada() - milissegundosInsercao;
        }

        long milissegundosAteMeiaNoite = (24 * 60 * 60 * 1000) - milissegundosInsercao;
        return milissegundosAteMeiaNoite;
    }

    private long calcularInsercaoEstacionamento(Bilhete bilhete){
        return LocalTime.parse(bilhete.getTime()).toSecondOfDay() * 1000;
    }

    private long calcularMilissegundosRetirada() {
        return LocalTime.now().toSecondOfDay() * 1000;
    }

    private long calcularDiferencaTempo(long totalDiasMilissegundos, long milissegundosPosInsercao, long tempoToleranciaMilissegundos) {
        return (totalDiasMilissegundos + milissegundosPosInsercao) - tempoToleranciaMilissegundos;
    }

    private long calcularValorFinal(long diferencaTempo, int valorPadraoPagamento, int valorMulta) {
        if (diferencaTempo <= 0) {
            return valorPadraoPagamento;
        }

        long converteSegundos = diferencaTempo / 1000;
        long horas = converteSegundos / 3600;
        long minutos = (converteSegundos % 3600 / 60);
        long segundos = (converteSegundos % 60);

        return (horas * valorMulta) + (minutos * (valorMulta / 60)) + (segundos * (valorMulta / 3600));
    }

    public void abrirJanelaPagamento() {
        pagamentoView.show();
    }
}
