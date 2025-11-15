package servicos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GrafoGerenciadorDAO {

    private static final String ARQUIVO = "cibacly.json";
    private final Gson gson;

    public GrafoGerenciadorDAO() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void salvar(GrafoGerenciador gerenciador) {
        try (FileWriter writer = new FileWriter(ARQUIVO)) {
            gson.toJson(gerenciador, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public GrafoGerenciador carregar() {
        try (FileReader reader = new FileReader(ARQUIVO)) {
            return gson.fromJson(reader, GrafoGerenciador.class);
        } catch (Exception e) {
            System.out.println("Nenhum arquivo salvo encontrado. Criando novo gerenciador...");
            return new GrafoGerenciador();
        }
    }
}

