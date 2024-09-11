package com.estacionamento.view;

import com.estacionamento.controller.BilheteController;
import com.estacionamento.controller.CatracaController;
import com.estacionamento.model.Bilhete;
import com.estacionamento.controller.PagamentoController;
import java.awt.*;
import java.io.*;

import javax.swing.*;

public class BilhetePanel {
    private PagamentoController pagamentoController;
    private BilheteController bilheteController;
    private CatracaController catracaController;
    private final JPanel panel;
    private JTextArea textArea;
    private JButton catracaButton;
    private JButton emitirButton;
    private JButton pagarButton;

    public BilhetePanel(BilheteController bilheteController) throws IOException, ClassNotFoundException {
        this.bilheteController = bilheteController;
        this.pagamentoController = new PagamentoController(bilheteController);
        this.catracaController = new CatracaController(bilheteController);
        this.panel = new JPanel(new GridBagLayout());
        adicionarComponentesAoPanel();
        exibirBotaoEmitir();
        exibirBotaoPagar();
        exibirBotaoCatraca();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void adicionarComponentesAoPanel() {
        emitirButton = new JButton("Emitir");
        pagarButton = new JButton("Pagar");
        catracaButton = new JButton("Catraca"); // Inicializa o botão da catraca
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10); // adicionar espaço próximo aos botões
        gbc.weightx = 1.0; // permitir usar espaço horizontal;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(emitirButton, gbc);

        gbc.gridx = 1;
        panel.add(pagarButton, gbc);

        gbc.gridx = 2;
        panel.add(catracaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(textArea, gbc);
    }

    private void exibirBotaoCatraca() {
        catracaButton.addActionListener(e -> {
            try {
                catracaController.abrirJanelaCatraca();
            } catch (RuntimeException a) {
                JOptionPane.showMessageDialog(panel,
                        "Erro ao abrir janela da catraca: " + a.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private void exibirBotaoEmitir()  {
        emitirButton.addActionListener(e -> {
                try{
                Bilhete bilhete = bilheteController.emitirBilhete();
                exibirInformacoesBilhete(bilhete);
                }catch(IOException | ClassNotFoundException a){
                    JOptionPane.showMessageDialog(panel,
                            "Erro ao emitir bilhete: " + a.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
    }

    private void exibirBotaoPagar(){
        pagarButton.addActionListener(e -> {
           try{
            pagamentoController.abrirJanelaPagamento();
           }catch(RuntimeException a){
                JOptionPane.showMessageDialog(panel,
                        "Erro ao abrir janela: " + a.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
           }
        });
    }


    private void exibirInformacoesBilhete(Bilhete bilhete){
        if(!panel.isAncestorOf(textArea)){
            this.panel.add(textArea);
        }
        textArea.setText(
            "IDIdentificação: " + bilhete.getId() + "\n" +
            "Data e Hora: " + bilhete.getDate() + " " + bilhete.getTime() + "\n" +
            "IDPagamento: " + bilhete.getIdPag());
            this.panel.revalidate();
            this.panel.repaint();
    }
}





