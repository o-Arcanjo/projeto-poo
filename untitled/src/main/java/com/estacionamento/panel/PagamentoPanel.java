package com.estacionamento.panel;
import com.estacionamento.controller.PagamentoController;
import com.estacionamento.model.Bilhete;
import com.estacionamento.exception.PagamentoJaRealizadoException;
import com.estacionamento.exception.CampoVazioException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PagamentoPanel {
    private final JPanel panel;
    private JButton pagarButton;
    private final PagamentoController pagamentoController;
    private JTextArea textArea;

    public PagamentoPanel(PagamentoController pagamentoController) {
        this.panel = new JPanel(new GridBagLayout());
        this.pagamentoController = pagamentoController;
        adicionarComponentesAoPanel();
        exibirBotaoPagar();
    }

    private void adicionarComponentesAoPanel(){
        // Criação do JLabel
        JLabel instrucaoLabel = new JLabel("Adicione o código de pagamento:");

        this.textArea = new JTextArea(10, 30);
        this.textArea.setEditable(true);
        this.pagarButton = new JButton("Pagar");

        GridBagConstraints gbc = new GridBagConstraints();
       // gbc.fill = GridBagConstraints.HORIZONTAL; // Para o botão ocupar toda a largura

        // Adiciona o JLabel com a instrução
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10); // Margem inferior menor para o JLabel
        panel.add(instrucaoLabel, gbc);

        // Botão "Pagar"
        gbc.gridy = 1; // Mover para a próxima linha
        gbc.weighty = 1; // ocupa 1 parte do espaço vertical
        panel.add(pagarButton, gbc);

        // JTextArea
        gbc.gridy = 2; // Mover para a linha do JTextArea
        gbc.weighty = 2; // ocupa 2 partes do espaço vertical
        gbc.fill = GridBagConstraints.BOTH; // Para o JTextArea ocupar a largura e altura disponíveis
        panel.add(textArea, gbc);
    }



    private void exibirBotaoPagar(){
        pagarButton.addActionListener(e->{
            try{
            short valorDigitado = (short) verificarValor();
            pagamentoController.realizarPagamento(valorDigitado);
            }catch(NumberFormatException | NullPointerException | PagamentoJaRealizadoException | IOException | ClassNotFoundException | CampoVazioException a){
                JOptionPane.showMessageDialog(panel,
                        "Erro ao gerar comprovante de pagamento: " + a.getMessage(),
                        "Impossível Gerar", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private int verificarValor() throws CampoVazioException, NumberFormatException{
       if(!panel.isAncestorOf(textArea) || textArea.getText().trim().isEmpty()) {
          throw new CampoVazioException("Campo não pode ser vazio!");
       }
        try {
            return Integer.parseInt(textArea.getText());
        } catch (NumberFormatException e) {
           throw new NumberFormatException("Formato Inválido!");
        }
    }

    public JPanel getPanel(){
        return panel;
    }

}
