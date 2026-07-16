package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import interfaces.ICRUD;
import modelos.Cliente;
import utils.ConectaDB;

public class ClienteDao implements ICRUD<Cliente> {

    @Override
    public Cliente salvar(Cliente c) {
        String sql = "insert into tb_clientes(cpf, nome, email, rua, numero, bairro, cep, cidade, estado) "
                + "values(?,?,?,?,?,?,?,?,?)";
        Connection con = ConectaDB.conectar();
        try {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, c.getCpf());
            stm.setString(2, c.getNome());
            stm.setString(3, c.getEmail());
            stm.setString(4, c.getRua());
            stm.setString(5, c.getNumero());
            stm.setString(6, c.getBairro());
            stm.setString(7, c.getCep());
            stm.setString(8, c.getCidade());
            stm.setString(9, c.getEstado());
            stm.execute();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }
            rs.close();
            stm.close();
            con.close();
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_clientes WHERE id = ?";
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
    public void alterar(Cliente c) {
        String sql = "update tb_clientes set cpf=?, nome=?, email=?, rua=?, numero=?, "
                + "bairro=?, cep=?, cidade=?, estado=? where id=?";
        Connection con = ConectaDB.conectar();
        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, c.getCpf());
            stm.setString(2, c.getNome());
            stm.setString(3, c.getEmail());
            stm.setString(4, c.getRua());
            stm.setString(5, c.getNumero());
            stm.setString(6, c.getBairro());
            stm.setString(7, c.getCep());
            stm.setString(8, c.getCidade());
            stm.setString(9, c.getEstado());
            stm.setInt(10, c.getId());
            stm.execute();
            stm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cliente consultar(int id) {
        Cliente cliente = null;
        Connection con = ConectaDB.conectar();
        try {
            PreparedStatement stm = con.prepareStatement("select * from tb_clientes where id = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                cliente = mapear(rs);
            }
            rs.close();
            stm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    @Override
    public List<Cliente> consultar() {
        List<Cliente> clientes = new ArrayList<>();
        Connection con = ConectaDB.conectar();
        try {
            PreparedStatement stm = con.prepareStatement("select * from tb_clientes");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
            rs.close();
            stm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"), rs.getString("cpf"), rs.getString("nome"),
                rs.getString("email"), rs.getString("rua"), rs.getString("numero"),
                rs.getString("bairro"), rs.getString("cep"), rs.getString("cidade"),
                rs.getString("estado"));
    }
}