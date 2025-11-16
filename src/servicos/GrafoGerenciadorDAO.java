package servicos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;

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
            return mapper.readValue(f, GrafoGerenciador.class);
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados, usando gerenciador vazio.");
            System.out.println("Detalhes: " + e.getMessage());
            return new GrafoGerenciador();
        }
    }
}
