package com.estacionamento.view;
import com.estacionamento.controller.BilheteController;
import com.estacionamento.view.BilhetePanel;
import javax.swing.*;
import java.io.IOException;


public class BilheteView {
    private final JFrame frame;
    private BilhetePanel bilhetePanel;

    public BilheteView(BilheteController bilheteController) throws IOException, ClassNotFoundException  {
        this.frame = inicializarFrame();
        this.bilhetePanel = new BilhetePanel(bilheteController);
        this.frame.getContentPane().add(this.bilhetePanel.getPanel());
    }

    public void show(){
        this.frame.setVisible(true);
    }

    private JFrame inicializarFrame(){
        JFrame frame = new JFrame("Bilhete");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);//Centraliza;
        return frame;
    }
  

    public void close(){
        this.frame.dispose();
    }
}



