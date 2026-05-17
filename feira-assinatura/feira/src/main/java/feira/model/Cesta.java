package feira.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cesta {

    public static class Item {
        private String id;
        private String nome;
        private int quantidade;
        private String unidade;
        private BigDecimal precoUnitario;

        public Item(String id, String nome, int quantidade, String unidade, BigDecimal precoUnitario) {
            this.id = id;
            this.nome = nome;
            this.quantidade = quantidade;
            this.unidade = unidade;
            this.precoUnitario = precoUnitario;
        }

        public String getId() { return id; }
        public String getNome() { return nome; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        public String getUnidade() { return unidade; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }

        public BigDecimal getSubtotal() {
            return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }

        @Override
        public String toString() {
            return nome + " x" + quantidade + " " + unidade + " = R$" + getSubtotal();
        }
    }

    private String id;
    private String usuarioId;
    private String planoId;
    private List<Item> itens = new ArrayList<>();
    private String status; // RASCUNHO, CONFIRMADA, PAGA

    public Cesta() {}

    public Cesta(String id, String usuarioId, String planoId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.planoId = planoId;
        this.status = "RASCUNHO";
    }

    public void adicionarItem(Item item) {
        itens.stream()
            .filter(i -> i.getId().equals(item.getId()))
            .findFirst()
            .ifPresentOrElse(
                existente -> existente.setQuantidade(existente.getQuantidade() + item.getQuantidade()),
                () -> itens.add(item)
            );
    }

    public void removerItem(String itemId) {
        itens.removeIf(i -> i.getId().equals(itemId));
    }

    public BigDecimal getTotal() {
        return itens.stream()
            .map(Item::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getPlanoId() { return planoId; }
    public void setPlanoId(String planoId) { this.planoId = planoId; }

    public List<Item> getItens() { return itens; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Cesta{id='" + id + "', itens=" + itens.size() + ", total=R$" + getTotal() + ", status='" + status + "'}";
    }
}
