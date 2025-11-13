package servicos;

import execoes.GrafoInexistenteExeption;
import execoes.GrafoJaExisteException;

import java.util.ArrayList;
import java.util.List;

public class GrafoGerenciador {
    private List<GrafoEcologico> grafos;

    public GrafoGerenciador(){
        this.grafos = new ArrayList<>();
    }
    public void adicionarGrafo(String nome) throws GrafoJaExisteException {
        for (GrafoEcologico grafo: this.grafos){
            if (grafo.getNome().equalsIgnoreCase(nome)){
                throw new GrafoJaExisteException("Essa cadeia já existe.");
            }
        }
        grafos.add(new GrafoEcologico(nome));
    }

    public void removerGrafo(int idGrafo) throws GrafoInexistenteExeption {
        if (idGrafo < 0 || idGrafo > this.grafos.size()-1){
            throw new GrafoInexistenteExeption("Digite um id válido.");
        }
        grafos.remove(idGrafo);
    }

    public GrafoController acessarGrafo(int idGrafo) throws GrafoInexistenteExeption{
        if (idGrafo < 0 || idGrafo > this.grafos.size()-1){
            throw new GrafoInexistenteExeption("Digite um id válido.");
        }
        return new GrafoController(this.grafos.get(idGrafo));
    }

    public List<String> listarGrafos() {
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < grafos.size(); i++) {
            lista.add((i + 1) + " - " + grafos.get(i).getNome());
        }
        return lista;
    }

    public int getTamanho() {
        return grafos.size();
    }

}
