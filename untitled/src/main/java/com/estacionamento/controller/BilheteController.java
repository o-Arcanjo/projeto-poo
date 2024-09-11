package com.estacionamento.controller;
import com.estacionamento.model.BilheteModel;
import com.estacionamento.view.BilheteView;
import com.estacionamento.model.Bilhete;
import com.estacionamento.model.BilheteListModel;
import java.util.Optional;
import java.io.*;
import java.util.List;



public class BilheteController {
    private static BilheteListModel bilheteList;
    private BilheteModel bilheteModel;
    private BilheteView bilheteView;
    private final File file = new File("dados.bin");

    public BilheteController() throws IOException, ClassNotFoundException {
        this.bilheteModel = new BilheteModel();
        this.bilheteView = new BilheteView(this);
        if(!file.exists()){
            bilheteList = new BilheteListModel();
            salvar();
        }else{
            ler();
        }
    }

    private int gerarIdentificadorBilhete(){
        return getBilhete().stream()
                                                .mapToInt(Bilhete::getIdPag)
                                                .max()
                                                .orElse(0) + 1;
    }

    public Bilhete emitirBilhete() throws IOException, ClassNotFoundException {
        int identificador = gerarIdentificadorBilhete();
        Bilhete bilhete = bilheteModel.emitirBilhete(identificador);
        getBilhete().add(bilhete);
        salvar();
        return bilhete;
    }

    public Bilhete buscarBilhete(int idPag){
       Optional <Bilhete> bilhete = bilheteModel.procurarBilhete(getBilhete(), idPag);
       return bilhete.isPresent() ? bilhete.get() : null;
    }

    public void removerBilhete(Bilhete bilhete){
        getBilhete().remove(bilhete);
    }

    private List<Bilhete> getBilhete(){
        return bilheteList.getListBilhete();
    }

    protected static void salvar() throws IOException, ClassNotFoundException {
        try(FileOutputStream file = new FileOutputStream("dados.bin");
            ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(bilheteList);
        }
    }

    protected static void ler() throws IOException, ClassNotFoundException {
      try(FileInputStream file = new FileInputStream("dados.bin");
          ObjectInputStream ois = new ObjectInputStream(file)){
          bilheteList = (BilheteListModel) ois.readObject();
      }
    }

    public void abrirJanelaPrincipal(){
        this.bilheteView.show();
    }

}

// Obsidian Notion
