import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venda {
    private int id;
    private Date data;
    private Cliente cliente;
    private double valorTotal;
    private String status;
    private List<ItemVenda> itens;

    public Venda(int id, Date data, Cliente cliente, String status) {
        this.id = id;
        this.data = data;
        this.cliente = cliente;
        this.status = status;
        this.valorTotal = 0.0; // Inicialmente, o valor total é zero
        this.itens = new ArrayList<>(); // Inicializa a lista de itens de venda
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    // Método para calcular o valor total da venda com base nos itens
    public void calcularValorTotal() {
        double total = 0.0;
        for (ItemVenda item : itens) {
            total += item.getValorTotal();
        }
        this.valorTotal = total;
    }

    // Método para vincular um produto à venda
    public void adicionarItemVenda(Produto produto, int quantidade, double preco) {
        ItemVenda item = new ItemVenda(produto, quantidade, preco);
        itens.add(item);
        calcularValorTotal(); // Atualiza o valor total após adicionar o item
    }

    // Método para efetivar a venda (reduzir estoque e alterar status)
    public void efetivarVenda() {
        if (status.equals("digitando")) {
            for (ItemVenda item : itens) {
                Produto produto = item.getProduto();
                int quantidadeVendida = item.getQuantidade();
                // Verifica se há estoque suficiente para a venda
                if (produto.getQuantidade() >= quantidadeVendida) {
                    // Atualiza o estoque do produto
                    produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);
                } else {
                    throw new RuntimeException("Estoque insuficiente para efetivar a venda.");
                }
            }
            status = "efetivada"; // Altera o status da venda para efetivada
        } else {
            throw new RuntimeException("A venda já foi efetivada ou está estornada.");
        }
    }

    // Método para estornar a venda (aumentar estoque e alterar status)
    public void estornarVenda() {
        if (status.equals("efetivada")) {
            for (ItemVenda item : itens) {
                Produto produto = item.getProduto();
                int quantidadeVendida = item.getQuantidade();
                // Atualiza o estoque do produto
                produto.setQuantidade(produto.getQuantidade() + quantidadeVendida);
            }
            status = "digitando"; // Altera o status da venda de volta para digitando
        } else {
            throw new RuntimeException("A venda não pode ser estornada, pois não está efetivada.");
        }
    }
}