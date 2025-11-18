package model.especies;

import execoes.ValorEnergeticoInvalidoException;

public class Decompositor extends Especie{

    public Decompositor(){
        super();
    }

    public Decompositor(String nome, int energia) throws ValorEnergeticoInvalidoException {
        super(nome, energia);
        this.tipo = "Decompositor";
    }
    @Override
    public void adicionarPresa(Especie morta){
        int custo = (int) Math.round(morta.getEnergia()*0.1);
        super.adicionarPresa(morta, custo);
    }
}
