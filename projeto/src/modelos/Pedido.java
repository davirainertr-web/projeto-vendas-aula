package modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    public static final String ABERTO = "ABERTO";
    public static final String FINALIZADO = "FINALIZADO";

    private int id;
    private Cliente cliente;
    private LocalDateTime data;
    private String status;
    private List<ItemPedido> itens;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.data = LocalDateTime.now();
        this.status = ABERTO;
        this.itens = new ArrayList<>();
    }

    public Pedido(int id, Cliente cliente, LocalDateTime data, String status, List<ItemPedido> itens) {
        this.id = id;
        this.cliente = cliente;
        this.data = data;
        this.status = status;
        this.itens = itens;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<ItemPedido> getItens() { return itens; }

    public double getTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " | Cliente: " + cliente.getNome()
                + " | Data: " + data + " | Status: " + status + " | Total: R$ " + getTotal();
    }
}