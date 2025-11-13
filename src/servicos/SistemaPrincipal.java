package servicos;

import model.especies.*;
import execoes.*;

public class SistemaPrincipal {
    public static void main(String[] args) {
        GrafoGerenciador gerenciador = new GrafoGerenciador();
        System.out.println("🌿 Bem-vindo ao Forma.ly - Sistema de Ecossistemas 🌿\n");

        int opcao = Menu.escolherOpcao("""
            ===== MENU PRINCIPAL =====
            1. Criar nova cadeia alimentar
            2. Listar cadeias existentes
            3. Acessar cadeia alimentar
            4. Remover cadeia alimentar
            5. Sair
            Escolha uma opção: """, 1, 5);

        try {
            switch (opcao) {
                case 1 -> criarGrafo(gerenciador);
                case 2 -> listarGrafos(gerenciador);
                case 3 -> acessarGrafo(gerenciador);
                case 4 -> removerGrafo(gerenciador);
                case 5 -> {
                    System.out.println("Encerrando o sistema. Até logo!");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void criarGrafo(GrafoGerenciador g) throws GrafoJaExisteException {
        String nome = Menu.lerString("Digite o nome do novo grafo: ");
        g.adicionarGrafo(nome);
        System.out.println("Grafo '" + nome + "' criado com sucesso!");
    }

    private static void listarGrafos(GrafoGerenciador g) {
        System.out.println("\nCadeias registradas:");
        for (String s : g.listarGrafos()) {
            System.out.println(s);
        }
        if (g.getTamanho() == 0)
            System.out.println("(nenhum grafo cadastrado)");
    }

    private static void removerGrafo(GrafoGerenciador g) throws GrafoInexistenteExeption {
        listarGrafos(g);
        if (g.getTamanho() == 0) return;
        int id = Menu.lerInt("Digite o id do grafo a remover: ") - 1;
        g.removerGrafo(id);
        System.out.println("Grafo removido com sucesso.");
    }

    private static void acessarGrafo(GrafoGerenciador g) throws GrafoInexistenteExeption {
        listarGrafos(g);
        if (g.getTamanho() == 0) return;
        int id = Menu.lerInt("Digite o id do grafo que deseja acessar: ") - 1;

        GrafoController controller = g.acessarGrafo(id);
        System.out.println("\n🌎 Acessando cadeia: " + controller.getNomeGrafo());
        menuGrafo(controller);
    }

    private static void menuGrafo(GrafoController c) {
        while (true) {
            int op = Menu.escolherOpcao("""
                ===== MENU DA CADEIA '%s' =====
                1. Adicionar espécie
                2. Criar relação de predação
                3. Editar espécie
                4. Mostrar espécies
                5. Analisar ecossistema
                0. Voltar
                Escolha uma opção: 
                """.formatted(c.getNomeGrafo()), 0, 5);

            try {
                switch (op) {
                    case 1 -> adicionarEspecie(c);
                    case 2 -> adicionarPredacao(c);
                    case 3 -> editarEspecie(c);
                    case 4 -> System.out.println(c.listarEspeciesSimples());
                    case 5 -> analisarEcossistema(c);
                    case 0 -> { return; }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void adicionarEspecie(GrafoController c) throws EspecieJaExisteException {
        int tipo = Menu.escolherOpcao("""
            Tipos disponíveis:
            1. Produtor
            2. Consumidor
            3. Decompositor
            Escolha o tipo: """, 1, 3);

        String nome = Menu.lerString("Nome da espécie: ");
        int energia = Menu.lerInt("Energia da espécie: ");

        Especie nova = switch (tipo) {
            case 1 -> new Produtor(nome, energia);
            case 2 -> new Consumidor(nome, energia);
            case 3 -> new Decompositor(nome, energia);
            default -> null;
        };

        c.adicionarEspecie(nova);
        System.out.println("Espécie adicionada com sucesso!");
    }

    private static void adicionarPredacao(GrafoController c)
            throws EspecieNaoEncontradaException, ValorEnergeticoInvalidoException {
        System.out.println(c.listarEspeciesSimples());
        int predador = Menu.lerInt("ID do predador: ");
        int presa = Menu.lerInt("ID da presa: ");
        int custo = Menu.lerInt("Custo energético (ou 0 para automático): ");

        if (custo > 0)
            c.adicionarPredacao(predador, presa, custo);
        else
            c.adicionarPredacao(predador, presa);

        System.out.println("Predação registrada com sucesso!");
    }

    private static void editarEspecie(GrafoController c) throws EspecieNaoEncontradaException {
        System.out.println(c.listarEspeciesSimples());
        int id = Menu.lerInt("ID da espécie a editar: ");
        String novoNome = Menu.lerString("Novo nome: ");
        int novaEnergia = Menu.lerInt("Nova energia: ");
        c.editarEspecie(id, novoNome, novaEnergia);
        System.out.println("Espécie atualizada!");
    }

    private static void analisarEcossistema(GrafoController c) throws Exception {
        int op = Menu.escolherOpcao("""
            ===== ANÁLISES DISPONÍVEIS =====
            1. Listar ciclos
            2. Maior ciclo
            3. Menor ciclo
            4. Maior caminho entre espécies
            5. Menor caminho entre espécies
            6. Melhor caminho energético
            7. Avaliar bem-estar de espécie
            0. Voltar
            Escolha: """, 0, 7);

        boolean decomps = Menu.confirmar("Incluir decompositores?", 's', 'n');

        switch (op) {
            case 1 -> System.out.println(c.ciclos(decomps));
            case 2 -> System.out.println(c.maiorCiclo(decomps));
            case 3 -> System.out.println(c.menorCiclo(decomps));
            case 4 -> caminhoMaiorMenor(c, true, decomps);
            case 5 -> caminhoMaiorMenor(c, false, decomps);
            case 6 -> caminhoEnergetico(c, decomps);
            case 7 -> bemEstar(c);
        }
    }

    private static void caminhoMaiorMenor(GrafoController c, boolean maior, boolean decomps)
            throws Exception {
        System.out.println(c.listarEspeciesSimples());
        int o = Menu.lerInt("ID da origem: ");
        int d = Menu.lerInt("ID do destino: ");
        String resultado = maior ? c.maiorCaminho(o, d, decomps) : c.menorCaminho(o, d, decomps);
        System.out.println(resultado);
    }

    private static void caminhoEnergetico(GrafoController c, boolean decomps)
            throws Exception {
        System.out.println(c.listarEspeciesSimples());
        int o = Menu.lerInt("ID da origem: ");
        int d = Menu.lerInt("ID do destino: ");
        boolean menor = Menu.confirmar("Buscar menor caminho energético?", 's', 'n');
        boolean custo = Menu.confirmar("Usar custo (s) ou ganho (n)?", 's', 'n');
        System.out.println(c.melhorCaminhoEnergetico(o, d, menor, custo, decomps));
    }

    private static void bemEstar(GrafoController c) throws EspecieNaoEncontradaException {
        System.out.println(c.listarEspeciesSimples());
        int id = Menu.lerInt("ID da espécie: ");
        System.out.println(c.avaliarBemEstar(id));
    }
}