package servicos;

import java.util.Scanner;

public abstract class Menu {

    private static final Scanner sc = new Scanner(System.in);

    public static int escolherOpcao(String texto, int min, int max) {

        int opcao;
        while (true) {
            System.out.print(texto);
            try {
                opcao = sc.nextInt();
                if (opcao >= min && opcao <= max){
                    break;
                }
            } catch (Exception e){
                //Ignora
            } finally {
                sc.nextLine();
            }
            System.out.println("Resposta inválida.");
        }
        return opcao;
    }

    public static boolean confirmar(String pergunta, char verdadeiro, char falso) {
        char resp;
        while (true) {
            System.out.print(pergunta + " (" + verdadeiro + "/" + falso + "): ");
            try {
                String entrada = sc.nextLine().trim().toLowerCase();
                resp = entrada.charAt(0);
                if (resp == verdadeiro || resp == falso){
                break;
                }

            } catch (Exception e){
                System.out.printf("\nDigite apenas %s ou %s.", verdadeiro, falso);
            }
        }
        return resp == verdadeiro;
    }

    public static int lerInt(String msg) {
        int num;
        do{
            System.out.print(msg);
            try{
                num = sc.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Valor inválido");
            }finally {
                sc.nextLine();
            }
        } while (true);
        return num;
    }

    public static String lerString(String msg) {
        String resposta;
        do {
            System.out.print(msg);
            try {
                resposta = sc.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Resposta inválida.");
            }
        } while (true);
        return resposta;
    }
}

