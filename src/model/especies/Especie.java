package model.especies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import execoes.ValorEnergeticoInvalidoException;
import model.Aresta;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Produtor.class, name = "produtor"),
        @JsonSubTypes.Type(value = Consumidor.class, name = "consumidor"),
        @JsonSubTypes.Type(value = Decompositor.class, name = "decompositor")
})

public abstract class Especie {
    protected String nome;
    protected String tipo;
    protected int energia;
    protected List<Aresta> presas;
    protected List<Aresta> predadores;

    public Especie(){

    }

    public Especie(String nome, int energia) throws ValorEnergeticoInvalidoException {
        this.nome = nome;
        if (energia <= 0){
            throw new ValorEnergeticoInvalidoException("Valor Inválido.");
        }
        this.energia = energia;
        this.presas = new ArrayList<>();
        this.predadores = new ArrayList<>();
    }

    public void adicionarPresa(Especie presa, int custo){
        Aresta a = new Aresta(this, presa, custo);
        this.presas.add(a);
        presa.predadores.add(a);
    }
    public void adicionarPresa(Especie presa){
        Aresta a = new Aresta(this, presa);
        this.presas.add(a);
        presa.predadores.add(a);
    }

    @Override
    public String toString() {
        return "\nEspécie: "+ nome +
        "\nTipo: "+ this.tipo +
        "\nEnergia: "+ this.energia +
        "\nPresas: "+ mostrarPresas() +
        "\nPredadores: "+ mostrarPredadores();
    }

    public String especieSimplificada(int id){
        return id + " - " + this.nome + " (" + this.energia + ")";
    }

    public String especieSimplificada(){
        return this.nome + " (" + this.energia + ")";
    }

    private String mostrarPresas(){
        StringBuilder texto = new StringBuilder();
        if (this.presas.isEmpty()) {
            return "Nenhuma";
        }
            for (int i = 0; i < this.presas.size(); i++) {
                texto.append(this.presas.get(i).getPresa().getNome());
                if (i < this.presas.size() - 1) {
                    texto.append(", ");
                } else {
                    texto.append(".");
                }
            }
        return texto.toString();
    }

    private String mostrarPredadores(){
        StringBuilder texto = new StringBuilder();
        if (this.predadores.isEmpty()) {
            return "Nenhum";
        }
            for (int i = 0; i < this.predadores.size(); i++) {
                texto.append(this.predadores.get(i).getPredador().getNome());
                if (i < this.predadores.size() - 1) {
                    texto.append(", ");
                } else {
                    texto.append(".");
                }
            }
        return texto.toString();
    }

    public int getGanhoTotal() {
        if (this.presas.isEmpty()){
            return 0;
        }
         return (getPresas().stream().mapToInt(Aresta::getGanhoEnergeticoLiquido).sum());
    }

    public int getPerdaTotal() {

        if (this.predadores.isEmpty()){
            return 0;
        }

        return getPredadores().stream().mapToInt(Aresta::getCustoEnergetico).sum();  // Soma dos custos de ser presa
    }

    public String getNome() {
        return nome;
    }

    public int getEnergia() {
        return energia;
    }

    public List<Aresta> getPresas() {
        return presas;
    }

    public List<Aresta> getPredadores() {
        return predadores;
    }

    public String getTipo() {
        return tipo;
    }
}
