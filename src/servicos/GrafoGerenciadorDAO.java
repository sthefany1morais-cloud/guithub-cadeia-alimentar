package servicos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Aresta;
import model.especies.*;


import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

public class GrafoGerenciadorDAO {

    private static final String ARQUIVO = "cibacly.json";
    private final ObjectMapper mapper;

    public GrafoGerenciadorDAO() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public void salvar(GrafoGerenciador gerenciador) {
        try {
            mapper.writeValue(new File(ARQUIVO), gerenciador);
            System.out.println("Dados salvos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public GrafoGerenciador carregar() {
        try {
            File f = new File(ARQUIVO);

            if (!f.exists()) {
                System.out.println("Nenhum arquivo salvo encontrado. Criando novo gerenciador...");
                return new GrafoGerenciador();
            }

            // Carrega somente o JSON
            GrafoGerenciador gerenciador = mapper.readValue(f, GrafoGerenciador.class);

            // --- MUITO IMPORTANTE ---
            // Reconstrói ligações das arestas (predador/presa)
            reconstruirReferencias(gerenciador);

            return gerenciador;

        } catch (Exception e) {
            System.out.println("Erro ao carregar dados, usando gerenciador vazio.");
            System.out.println("Detalhes: " + e.getMessage());
            return new GrafoGerenciador();
        }
    }


    private void reconstruirReferencias(GrafoGerenciador g) {
        g.getGrafos().forEach(grafo -> {
            Map<String, Especie> mapa = grafo.getEspecies()
                    .stream()
                    .collect(Collectors.toMap(Especie::getNome, e -> e));

            for (Especie esp : grafo.getEspecies()) {
                // presas: a.predador = esp, a.presa = mapa.get(nomePresa)
                for (Aresta a : esp.getPresas()) {
                    a.setPredador(esp);
                    String nomeP = a.getNomePresa();
                    if (nomeP != null) {
                        Especie presaReal = mapa.get(nomeP);
                        a.setPresa(presaReal);
                    }
                }

                // predadores: a.presa = esp, a.predador = mapa.get(nomePredador)
                for (Aresta a : esp.getPredadores()) {
                    a.setPresa(esp);
                    String nomePred = a.getNomePredador();
                    if (nomePred != null) {
                        Especie predReal = mapa.get(nomePred);
                        a.setPredador(predReal);
                    }
                }
            }
        });
    }

}
