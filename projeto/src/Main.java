import java.util.Scanner;

import dao.ProdutoDao;
import modelos.Produto;
import dao.ClienteDao;
import modelos.Cliente;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProdutoDao produtoDao = new ProdutoDao();
    static ClienteDao clienteDao = new ClienteDao();

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

        System.out.println("Estoque: ");
        int estoque = Integer.parseInt(sc.nextLine());

        Produto p = new Produto(descricao, preco, estoque);
        Produto salvo = produtoDao.salvar(p);

        if (salvo != null) {
            System.out.println("Produto cadastrado! ID: " + salvo.getId());
        } else {
            System.out.println("Erro ao cadastrar produto.");
        }
    }

    static void cadastrarCliente() {
    System.out.print("CPF: ");
    String cpf = sc.nextLine();

    System.out.print("Nome: ");
    String nome = sc.nextLine();

    System.out.print("Email: ");
    String email = sc.nextLine();

    System.out.print("Rua: ");
    String rua = sc.nextLine();

    System.out.print("Número: ");
    String numero = sc.nextLine();

    System.out.print("Bairro: ");
    String bairro = sc.nextLine();

    System.out.print("CEP: ");
    String cep = sc.nextLine();

    System.out.print("Cidade: ");
    String cidade = sc.nextLine();

    System.out.print("Estado: ");
    String estado = sc.nextLine();

    Cliente cliente = new Cliente(
            cpf,
            nome,
            email,
            rua,
            numero,
            bairro,
            cep,
            cidade,
            estado
    );

    clienteDao.salvar(cliente);

    System.out.println("Cliente cadastrado.");
    }
}