package servicos;

import model.especies.Especie;
import execoes.*;

public class GrafoController {

    private GrafoEcologico grafo;
    private AnalisarGrafo analisador;

    public GrafoController(GrafoEcologico grafo) {
        this.grafo = grafo;
        this.analisador = new AnalisarGrafo(this.grafo);
    }

    public String getNomeGrafo(){
        return grafo.getNome();
    }

    public GrafoEcologico getGrafo() {
        return grafo;
    }

    public void adicionarEspecie(Especie e) throws EspecieJaExisteException {
        grafo.adicionarEspecie(e);
    }

    public void adicionarPredacao(int idPredador, int idPresa, int custo)
            throws EspecieNaoEncontradaException, ValorEnergeticoInvalidoException {
        grafo.adicionarPredacao(idPredador, idPresa, custo);
    }

    public void adicionarPredacao(int idPredador, int idPresa)
            throws EspecieNaoEncontradaException, PredacaoJaExistenteException {
        grafo.adicionarPredacao(idPredador, idPresa);
    }


    public String listarEspeciesSimples() {
        StringBuilder texto = new StringBuilder();
        for (String especie: grafo.listarEspeciesSimples()){
            texto.append(especie).append("\n");
        }
        return texto.toString();
    }

    public String listarPredadores(int idEspecie) throws EspecieNaoEncontradaException {
        StringBuilder texto = new StringBuilder();
        Especie especie = grafo.getEspeciePorId(idEspecie);
        for (String predador: grafo.listarPredadores(especie)){
            texto.append(predador);
        }
        return texto.toString();
    }

    public String listarPresas(int idEspecie) throws EspecieNaoEncontradaException {
        StringBuilder texto = new StringBuilder();
        Especie especie = grafo.getEspeciePorId(idEspecie);
        for (String presa: grafo.listarPresas(especie)){
            texto.append(presa);
        }
        return texto.toString();
    }

    public String maiorCaminho(int origem, int destino, boolean decompositores)
            throws Exception {
        return analisador.maiorCaminho(origem, destino, decompositores);
    }

    public String menorCaminho(int origem, int destino, boolean decompositores)
            throws Exception {
        return analisador.menorCaminho(origem, destino, decompositores);
    }

    public String melhorCaminhoEnergetico(int origem, int destino,
                                          boolean menor, boolean custo, boolean decompositores)
            throws Exception {
        return analisador.melhorCaminhoEnergetico(origem, destino, menor, custo, decompositores);
    }

    public String ciclos(boolean decompositores) {
        return analisador.listarCiclos(decompositores);
    }

    public String maiorCiclo(boolean decompositores) {
        return analisador.maiorCiclo(decompositores);
    }

    public String menorCiclo(boolean decompositores) {
        return analisador.menorCiclo(decompositores);
    }

    public String avaliarBemEstar(int id) throws EspecieNaoEncontradaException {
        return analisador.avaliarBemEstar(id);
    }
}

