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
            System.out.println();
            System.out.println();
            System.out.println("       MENU");
            System.out.println("número(1): Produtos");
            System.out.println("número(2): Clientes");
            System.out.println("número(3): Pedidos");
            System.out.println("número(0): Sair");
            System.out.print("Número: ");

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

        System.out.print("Estoque: ");
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

        System.out.print("CEP: ");
        String cep = sc.nextLine();

        System.out.print("Rua: ");
        String rua = sc.nextLine();

        System.out.print("Número: ");
        String numero = sc.nextLine();

        System.out.print("Bairro: ");
        String bairro = sc.nextLine();

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
            System.out.println();
            System.out.println();
            System.out.println("     Clientes");
            System.out.println("número(1): Cadastrar");
            System.out.println("número(2): Listar");
            System.out.println("número(3): Consultar por ID");
            System.out.println("número(4): Alterar");
            System.out.println("número(5): Excluir");
            System.out.println("número(0): Voltar");
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

        System.out.print("Novo cpf: ");
        c.setNome(sc.nextLine());

        System.out.print("Novo nome: ");
        c.setNome(sc.nextLine());

        System.out.print("Novo email: ");
        c.setEmail(sc.nextLine());

        System.out.print("Novo cep: ");
        c.setCidade(sc.nextLine());

        System.out.print("Nova rua: ");
        c.setCidade(sc.nextLine());

        System.out.print("Nova número: ");
        c.setCidade(sc.nextLine());

        System.out.print("Novo bairro: ");
        c.setCidade(sc.nextLine());

        System.out.print("Nova cidade: ");
        c.setCidade(sc.nextLine());

        System.out.print("Novo estado: ");
        c.setCidade(sc.nextLine());

        clienteDao.alterar(c);

        if (carrinhoAtual != null &&
                carrinhoAtual.getCliente().getId() == c.getId()) {

            carrinhoAtual.setCliente(c);
        }

        System.out.println("Cliente alterado.");
    }

    static void excluirCliente() {

        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());

        if (pedidoDao.clientePossuiPedidos(id)) {
            System.out.println("Não é possível excluir este cliente, pois ele possui pedidos cadastrados.");
            return;
        }

        clienteDao.deletar(id);

        System.out.println("Cliente excluído.");
    }

    static void menuPedido() {

        int opcao;

        do {
            System.out.println();
            System.out.println();
            System.out.println("         Pedidos");
            System.out.println("número(1): Iniciar novo pedido");
            System.out.println("número(2): Adicionar produto ao carrinho");
            System.out.println("número(3): Remover produto do carrinho");
            System.out.println("número(4): Ver carrinho atual");
            System.out.println("número(5): Finalizar pedido");
            System.out.println("número(6): Consultar pedido por ID");
            System.out.println("número(7): Listar todos os pedidos");
            System.out.println("número(0): Voltar");

            System.out.print("Escolha: ");
            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {

                case 1:
                    iniciarPedido();
                    break;

                case 2:
                    adicionarAoCarrinho();
                    break;

                case 3:
                    removerDoCarrinho();
                    break;

                case 4:
                    verCarrinho();
                    break;

                case 5:
                    finalizarPedido();
                    break;

                case 6:
                    consultarPedido();
                    break;

                case 7:
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

        if (cliente == null) {
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

    static void adicionarAoCarrinho() {

        if (carrinhoAtual == null) {
            System.out.println("Nenhum pedido aberto.");
            return;
        }

        System.out.print("ID do produto: ");
        int id = Integer.parseInt(sc.nextLine());

        Produto produto = produtoDao.consultar(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Quantidade: ");
        int quantidade = Integer.parseInt(sc.nextLine());

        boolean adicionou = pedidoDao.adicionarAoCarrinho(carrinhoAtual, produto, quantidade);

        if (adicionou) {
            System.out.println("Produto adicionado ao carrinho.");
        }
    }

    static void removerDoCarrinho() {

        if (carrinhoAtual == null) {
            System.out.println("Nenhum pedido aberto.");
            return;
        }

        System.out.print("ID do produto: ");
        int idProduto = Integer.parseInt(sc.nextLine());

        System.out.print("Quantidade para remover: ");
        int quantidade = Integer.parseInt(sc.nextLine());

        boolean removeu = pedidoDao.removerDoCarrinho(
                carrinhoAtual,
                idProduto,
                quantidade);

        if (removeu) {
            System.out.println("Produto removido do carrinho.");
        }

    }

    static void finalizarPedido() {

        if (carrinhoAtual == null) {
            System.out.println("Nenhum pedido aberto.");
            return;
        }

        Pedido pedido = pedidoDao.finalizarPedido(carrinhoAtual);

        if (pedido != null) {

            System.out.println("Pedido finalizado!");
            System.out.println(pedido);

            carrinhoAtual = null;
        }

    }

    static void verCarrinho() {

        if (carrinhoAtual == null) {
            System.out.println("Nenhum carrinho aberto.");
            return;
        }

        System.out.println(carrinhoAtual);

        carrinhoAtual.getItens().forEach(System.out::println);

    }

}