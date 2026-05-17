package feira.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {

    public enum StatusPagamento {
        PENDENTE, APROVADO, RECUSADO, ESTORNADO
    }

    public enum MetodoPagamento {
        CARTAO_CREDITO, CARTAO_DEBITO, PIX, BOLETO
    }

    private String id;
    private String assinaturaId;
    private BigDecimal valor;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private String codigoTransacao;
    private LocalDateTime processadoEm;

    public Pagamento() {}

    public Pagamento(String id, String assinaturaId, BigDecimal valor, MetodoPagamento metodo) {
        this.id = id;
        this.assinaturaId = assinaturaId;
        this.valor = valor;
        this.metodo = metodo;
        this.status = StatusPagamento.PENDENTE;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAssinaturaId() { return assinaturaId; }
    public void setAssinaturaId(String assinaturaId) { this.assinaturaId = assinaturaId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public MetodoPagamento getMetodo() { return metodo; }
    public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public String getCodigoTransacao() { return codigoTransacao; }
    public void setCodigoTransacao(String codigoTransacao) { this.codigoTransacao = codigoTransacao; }

    public LocalDateTime getProcessadoEm() { return processadoEm; }
    public void setProcessadoEm(LocalDateTime processadoEm) { this.processadoEm = processadoEm; }

    public void aprovar(String codigoTransacao) {
        this.status = StatusPagamento.APROVADO;
        this.codigoTransacao = codigoTransacao;
        this.processadoEm = LocalDateTime.now();
    }

    public void recusar() {
        this.status = StatusPagamento.RECUSADO;
        this.processadoEm = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Pagamento{id='" + id + "', valor=R$" + valor + ", metodo=" + metodo + ", status=" + status + "}";
    }
}
