package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void adicionarAoCarrinho(Pedido pedido, Produto produto, int quantidade) {

    if (quantidade <= 0) {
        System.out.println("Quantidade inválida.");
        return;
    }

    if (produto.getEstoque() < quantidade) {
        System.out.println("Estoque insuficiente.");
        return;
    }

    for (ItemPedido item : pedido.getItens()) {

        if (item.getProduto().getId() == produto.getId()) {
            item.setQuantidade(item.getQuantidade() + quantidade);
            return;
        }

    }

    pedido.getItens().add(new ItemPedido(produto, quantidade));
    }
}