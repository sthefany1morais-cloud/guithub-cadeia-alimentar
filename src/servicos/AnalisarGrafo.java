package servicos;

import model.Aresta;
import model.especies.Decompositor;
import model.especies.Especie;
import execoes.CaminhoInexistenteException;
import execoes.EspecieNaoEncontradaException;

import java.util.*;

public class AnalisarGrafo {
    GrafoEcologico grafo;

    public AnalisarGrafo() {
    }

    public AnalisarGrafo(GrafoEcologico grafo){
        this.grafo = grafo;
    }

    public List<List<Especie>> detectarCiclos(boolean considerarDecompositores) {
        List<List<Especie>> ciclosEncontrados = new ArrayList<>();

        for (Especie inicio : grafo.getEspecies()) {
            dfsCiclos(inicio, new HashSet<>(), new ArrayList<>(), ciclosEncontrados, considerarDecompositores);
        }

        return removerCiclosDuplicados(ciclosEncontrados);
    }

    private void dfsCiclos(Especie atual,
                           Set<Especie> pilha,
                           List<Especie> caminho,
                           List<List<Especie>> ciclos,
                           boolean considerarDecompositores) {


        if (pilha.contains(atual)) {

            int idx = caminho.indexOf(atual);
            if (idx != -1) {
                List<Especie> ciclo = new ArrayList<>(caminho.subList(idx, caminho.size()));
                ciclos.add(ciclo);
            }
            return;
        }

        if (caminho.contains(atual)) {
            return;
        }

        pilha.add(atual);
        caminho.add(atual);

        for (Aresta a : atual.getPresas()) {
            Especie proxima = a.getPresa();

            if (!considerarDecompositores && proxima instanceof Decompositor) {
                continue;
            }

            dfsCiclos(proxima, pilha, caminho, ciclos, considerarDecompositores);
        }

        pilha.remove(atual);
        caminho.remove(caminho.size() - 1);
    }

    private List<List<Especie>> removerCiclosDuplicados(List<List<Especie>> ciclos) {
        Set<String> vistos = new HashSet<>();
        List<List<Especie>> unicos = new ArrayList<>();

        for (List<Especie> ciclo : ciclos) {
            if (ciclo == null || ciclo.isEmpty()) continue;
            String canonico = gerarFormaCanonicaCiclo(ciclo);
            if (vistos.add(canonico)) {
                unicos.add(ciclo);
            }
        }
        return unicos;
    }

    private String gerarFormaCanonicaCiclo(List<Especie> ciclo) {
        int menorIndex = 0;
        int menorId = grafo.getEspecies().indexOf(ciclo.get(0));

        for (int i = 1; i < ciclo.size(); i++) {
            int idAtual = grafo.getEspecies().indexOf(ciclo.get(i));
            if (idAtual < menorId) {
                menorId = idAtual;
                menorIndex = i;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ciclo.size(); i++) {
            Especie e = ciclo.get((menorIndex + i) % ciclo.size());
            sb.append(grafo.getEspecies().indexOf(e));
            if (i < ciclo.size() - 1) sb.append("->");
        }
        return sb.toString();
    }

    private String formatarCiclo(List<Especie> ciclo) {
        if (ciclo == null || ciclo.isEmpty()) return "(vazio)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ciclo.size(); i++) {
            sb.append(ciclo.get(i).getNome());
            if (i < ciclo.size() - 1) sb.append(" -> ");
        }
        sb.append(" -> ").append(ciclo.get(0).getNome());
        return sb.toString();
    }

    public String listarCiclos(boolean considerarDecompositores) {
        List<List<Especie>> ciclos = detectarCiclos(considerarDecompositores);
        if (ciclos.isEmpty()) return "Nenhum ciclo encontrado.";

        StringBuilder sb = new StringBuilder("Ciclos encontrados:\n\n");
        for (int i = 0; i < ciclos.size(); i++) {
            List<Especie> ciclo = ciclos.get(i);
            sb.append("Ciclo ").append(i + 1).append(":\n")
                    .append(formatarCiclo(ciclo)).append("\n")
                    .append("Tamanho: ").append(ciclo.size()).append("\n\n");
        }
        return sb.toString();
    }

    public String maiorCiclo(boolean considerarDecompositores) {
        List<List<Especie>> ciclos = detectarCiclos(considerarDecompositores);
        if (ciclos.isEmpty()) return "Nenhum ciclo encontrado.";
        List<Especie> maior = ciclos.stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(null);

        return "Maior ciclo:\n" +
                formatarCiclo(maior) + "\n" +
                "Tamanho: " + maior.size() + "\n";
    }

    public String menorCiclo(boolean considerarDecompositores) {
        List<List<Especie>> ciclos = detectarCiclos(considerarDecompositores);
        if (ciclos.isEmpty()) return "Nenhum ciclo encontrado.";
        List<Especie> menor = ciclos.stream()
                .min(Comparator.comparingInt(List::size))
                .orElse(null);

        return "Menor ciclo:\n" +
                formatarCiclo(menor) + "\n" +
                "Tamanho: " + menor.size() + "\n";
    }

    public String maiorCaminho(int idOrigem, int idDestino, boolean considerarDecompositores)
            throws EspecieNaoEncontradaException, CaminhoInexistenteException {

        Especie origem = grafo.getEspeciePorId(idOrigem);
        Especie destino = grafo.getEspeciePorId(idDestino);

        List<Especie> melhorCaminho = new ArrayList<>();
        List<Especie> caminhoAtual = new ArrayList<>();
        Set<Especie> visitado = new HashSet<>();

        dfsMaiorCaminho(origem, destino, considerarDecompositores, visitado, caminhoAtual, melhorCaminho);

        if (melhorCaminho.isEmpty()) {
            throw new CaminhoInexistenteException(
                    "Não existe caminho válido de " + origem.getNome() + " até " + destino.getNome()
            );
        }

        return reconstruirCaminho(melhorCaminho, "Maior caminho: ");
    }

    private void dfsMaiorCaminho(Especie atual,
                                 Especie destino,
                                 boolean considerarDecompositores,
                                 Set<Especie> visitado,
                                 List<Especie> caminhoAtual,
                                 List<Especie> melhorCaminho) {

        visitado.add(atual);
        caminhoAtual.add(atual);

        if (atual.equals(destino) && caminhoAtual.size() > 1) {
            if (caminhoAtual.size() > melhorCaminho.size()) {
                melhorCaminho.clear();
                melhorCaminho.addAll(new ArrayList<>(caminhoAtual));
            }
        }

        for (Aresta a : atual.getPresas()) {
            Especie proxima = a.getPresa();

            if (!considerarDecompositores && proxima instanceof Decompositor) {
                continue;
            }

            if (!visitado.contains(proxima)) {
                dfsMaiorCaminho(proxima, destino, considerarDecompositores,
                        visitado, caminhoAtual, melhorCaminho);
            }
        }

        caminhoAtual.remove(caminhoAtual.size() - 1);
        visitado.remove(atual);
    }

    public String menorCaminho(int idOrigem, int idDestino, boolean considerarDecompositores)
            throws CaminhoInexistenteException, EspecieNaoEncontradaException {

        Especie origem = grafo.getEspeciePorId(idOrigem);
        Especie destino = grafo.getEspeciePorId(idDestino);

        Queue<Especie> fila = new LinkedList<>();
        Map<Especie, Especie> anterior = new HashMap<>();
        Set<Especie> visitado = new HashSet<>();

        fila.add(origem);
        visitado.add(origem);

        while (!fila.isEmpty()) {
            Especie atual = fila.poll();

            for (Aresta a : atual.getPresas()) {
                Especie proxima = a.getPresa();

                if (!considerarDecompositores && proxima instanceof Decompositor) {
                    continue;
                }

                if (!visitado.contains(proxima)) {
                    visitado.add(proxima);
                    anterior.put(proxima, atual);
                    fila.add(proxima);

                    if (proxima.equals(destino)) {
                        return reconstruirCaminho(anterior, origem, destino);
                    }
                }
            }
        }

        throw new CaminhoInexistenteException(
                "Não existe caminho de " + origem.getNome() + " para " + destino.getNome()
        );
    }

    public String melhorCaminhoEnergetico(int idOrigem, int idDestino, boolean menor, boolean custoEnergetico, boolean considerarDecompositores)
            throws EspecieNaoEncontradaException, CaminhoInexistenteException {

        Especie origem = grafo.getEspeciePorId(idOrigem);
        Especie destino = grafo.getEspeciePorId(idDestino);

        FilaDePrioridade fila = (new FilaDePrioridade(menor));

        Map<Especie, Integer> distancia = new HashMap<>();
        Map<Especie, Especie> anterior = new HashMap<>();
        Set<Especie> visitado = new HashSet<>();

        distancia.put(origem, 0);
        fila.adicionar(origem, 0);

        while (!fila.vazia()) {

            Item itemAtual = fila.poll();
            Especie atual = itemAtual.getEspecie();

            if (visitado.contains(atual)) {
                continue;
            }

            visitado.add(atual);

            for (Aresta a : atual.getPresas()) {
                Especie proxima = a.getPresa();

                if (!considerarDecompositores && proxima instanceof Decompositor) {
                    continue;
                }

                int custoAresta = custoEnergetico ? (a.getCustoEnergetico()) : (a.getGanhoEnergeticoLiquido());
                int custoAtual = distancia.get(atual);
                int novoCusto = custoAtual + custoAresta;

                if (!distancia.containsKey(proxima) || (menor? (novoCusto < distancia.get(proxima)) : (novoCusto > distancia.get(proxima)))) {
                    distancia.put(proxima, novoCusto);
                    anterior.put(proxima, atual);
                    fila.adicionar(proxima, novoCusto);

                    if (proxima.equals(destino)) {
                        return reconstruirCaminho(anterior, origem, destino)
                                + "\nEnergia: " + novoCusto;
                    }
                }
            }
        }

        throw new CaminhoInexistenteException(
                "Não existe caminho energético de " + origem.getNome() + " até " + destino.getNome()
        );
    }

    private String reconstruirCaminho(Map<Especie, Especie> anterior, Especie origem, Especie destino){
        List<Especie> caminho = new ArrayList<>();
        Especie atual = destino;

        while(atual != null){
            caminho.add(atual);
            atual = anterior.get(atual);
        }

        Collections.reverse(caminho);

        StringBuilder sb = new StringBuilder("Melhor caminho: ");
        for(int i=0; i<caminho.size(); i++){
            sb.append(caminho.get(i).getNome());
            if(i < caminho.size() - 1) sb.append(" -> ");
        }
        return sb.toString();
    }

    private String reconstruirCaminho(List<Especie> caminho, String titulo) {
        StringBuilder sb = new StringBuilder(titulo);
        for (int i = 0; i < caminho.size(); i++) {
            sb.append(caminho.get(i).getNome());
            if (i < caminho.size() - 1) sb.append(" -> ");
        }
        sb.append("\nTotal de espécies no caminho: ").append(caminho.size());
        return sb.toString();
    }
}
