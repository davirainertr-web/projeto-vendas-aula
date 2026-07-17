package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import interfaces.ICRUD;
import modelos.Produto;
import utils.ConectaDB;

public class ProdutoDao implements ICRUD<Produto> {

    @Override
    public Produto salvar(Produto prod) {
        String sql = "insert into tb_produtos(descricao, preco, estoque) values(?,?,?)";
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, prod.getDescricao());
            stm.setDouble(2, prod.getPreco());
            stm.setInt(3, prod.getEstoque());
            stm.execute();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                prod.setId(rs.getInt(1));
            }

            rs.close();
            stm.close();
            con.close();

            return prod;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_produtos WHERE id = ?";
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();

            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Produto prod) {
        String sql = "update tb_produtos set descricao=?, preco=?, estoque=? where id=?";
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, prod.getDescricao());
            stm.setDouble(2, prod.getPreco());
            stm.setInt(3, prod.getEstoque());
            stm.setInt(4, prod.getId());
            stm.execute();

            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Produto consultar(int id) {
        Produto produto = null;
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement("select * from tb_produtos where id = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getInt("estoque"));
            }

            rs.close();
            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produto;
    }

    @Override
    public List<Produto> consultar() {
        List<Produto> produtos = new ArrayList<>();
        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement("select * from tb_produtos");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                produtos.add(new Produto(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getInt("estoque")));
            }

            rs.close();
            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public void baixarEstoque(int produtoId, int quantidade) {
        String sql = "update tb_produtos set estoque = estoque - ? where id = ?";

        Connection con = ConectaDB.conectar();

        try {
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, quantidade);
            stm.setInt(2, produtoId);

            stm.executeUpdate();

            stm.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}