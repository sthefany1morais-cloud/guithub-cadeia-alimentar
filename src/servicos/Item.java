package servicos;

import model.especies.Especie;

public class Item {
    private Especie especie;
    private int custo;

    public Item() {
    }

    public Item(Especie especie, int custo) {
        this.especie = especie;
        this.custo = custo;
    }

    public Especie getEspecie() {
        return especie;
    }

    public int getCusto() {
        return custo;
    }
}
