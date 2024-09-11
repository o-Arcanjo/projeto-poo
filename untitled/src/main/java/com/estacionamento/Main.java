package com.estacionamento;
import com.estacionamento.controller.BilheteController;
import java.io.*;

public class Main {
    public static void main(String[] args){
      try{
          BilheteController bilheteController = new BilheteController();
          bilheteController.abrirJanelaPrincipal();
      }catch (IOException | ClassNotFoundException e) {
            logError("Erro ao carregar dados", e);
            System.exit(1);
      }
    }

    private static void logError(String msg, Exception e){
        System.err.println(msg + ": " + e.getMessage());
        e.printStackTrace();
    }
}
