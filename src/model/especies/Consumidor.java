package model.especies;

public class Consumidor extends Especie{
    public Consumidor(String nome, int energia) {
        super(nome, energia);
        this.tipo = "Consumidor";
    }
}
