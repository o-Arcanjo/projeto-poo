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
        carregarDadosDoCSV("bilhetes.csv");
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

        // Adicionar ação ao botão de adicionar bilhete
        adicionarBilheteButton.addActionListener(e -> {
            try {
                adicionarBilheteAoCSV("bilhetes.csv", "1", "2024-09-08", "14:00", "Pagamento123", "2024-09-08", "15:00");
                carregarDadosDoCSV("bilhetes.csv"); // Recarregar os dados após adicionar o bilhete
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(panel,
                        "Erro ao adicionar bilhete: " + ex.getMessage(),
                        "Erro de Adição", JOptionPane.ERROR_MESSAGE);
            }
        });
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

    private void carregarDadosDoCSV(String caminhoArquivo) {
        File arquivoCSV = new File(caminhoArquivo);

        if (!arquivoCSV.exists()) {
            try {
                arquivoCSV.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoCSV))) {
                    writer.write("IDBilhete,DataEmissao,HoraEmissao,ComprovantePagamento,DataSaida,HoraSaida,TotalBilhetesDia");
                    writer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(panel, "Erro ao criar o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Map<String, Integer> contagemBilhetesPorDia = new HashMap<>();
        List<String[]> bilhetes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            br.readLine(); // Pula o cabeçalho

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length < 6) {
                    continue;
                }

                String dataSaida = dados[4];
                contagemBilhetesPorDia.put(dataSaida, contagemBilhetesPorDia.getOrDefault(dataSaida, 0) + 1);
                bilhetes.add(dados);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "Erro ao ler o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoCSV))) {
            writer.write("IDBilhete,DataEmissao,HoraEmissao,ComprovantePagamento,DataSaida,HoraSaida,TotalBilhetesDia");
            writer.newLine();

            for (String[] dados : bilhetes) {
                String dataSaida = dados[4];
                int totalBilhetesDia = contagemBilhetesPorDia.getOrDefault(dataSaida, 0);

                writer.write(String.join(",", dados) + "," + totalBilhetesDia);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "Erro ao escrever no arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabela.setRowCount(0);

        for (String[] dados : bilhetes) {
            String dataSaida = dados[4];
            int totalBilhetesDia = contagemBilhetesPorDia.get(dataSaida);

            Object[] linhaTabela = {
                    dados[0],
                    dados[1],
                    dados[2],
                    dados[3],
                    dados[4],
                    dados[5],
                    totalBilhetesDia
            };

            modeloTabela.addRow(linhaTabela);
        }
    }

    private void adicionarBilheteAoCSV(String caminhoArquivo, String idBilhete, String dataEmissao, String horaEmissao, String comprovantePagamento, String dataSaida, String horaSaida) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            writer.write(idBilhete + "," + dataEmissao + "," + horaEmissao + "," + comprovantePagamento + "," + dataSaida + "," + horaSaida);
            writer.newLine();
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
