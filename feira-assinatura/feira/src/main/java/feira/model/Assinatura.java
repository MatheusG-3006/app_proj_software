package feira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Assinatura {

    public enum StatusAssinatura {
        PENDENTE, ATIVA, CANCELADA, SUSPENSA
    }

    private String id;
    private String usuarioId;
    private String planoId;
    private String cestaId;
    private String enderecoEntrega;
    private StatusAssinatura status;
    private LocalDate dataInicio;
    private LocalDate proximaEntrega;
    private LocalDateTime criadoEm;

    public Assinatura() {}

    public Assinatura(String id, String usuarioId, String planoId, String cestaId, String enderecoEntrega) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.planoId = planoId;
        this.cestaId = cestaId;
        this.enderecoEntrega = enderecoEntrega;
        this.status = StatusAssinatura.PENDENTE;
        this.criadoEm = LocalDateTime.now();
        this.dataInicio = LocalDate.now();
        this.proximaEntrega = dataInicio.plusDays(7);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getPlanoId() { return planoId; }
    public void setPlanoId(String planoId) { this.planoId = planoId; }

    public String getCestaId() { return cestaId; }
    public void setCestaId(String cestaId) { this.cestaId = cestaId; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public StatusAssinatura getStatus() { return status; }
    public void setStatus(StatusAssinatura status) { this.status = status; }

    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getProximaEntrega() { return proximaEntrega; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    @Override
    public String toString() {
        return "Assinatura{id='" + id + "', status=" + status + ", proximaEntrega=" + proximaEntrega + "}";
    }
}
