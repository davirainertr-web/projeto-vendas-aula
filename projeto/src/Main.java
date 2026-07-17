import java.util.List;
import java.util.Scanner;

import dao.ClienteDao;
import dao.PedidoDao;
import dao.ProdutoDao;

import modelos.Cliente;
import modelos.Pedido;
import modelos.Produto;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProdutoDao produtoDao = new ProdutoDao();
    static ClienteDao clienteDao = new ClienteDao();
     static PedidoDao pedidoDao = new PedidoDao();

    static Pedido carrinhoAtual = null;

    public static void main(String[] args) {
        int opcao;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Produtos");
            System.out.println("2 - Clientes");
            System.out.println("3 - Pedidos");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 3:
                    menuPedido();
                    break;
                case 2:
                    menuCliente();
                    break;

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
                estado);

        clienteDao.salvar(cliente);

        System.out.println("Cliente cadastrado.");
    }

    static void menuCliente() {
        int opcao;

        do {
            System.out.println("\n--- Clientes ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Consultar por ID");
            System.out.println("4 - Alterar");
            System.out.println("5 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;

                case 2:
                    listarClientes();
                    break;

                case 3:
                    consultarCliente();
                    break;

                case 4:
                    alterarCliente();
                    break;

                case 5:
                    excluirCliente();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    static void listarClientes() {

        List<Cliente> clientes = clienteDao.consultar();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (Cliente c : clientes) {
            System.out.println(c);
        }

    }

    static void consultarCliente() {

        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());

        Cliente c = clienteDao.consultar(id);

        if (c != null)
            System.out.println(c);
        else
            System.out.println("Cliente não encontrado.");

    }

    static void alterarCliente() {

        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());

        Cliente c = clienteDao.consultar(id);

        if (c == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        c.setNome(sc.nextLine());

        System.out.print("Novo email: ");
        c.setEmail(sc.nextLine());

        System.out.print("Nova cidade: ");
        c.setCidade(sc.nextLine());

        clienteDao.alterar(c);

        System.out.println("Cliente alterado.");
    }

    static void excluirCliente() {

        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());

        clienteDao.deletar(id);

        System.out.println("Cliente excluído.");

    }

    static void menuPedido() {

    int opcao;

    do {

        System.out.println("\n--- Pedidos ---");
        System.out.println("1 - Novo pedido");
        System.out.println("2 - Consultar pedido");
        System.out.println("3 - Listar pedidos");
        System.out.println("0 - Voltar");

        System.out.print("Escolha: ");
        opcao = Integer.parseInt(sc.nextLine());

        switch (opcao) {

            case 1:
                iniciarPedido();
                break;

            case 2:
                consultarPedido();
                break;

            case 3:
                listarPedidos();
                break;

            case 0:
                break;

            default:
                System.out.println("Opção inválida.");
        }

    } while (opcao != 0);
    }

    static void iniciarPedido() {

    System.out.print("ID do cliente: ");
    int id = Integer.parseInt(sc.nextLine());

    Cliente cliente = clienteDao.consultar(id);

    if(cliente == null){
        System.out.println("Cliente não encontrado.");
        return;
    }

    carrinhoAtual = pedidoDao.criarPedido(cliente);

    System.out.println("Pedido criado.");
    }

    static void consultarPedido() {

    System.out.print("ID do pedido: ");
    int id = Integer.parseInt(sc.nextLine());

    Pedido p = pedidoDao.consultar(id);

    if (p != null) {
        System.out.println(p);

        // mostra os itens do pedido
        p.getItens().forEach(System.out::println);

    } else {
        System.out.println("Pedido não encontrado.");
    }
    }

    static void listarPedidos() {

    List<Pedido> pedidos = pedidoDao.consultar();

    if (pedidos.isEmpty()) {
        System.out.println("Nenhum pedido cadastrado.");
        return;
    }

    for (Pedido p : pedidos) {
        System.out.println(p);
    }
    }

}