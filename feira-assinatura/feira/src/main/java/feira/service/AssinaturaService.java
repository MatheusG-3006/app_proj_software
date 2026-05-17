package feira.service;

import feira.exception.FeiraException;
import feira.model.Assinatura;
import feira.model.Cesta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Serviço de Endereço e Confirmação — etapa 4 do diagrama de sequência.
 *
 * Fluxo:
 *   4.1 Usuário informa endereço de entrega
 *   4.2 Sistema valida o endereço (CEP, disponibilidade de área)
 *   4.3 Sistema exibe resumo da assinatura para confirmação
 *   4.4 Usuário confirma
 *   4.5 Sistema cria a assinatura com status PENDENTE (aguardando pagamento)
 */
public class AssinaturaService {

    private final Map<String, Assinatura> assinaturas = new HashMap<>();

    // ── 4.1 / 4.2 Validar endereço ────────────────────────────────────────

    /**
     * Simula validação de CEP e área de entrega.
     * Em produção: integrar com serviço de CEP (e.g. ViaCEP) e verificar
     * cobertura geográfica.
     */
    public String validarEndereco(String cep, String logradouro, String numero, String cidade) {
        if (cep == null || cep.replaceAll("\\D", "").length() != 8) {
            throw new FeiraException("CEP inválido: " + cep);
        }
        // Simula área de cobertura: aceita qualquer CEP por ora
        String enderecoFormatado = logradouro + ", " + numero + " — " + cidade + " — CEP " + cep;
        System.out.println("[AssinaturaService] Endereço validado: " + enderecoFormatado);
        return enderecoFormatado;
    }

    // ── 4.3 Resumo antes da confirmação ────────────────────────────────────

    public void exibirResumo(String planoNome, Cesta cesta, String enderecoEntrega) {
        System.out.println("\n──────────────────────────────────────────");
        System.out.println("  RESUMO DA ASSINATURA");
        System.out.println("  Plano      : " + planoNome);
        System.out.println("  Entrega em : " + enderecoEntrega);
        System.out.println("  Itens da cesta:");
        cesta.getItens().forEach(i -> System.out.println("    • " + i));
        System.out.printf("  Total      : R$ %.2f%n", cesta.getTotal());
        System.out.println("──────────────────────────────────────────\n");
    }

    // ── 4.4 / 4.5 Criar assinatura ─────────────────────────────────────────

    /**
     * Cria a assinatura após confirmação do usuário.
     */
    public Assinatura criarAssinatura(String usuarioId, String planoId, String cestaId, String enderecoEntrega) {
        String id = "a-" + UUID.randomUUID().toString().substring(0, 8);
        Assinatura assinatura = new Assinatura(id, usuarioId, planoId, cestaId, enderecoEntrega);
        assinaturas.put(id, assinatura);
        System.out.println("[AssinaturaService] Assinatura criada: " + assinatura);
        return assinatura;
    }

    public void ativarAssinatura(String assinaturaId) {
        Assinatura a = getAssinatura(assinaturaId);
        a.setStatus(Assinatura.StatusAssinatura.ATIVA);
        System.out.println("[AssinaturaService] Assinatura ativada: " + assinaturaId);
    }

    public Assinatura getAssinatura(String id) {
        Assinatura a = assinaturas.get(id);
        if (a == null) {
            throw new FeiraException("Assinatura não encontrada: " + id);
        }
        return a;
    }
}
