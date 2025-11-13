package servicos;

import model.Aresta;
import model.especies.Especie;
import execoes.EspecieJaExisteException;
import execoes.EspecieNaoEncontradaException;
import execoes.ValorEnergeticoInvalidoException;

import java.util.ArrayList;
import java.util.List;

public class GrafoEcologico {
    private List<Especie> especies;
    private String nome;

    public GrafoEcologico(String nome){
        this.nome = nome;
        this.especies = new ArrayList<>();
    }

    public void adicionarEspecie(Especie especie) throws EspecieJaExisteException {
        for (Especie e: this.especies) {
            if (especie.getNome().equalsIgnoreCase(e.getNome())){
                throw new EspecieJaExisteException("A espécie " + especie.getNome() + " já está cadastrada na cadeia alimentar.");
            }
        }
        this.especies.add(especie);
    }

    public void adicionarPredacao(int idPredador, int idPresa, int custo) throws EspecieNaoEncontradaException, ValorEnergeticoInvalidoException {

        Especie predador = getEspeciePorId(idPredador);
        Especie presa = getEspeciePorId(idPresa);

        if (custo < 0){
            throw new ValorEnergeticoInvalidoException("O custo energético não pode ser negativo.");
        }
        if (custo == 0){
            throw new ValorEnergeticoInvalidoException("O custo energético não pode ser zero.");
        }
        predador.adicionarPresa(presa,custo);
    }

    public void adicionarPredacao(int idPredador, int idPresa) throws EspecieNaoEncontradaException{

        Especie predador = getEspeciePorId(idPredador);
        Especie presa = getEspeciePorId(idPresa);

        predador.adicionarPresa(presa);
    }

    public Especie getEspeciePorId(int id) throws EspecieNaoEncontradaException {
        if (this.especies.isEmpty()){
            throw new EspecieNaoEncontradaException("Nenhuma espécie foi cadastrada ainda.");
        }
        else if (id < 0 || id > this.especies.size()-1){
            throw new EspecieNaoEncontradaException("Id inválido. Digite um número entre 1 e "+ this.especies.size());
        }
        return this.especies.get(id-1);
    }

    public int getIdPorEspecie(Especie e) throws EspecieNaoEncontradaException{
        if (this.especies.isEmpty()){
            throw new EspecieNaoEncontradaException("Nenhuma espécie foi cadastrada ainda.");
        } else if (this.especies.contains(e)){
            return this.especies.indexOf(e);
        }
        else {
            throw new EspecieNaoEncontradaException("Espécie não encontrada.");
        }
    }

    public List<Especie> getEspecies() {
        return especies;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> listarEspeciesSimples(){
        List<String> lista = new ArrayList<>();
        for (int i = 0; i< this.especies.size(); i++){
            Especie e = this.especies.get(i);
            lista.add(e.especieSimplificada(i+1));
        }
        return lista;
    }

    public List<String> listarPresas(Especie e){
        List<String> lista = new ArrayList<>();
        for (Aresta a: e.getPresas()){
            lista.add(a.toString());
        }
        return lista;
    }

    public List<String> listarPredadores(Especie e){
        List<String> lista = new ArrayList<>();
        for (Aresta a: e.getPredadores()){
            lista.add(a.toString());
        }
        return lista;
    }

    public List<String> listarArestasPredadores(Especie e){
        List<String> lista = new ArrayList<>();
        for (int i = 0; i< e.getPredadores().size(); i++){
            Aresta a = e.getPredadores().get(i);
            lista.add(a.toString());
        }
        return lista;
    }

    public List<String> listarArestasPresas(Especie e){
        List<String> lista = new ArrayList<>();
        for (int i = 0; i< e.getPresas().size(); i++){
            Aresta a = e.getPresas().get(i);
            lista.add(a.toString());
        }
        return lista;
    }
}
