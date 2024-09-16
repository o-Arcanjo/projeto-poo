package com.estacionamento.panel;

import java.io.*;
import com.estacionamento.controller.CatracaController;
import com.estacionamento.exception.CampoVazioException;
import com.estacionamento.exception.ErroInternoNoGerenciamentoException;
import com.estacionamento.exception.PagamentoNaoRealizadoException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatracaPanel {
    private final JPanel panel;
    private JButton catracaButton;
    private JButton adicionarBilheteButton;
    private final CatracaController catracaController;
    private JTextArea textArea;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public CatracaPanel(CatracaController catracaController) {
        this.catracaController = catracaController;
        this.panel = new JPanel(new BorderLayout());
        adicionarComponentesAoPanel();
        exibirBotaoCatraca();
    }

    private void adicionarComponentesAoPanel(){
        JLabel instrucaoLabel = new JLabel("Adicione o código de pagamento:");

        this.textArea = new JTextArea(10, 30);
        this.textArea.setEditable(true);
        this.catracaButton = new JButton("Catraca");
        this.adicionarBilheteButton = new JButton("Adicionar Bilhete");

        String[] colunas = {"IDBilhete", "DataEmissao", "HoraEmissao", "ComprovantePagamento", "DataSaida", "HoraSaida", "TotalBilhetesDia"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        panelSuperior.add(instrucaoLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        panelSuperior.add(catracaButton, gbc);

        gbc.gridy = 2;
        gbc.weighty = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panelSuperior.add(textArea, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1;
        panelSuperior.add(adicionarBilheteButton, gbc);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

    }

    private void exibirBotaoCatraca() {
        catracaButton.addActionListener(e -> {
            try {
                int valorDigitado = verificarValor(); // Correção para int
                catracaController.confirmarPagamento(valorDigitado);
            } catch (NumberFormatException | NullPointerException | PagamentoNaoRealizadoException | ErroInternoNoGerenciamentoException | IOException | ClassNotFoundException |
                     CampoVazioException a) {
                JOptionPane.showMessageDialog(panel,
                        "Erro ao verificar pagamento: " + a.getMessage(),
                        "Verificação Imprecisa", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private int verificarValor() throws CampoVazioException, NumberFormatException {
        if (!panel.isAncestorOf(textArea) || textArea.getText().trim().isEmpty()) {
            throw new CampoVazioException("Campo não pode ser vazio!");
        }
        try {
            return Integer.parseInt(textArea.getText());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Formato Inválido!");
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
