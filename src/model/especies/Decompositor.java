package model.especies;

import execoes.ValorEnergeticoInvalidoException;

public class Decompositor extends Especie{
    private double porcentagem;
    public Decompositor(String nome, int energia) throws ValorEnergeticoInvalidoException {
        super(nome, energia);
        this.porcentagem = 0.1;
        this.tipo = "Decompositor";
    }
    public Decompositor(String nome, int energia, double porcentagem) throws ValorEnergeticoInvalidoException{
        super(nome, energia);
        this.porcentagem = porcentagem/100;
        this.tipo = "Decompositor";
    }
    public void decompor(Especie morta){
        int custo = (int) Math.round(morta.getEnergia()*this.porcentagem);
        super.adicionarPresa(morta, custo);
    }
}
