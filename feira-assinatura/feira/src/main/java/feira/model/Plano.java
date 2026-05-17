package feira.model;

import java.math.BigDecimal;

public class Plano {
    private String id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int frequenciaEntregas; // entregas por mês
    private int maxItens;

    public Plano() {}

    public Plano(String id, String nome, BigDecimal preco, int frequenciaEntregas, int maxItens) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.frequenciaEntregas = frequenciaEntregas;
        this.maxItens = maxItens;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getFrequenciaEntregas() { return frequenciaEntregas; }
    public void setFrequenciaEntregas(int frequenciaEntregas) { this.frequenciaEntregas = frequenciaEntregas; }

    public int getMaxItens() { return maxItens; }
    public void setMaxItens(int maxItens) { this.maxItens = maxItens; }

    @Override
    public String toString() {
        return "Plano{id='" + id + "', nome='" + nome + "', preco=" + preco + "}";
    }
}
