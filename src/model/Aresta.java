package model;

import model.especies.Especie;

public class Aresta {

    private Especie predador;
    private Especie presa;
    private int custoEnergetico;
    private int ganhoEnergeticoLiquido;

    public Aresta(Especie predador, Especie presa, int custo){
        this.predador = predador;
        this.presa = presa;
        this.custoEnergetico = custo;
        this.ganhoEnergeticoLiquido = presa.getEnergia() - custo;
    }
    public Aresta(Especie predador, Especie presa){
        this.predador = predador;
        this.presa = presa;
        this.custoEnergetico = calcularCusto(presa, predador);
        this.ganhoEnergeticoLiquido = presa.getEnergia() - this.custoEnergetico;
    }

    @Override
    public String toString() {
        return "\nPredador: " + predador.especieSimplificada() +
                "\nPresa: " + presa.especieSimplificada() +
                "\nCustoEnergetico: " + custoEnergetico +
                "\nGanhoEnergeticoLiquido: " + ganhoEnergeticoLiquido;
    }

    private int calcularCusto(Especie presa, Especie predador){
        double ratio = (double) presa.getEnergia() / predador.getEnergia();
        double calculado = presa.getEnergia() * ratio;
        if (calculado >= presa.getEnergia()){
            calculado = presa.getEnergia()*0.9;
        }
        return (int) Math.round(calculado);
    }

    public Especie getPredador() {
        return predador;
    }

    public Especie getPresa() {
        return presa;
    }

    public int getCustoEnergetico() {
        return custoEnergetico;
    }

    public int getGanhoEnergeticoLiquido() {
        return ganhoEnergeticoLiquido;
    }
}
