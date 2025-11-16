package servicos;

import model.especies.*;
import execoes.*;

public class SistemaPrincipal {
    public static void main(String[] args) {

        GrafoGerenciadorDAO dao = new GrafoGerenciadorDAO();
        GrafoGerenciador gerenciador = dao.carregar();
        System.out.println("🌿 Bem-vindo ao CiBac.ly - Sistema de Ecossistemas 🌿\n");
        while (true) {
            int opcao = Menu.escolherOpcao("""
                ===== MENU PRINCIPAL =====
                1. Criar nova cadeia alimentar
                2. Listar cadeias existentes
                3. Acessar cadeia alimentar
                4. Remover cadeia alimentar
                5. Salvar e sair
                Escolha uma opção:\s""", 1, 5);

            try {
                switch (opcao) {
                    case 1 -> criarGrafo(gerenciador);
                    case 2 -> listarGrafos(gerenciador);
                    case 3 -> acessarGrafo(gerenciador);
                    case 4 -> removerGrafo(gerenciador);
                    case 5 -> {
                        dao.salvar(gerenciador);
                        System.out.println("Encerrando o sistema. Até logo!");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void criarGrafo(GrafoGerenciador g) throws GrafoJaExisteException {
        String nome = Menu.lerString("Digite o nome da nova cadeia: ");
        g.adicionarGrafo(nome);
        System.out.println("Cadeia alimentar '" + nome + "' criada com sucesso!");
    }

    private static void listarGrafos(GrafoGerenciador g) {
        System.out.println("\nCadeias registradas:");
        for (String s : g.listarGrafos()) {
            System.out.println(s);
        }
        if (g.getTamanho() == 0)
            System.out.println("(nenhuma cadeia cadastrada)");
    }

    private static void removerGrafo(GrafoGerenciador g) throws GrafoInexistenteExeption {
        listarGrafos(g);
        if (g.getTamanho() == 0) return;
        int id = Menu.lerInt("Digite o id do grafo a remover: ") - 1;
        g.removerGrafo(id);
        System.out.println("Cadeia removida com sucesso.");
    }

    private static void acessarGrafo(GrafoGerenciador g) throws GrafoInexistenteExeption {
        listarGrafos(g);
        if (g.getTamanho() == 0) return;
        int id = Menu.lerInt("Digite o id da caideia que deseja acessar: ") - 1;

        GrafoController controller = g.acessarGrafo(id);
        System.out.println("\nAcessando Cadeia alimentar: " + controller.getNomeGrafo());
        menuGrafo(controller);
    }

    private static void menuGrafo(GrafoController c) {
        while (true) {
            int op = Menu.escolherOpcao("""
                ===== MENU DA CADEIA '%s' =====
                1. Adicionar espécie
                2. Acessar uma espécie
                3. Criar relação de predação
                4. Mostrar espécies
                5. Analisar ecossistema
                6. Voltar
                Escolha uma opção:\s""".formatted(c.getNomeGrafo()), 1, 6);

            try {
                switch (op) {
                    case 1 -> adicionarEspecie(c);
                    case 2 -> acessarEspecie(c);
                    case 3 -> adicionarPredacao(c);
                    case 4 -> mostrarEspecies(c);
                    case 5 -> analisarEcossistema(c);
                    case 6 -> {return;}
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void mostrarEspecies(GrafoController c){
        if(c.getGrafo().getEspecies().isEmpty()){
            System.out.println("(Cadeia vazia.)");
        } else {
            System.out.println(c.listarEspeciesSimples());
        }
    }

    private static void adicionarEspecie(GrafoController c) throws EspecieJaExisteException, EspecieNaoEncontradaException, ValorEnergeticoInvalidoException {
        int tipo = Menu.escolherOpcao("""
            Tipos disponíveis:
            1. Produtor
            2. Consumidor
            3. Decompositor
            Escolha o tipo:\s""", 1, 3);

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
        if (Menu.confirmar("Deseja adicionar as relações de predação da espécie adora?", 'S', 'N')){
            if (Menu.confirmar("Deseja adicionar os predadores da espécie agora?", 'S', 'N')) {
                System.out.println("Lista de possíveis Predadores:\n");
                while (true) {
                    System.out.println(c.listarEspeciesSimples());
                    int predador = Menu.lerInt("ID do predador: ");
                    int custo = Menu.lerInt("Custo energético (ou 0 para automático): ");

                    if (custo > 0)
                        c.adicionarPredacao(predador, c.getGrafo().getIdPorEspecie(nova), custo);
                    else
                        c.adicionarPredacao(predador, c.getGrafo().getIdPorEspecie(nova));

                    System.out.println("Predação registrada com sucesso!");
                    if (Menu.confirmar("Deseja continuar a adicionar predadores?", 'S', 'N')) {
                        break;
                    }
                }
            }

            if (Menu.confirmar("Deseja adicionar as presas da espécie agora?", 'S', 'N')) {

                System.out.println("Lista de possíveis Presas:\n");
                while (true) {
                    System.out.println(c.listarEspeciesSimples());
                    int presa = Menu.lerInt("ID da presa: ");
                    int custo = Menu.lerInt("Custo energético (ou 0 para automático): ");

                    if (custo > 0)
                        c.adicionarPredacao(c.getGrafo().getIdPorEspecie(nova), presa, custo);
                    else
                        c.adicionarPredacao(c.getGrafo().getIdPorEspecie(nova), presa);

                    System.out.println("Predação registrada com sucesso!");
                    if (Menu.confirmar("Deseja continuar a adicionar presas?", 'S', 'N')) {
                        break;
                    }
                }
            }
        }
    }

    private static void acessarEspecie(GrafoController c) throws EspecieNaoEncontradaException{
        mostrarEspecies(c);
        int id = Menu.lerInt("Digite o id da espécie que deseja acessar: ");
        Especie esp = c.getGrafo().getEspeciePorId(id);
        System.out.println(esp);
        while (true) {
            int op = Menu.escolherOpcao("""
                ===== ESPÉCIE '%s' =====
                1. Visualizar presas
                2. Visualizar predadores
                3. Voltar
                Escolha uma opção:\s""".formatted(esp.especieSimplificada()), 1, 3);

            try {
                switch (op) {
                    case 1 -> System.out.println(c.listarPresas(id));
                    case 2 -> System.out.println(c.listarPredadores(id));
                    case 3 -> { return; }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void adicionarPredacao(GrafoController c) throws EspecieNaoEncontradaException, ValorEnergeticoInvalidoException {
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

    private static void analisarEcossistema(GrafoController c) throws Exception {
        while (true) {
            int op = Menu.escolherOpcao("""
                    ===== ANÁLISES DISPONÍVEIS =====
                    1. Listar ciclos
                    2. Maior ciclo
                    3. Menor ciclo
                    4. Melhor caminho entre duas espécies
                    5. Avaliar bem-estar de espécie
                    6. Voltar
                    Escolha:\s""", 1, 6);
            boolean decomps = false;

            if (op < 5) {
                decomps = Menu.confirmar("Incluir decompositores?", 'S', 'N');
            }
            switch (op) {
                case 1 -> System.out.println(c.ciclos(decomps));
                case 2 -> System.out.println(c.maiorCiclo(decomps));
                case 3 -> System.out.println(c.menorCiclo(decomps));
                case 4 -> caminhoMelhor(c, decomps);
                case 5 -> bemEstar(c);
                case 6 -> {return;}
            }
        }
    }

    private static void caminhoMelhor(GrafoController c, boolean decomps) throws Exception {
        System.out.println(c.listarEspeciesSimples());
        String resultado;
        int o = Menu.lerInt("ID da origem: ");
        int d = Menu.lerInt("ID do destino: ");
        boolean menor = Menu.confirmar("Deseja visualizar o menor caminho?", 'S', 'N');
        boolean arestas = Menu.confirmar("Deseja considerar arestas?", 'S', 'N');
        if (!arestas){
            if (menor){
                resultado = c.menorCaminho(o, d, decomps);
            } else {
                resultado = c.maiorCaminho(o, d, decomps);
            }
        }
        boolean nos = Menu.confirmar("Deseja considerar peso dos nós?", 'S', 'N');
        resultado = c.melhorCaminhoEnergetico(o, d, menor, nos, decomps);
        System.out.println(resultado);
    }

    private static void bemEstar(GrafoController c) throws EspecieNaoEncontradaException {
        System.out.println(c.listarEspeciesSimples());
        int id = Menu.lerInt("ID da espécie: ");
        System.out.println(c.avaliarBemEstar(id));
    }
}