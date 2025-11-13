package model.especies;

public class Produtor extends Especie {
    private int fotossintese;
    public Produtor(String nome, int energia) {
        super(nome, energia);
        this.fotossintese = (int) (this.energia*0.5);
        this.tipo = "Produtor";
    }
    @Override
    public int getGanhoTotal() {
        int ganhoPresas = super.getGanhoTotal();
        return this.fotossintese + ganhoPresas;
    }
}
