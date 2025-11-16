package model.especies;

import execoes.ValorEnergeticoInvalidoException;

public class Consumidor extends Especie{

    public Consumidor(){
        super();
    }

    public Consumidor(String nome, int energia) throws ValorEnergeticoInvalidoException {
        super(nome, energia);
        this.tipo = "Consumidor";
    }
}
