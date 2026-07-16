import java.util.Scanner;

import dao.ProdutoDao;
import modelos.Produto;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProdutoDao produtoDao = new ProdutoDao();

    public static void main(String[] args) {
        int opcao;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Cadastrar produto");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    static void cadastrarProduto() {
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Preço: ");
        double preco = Double.parseDouble(sc.nextLine());

        Produto p = new Produto(descricao, preco);
        Produto salvo = produtoDao.salvar(p);

        if (salvo != null) {
            System.out.println("Produto cadastrado! ID: " + salvo.getId());
        } else {
            System.out.println("Erro ao cadastrar produto.");
        }
    }
}