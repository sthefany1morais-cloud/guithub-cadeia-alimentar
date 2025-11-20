package servicos;

import model.especies.Especie;

import java.util.ArrayList;
import java.util.List;

public class FilaDePrioridade {

    private boolean procurarMenor;
    private List<Item> lista;

    public FilaDePrioridade() {
    }

    public FilaDePrioridade(boolean procurarMenor) {
        this.procurarMenor = procurarMenor; // true = menor, false = maior
        this.lista = new ArrayList<>();
    }

    public void adicionar(Especie especie, int custo) {
        lista.add(new Item(especie, custo));
    }

    public Item poll() {
        if (lista.isEmpty()) return null;

        int indiceMelhor = 0;

        for (int i = 1; i < lista.size(); i++) {

            boolean melhor = procurarMenor ?
                    (lista.get(i).getCusto() < lista.get(indiceMelhor).getCusto()) :
                    (lista.get(i).getCusto() > lista.get(indiceMelhor).getCusto());

            if (melhor) {
                indiceMelhor = i;
            }
        }
        return lista.remove(indiceMelhor);
    }

    public boolean vazia() {
        return lista.isEmpty();
    }
}

