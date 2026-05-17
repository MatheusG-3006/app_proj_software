package feira.service;

import feira.exception.PagamentoException;
import feira.model.Assinatura;
import feira.model.Pagamento;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Serviço de Pagamento — etapa 5 do diagrama de sequência.
 *
 * Fluxo:
 *   5.1 Usuário informa dados de pagamento
 *   5.2 Sistema envia para gateway externo
 *   5.3 Gateway processa e retorna resultado
 *   5.4a Pagamento aprovado → assinatura ativada, e-mail de confirmação enviado
 *   5.4b Pagamento recusado → notificar usuário, permitir nova tentativa
 */
public class PagamentoService {

    private final Map<String, Pagamento> pagamentos = new HashMap<>();
    private final AssinaturaService assinaturaService;

    public PagamentoService(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    // ── 5.1 / 5.2 Processar pagamento ──────────────────────────────────────

    /**
     * Processa o pagamento junto ao gateway externo (simulado).
     * @param assinaturaId ID da assinatura a ser paga
     * @param valor        Valor a cobrar
     * @param metodo       Método de pagamento escolhido
     * @param dadosExtra   Dados extras (número do cartão, chave PIX etc.)
     * @return Pagamento com status final
     */
    public Pagamento processarPagamento(
            String assinaturaId,
            BigDecimal valor,
            Pagamento.MetodoPagamento metodo,
            String dadosExtra) {

        Assinatura assinatura = assinaturaService.getAssinatura(assinaturaId);
        if (assinatura.getStatus() == Assinatura.StatusAssinatura.ATIVA) {
            throw new PagamentoException("Assinatura já está ativa.");
        }

        String pagamentoId = "pg-" + UUID.randomUUID().toString().substring(0, 8);
        Pagamento pagamento = new Pagamento(pagamentoId, assinaturaId, valor, metodo);
        pagamentos.put(pagamentoId, pagamento);

        System.out.println("[PagamentoService] Enviando para gateway: " + pagamento);

        // ── 5.3 Simula resposta do gateway ────────────────────────────────
        boolean aprovado = simularGateway(metodo, dadosExtra);

        if (aprovado) {
            // 5.4a Aprovado
            String codigoTransacao = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
            pagamento.aprovar(codigoTransacao);
            assinaturaService.ativarAssinatura(assinaturaId);
            enviarEmailConfirmacao(assinatura, pagamento);
            System.out.println("[PagamentoService] Pagamento APROVADO — " + codigoTransacao);
        } else {
            // 5.4b Recusado
            pagamento.recusar();
            System.out.println("[PagamentoService] Pagamento RECUSADO. Tente novamente.");
        }

        return pagamento;
    }

    // ── Gateway simulado ────────────────────────────────────────────────────

    /**
     * Simula aprovação do gateway.
     * Regra simples: PIX é sempre aprovado; cartão é aprovado se não contiver "0000".
     */
    private boolean simularGateway(Pagamento.MetodoPagamento metodo, String dados) {
        if (metodo == Pagamento.MetodoPagamento.PIX) return true;
        if (dados != null && dados.contains("0000")) return false; // cartão inválido
        return true;
    }

    // ── Notificações ────────────────────────────────────────────────────────

    private void enviarEmailConfirmacao(Assinatura assinatura, Pagamento pagamento) {
        // Em produção: integrar com serviço de e-mail (SendGrid, SES etc.)
        System.out.println("[PagamentoService] E-mail de confirmação enviado para assinatura " + assinatura.getId());
    }

    public Pagamento getPagamento(String id) {
        Pagamento p = pagamentos.get(id);
        if (p == null) throw new PagamentoException("Pagamento não encontrado: " + id);
        return p;
    }
}
