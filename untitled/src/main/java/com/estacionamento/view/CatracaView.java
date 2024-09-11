package com.estacionamento.view;
import com.estacionamento.controller.CatracaController;
import com.estacionamento.panel.CatracaPanel;
import javax.swing.*;

public class CatracaView {
    private final JFrame frame;
    private final CatracaPanel catracaPanel;
    
    public CatracaView(CatracaController catracaController){
        this.frame = inicializarFrame();
        this.catracaPanel = new CatracaPanel(catracaController);
        this.frame.getContentPane().add(catracaPanel.getPanel());
    }

    private JFrame inicializarFrame(){
        JFrame frame = new JFrame("Bilhete");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);//Centraliza;
        return frame;
    }

    public void show(){
        this.frame.setVisible(true);
    }

    public void close(){
        this.frame.dispose();
    }
}
