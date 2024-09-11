package com.estacionamento.view;
import com.estacionamento.controller.PagamentoController;
import com.estacionamento.panel.PagamentoPanel;
import javax.swing.*;

public class PagamentoView {
    private final JFrame frame;
    private PagamentoPanel pagamentoPanel;
    public PagamentoView(PagamentoController pagamentoController){
        this.frame = inicializarFrame();
        this.pagamentoPanel = new PagamentoPanel(pagamentoController);
        this.frame.getContentPane().add(pagamentoPanel.getPanel());
    }

    private JFrame inicializarFrame(){
        JFrame frame = new JFrame("Bilhete");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        return frame;
    }


    public void show(){
        this.frame.setVisible(true);
    }

    public void close(){
        this.frame.dispose();
    }
}
