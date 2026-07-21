package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import modelos.Cliente;
import modelos.ItemPedido;
import modelos.Pedido;
import modelos.Produto;
import utils.ConectaDB;

public class PedidoDao {

    private ProdutoDao produtoDao = new ProdutoDao();
    private ClienteDao clienteDao = new ClienteDao();

    public Pedido criarPedido(Cliente cliente) {
        return new Pedido(cliente);
    }

    public Pedido consultar(int id) {
        Pedido pedido = null;
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement(
                    "select * from tb_pedidos where id = ?");
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Cliente cliente = clienteDao.consultar(rs.getInt("cliente_id"));

                pedido = new Pedido(
                        rs.getInt("id"),
                        cliente,
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getString("status"),
                        carregarItens(con, id));
            }

            rs.close();
            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }

    public List<Pedido> consultar() {
        List<Pedido> pedidos = new ArrayList<>();
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement(
                    "select * from tb_pedidos");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Cliente cliente = clienteDao.consultar(rs.getInt("cliente_id"));

                pedidos.add(new Pedido(
                        rs.getInt("id"),
                        cliente,
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getString("status"),
                        carregarItens(con, rs.getInt("id"))));
            }

            rs.close();
            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    private List<ItemPedido> carregarItens(Connection con, int pedidoId) throws SQLException {
        List<ItemPedido> itens = new ArrayList<>();

        PreparedStatement stm = con.prepareStatement(
                "select * from tb_itens_pedido where pedido_id = ?");
        stm.setInt(1, pedidoId);

        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            Produto produto = produtoDao.consultar(rs.getInt("produto_id"));
            itens.add(new ItemPedido(produto, rs.getInt("quantidade")));
        }

        rs.close();
        stm.close();

        return itens;
    }

    public boolean adicionarAoCarrinho(Pedido pedido, Produto produto, int quantidade) {

    if (quantidade <= 0) {
        System.out.println("Quantidade inválida.");
        return false;
    }

    int quantidadeNoCarrinho = 0;

    for (ItemPedido item : pedido.getItens()) {

        if (item.getProduto().getId() == produto.getId()) {
            quantidadeNoCarrinho = item.getQuantidade();
            break;
        }

    }

    if (quantidadeNoCarrinho + quantidade > produto.getEstoque()) {
        System.out.println("Estoque insuficiente.");
        return false;
    }

    for (ItemPedido item : pedido.getItens()) {

        if (item.getProduto().getId() == produto.getId()) {

            item.setQuantidade(item.getQuantidade() + quantidade);
            return true;

        }

    }

    pedido.getItens().add(new ItemPedido(produto, quantidade));

    return true;
    }

    public boolean removerDoCarrinho(Pedido pedido, int produtoId, int quantidade) {

    for (ItemPedido item : pedido.getItens()) {

        if (item.getProduto().getId() == produtoId) {

            if (quantidade <= 0) {
                System.out.println("Quantidade inválida.");
                return false;
            }

            if (quantidade > item.getQuantidade()) {
                System.out.println("Você possui apenas "
                        + item.getQuantidade()
                        + " unidades no carrinho.");
                return false;
            }

            item.setQuantidade(item.getQuantidade() - quantidade);

            item.getProduto().setEstoque(
                    item.getProduto().getEstoque() + quantidade);

            if (item.getQuantidade() == 0) {
                pedido.getItens().remove(item);
            }

            return true;
        }
    }

    System.out.println("Produto não encontrado.");
    return false;
    }

    public Pedido finalizarPedido(Pedido pedido) {

        if (pedido.getItens().isEmpty()) {
            System.out.println("Não é possível finalizar um pedido sem itens.");
            return null;
        }

        String sqlPedido = "insert into tb_pedidos(cliente_id, data, status) values(?,?,?)";
        String sqlItem = "insert into tb_itens_pedido(pedido_id, produto_id, quantidade, preco_unitario) values(?,?,?,?)";

        Connection con = ConectaDB.conectar();

        try {

            con.setAutoCommit(false);

            PreparedStatement stmPedido = con.prepareStatement(
                    sqlPedido,
                    Statement.RETURN_GENERATED_KEYS);

            stmPedido.setInt(1, pedido.getCliente().getId());
            stmPedido.setTimestamp(2, Timestamp.valueOf(pedido.getData()));
            stmPedido.setString(3, Pedido.FINALIZADO);

            stmPedido.execute();

            ResultSet rs = stmPedido.getGeneratedKeys();

            if (rs.next()) {
                pedido.setId(rs.getInt(1));
            }

            rs.close();
            stmPedido.close();

            PreparedStatement stmItem = con.prepareStatement(sqlItem);

            for (ItemPedido item : pedido.getItens()) {

                stmItem.setInt(1, pedido.getId());
                stmItem.setInt(2, item.getProduto().getId());
                stmItem.setInt(3, item.getQuantidade());
                stmItem.setDouble(4, item.getProduto().getPreco());

                stmItem.addBatch();
            }

            stmItem.executeBatch();
            stmItem.close();

            con.commit();
            con.close();

            for (ItemPedido item : pedido.getItens()) {
                produtoDao.baixarEstoque(
                        item.getProduto().getId(),
                        item.getQuantidade());
            }

            pedido.setStatus(Pedido.FINALIZADO);

            return pedido;

        } catch (SQLException e) {

            e.printStackTrace();

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }

    public boolean clientePossuiPedidos(int clienteId) {

    String sql = "SELECT COUNT(*) FROM tb_pedidos WHERE cliente_id = ?";

    Connection con = ConectaDB.conectar();

    try {

        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, clienteId);

        ResultSet rs = stm.executeQuery();

        rs.next();

        boolean possuiPedidos = rs.getInt(1) > 0;

        rs.close();
        stm.close();
        con.close();

        return possuiPedidos;

    } catch (SQLException e) {
        e.printStackTrace();
        return true;
    }
    }
}