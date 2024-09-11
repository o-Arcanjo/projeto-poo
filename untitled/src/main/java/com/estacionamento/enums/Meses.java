package com.estacionamento.enums;

public enum Meses {
    JANEIRO(31),
    FEVEREIRO(28),
    MARCO(31),
    ABRIL(30),
    MAIO(31),
    JUNHO(30),
    JULHO(31),
    AGOSTO(31),
    SETEMBRO(30),
    OUTUBRO(31),
    NOVEMBRO(30),
    DEZEMBRO(31);

   private int dias;

    Meses(int dias){
        this.dias = dias;
    }
    public int getDias(int ano){
        if(this == FEVEREIRO && ano % 4 == 0 && ano % 100 !=0 || ano % 400 == 0){
            return 29;
        }
        return dias;
    }
}
