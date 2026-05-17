package feira;

import feira.controller.AssinaturaController;
import feira.model.*;
import feira.service.CestaService;

import java.util.List;
import java.util.Map;

/**
 * Demonstração completa do fluxo "Assinar Serviço de Feira".
 * Executa todas as 5 etapas do diagrama de sequência.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    Serviço de Assinatura de Feira        ║");
        System.out.println("╚══════════════════════════════════════════╝");

        AssinaturaController controller = new AssinaturaController();

        // ── Cenário 1: Usuário existente ─────────────────────────────────
        System.out.println("\n=== CENÁRIO: Usuário existente ===");

        // ETAPA 1 — Autenticação
        String otp = controller.iniciarAutenticacao("usuario@email.com", "senha123");
        controller.confirmarOtp("usuario@email.com", otp);

        // ETAPA 2 — Seleção de Plano
        List<Plano> planos = controller.listarPlanos();
        System.out.println("[Main] Planos disponíveis:");
        planos.forEach(p -> System.out.println("  • [" + p.getId() + "] " + p.getNome()
            + " — R$" + p.getPreco() + " / " + p.getFrequenciaEntregas() + "x semana"));

        controller.selecionarPlano("p-002"); // Plano Plus

        // ETAPA 3 — Montagem da Cesta
        Map<String, CestaService.CestaItemInfo> catalogo = controller.iniciarMontagem();
        System.out.println("[Main] Catálogo recebido com " + catalogo.size() + " itens.");

        controller.adicionarItem("i-001", 2); // 2 kg de Banana
        controller.adicionarItem("i-003", 3); // 3 kg de Laranja
        controller.adicionarItem("i-005", 1); // 1 kg de Tomate
        controller.adicionarItem("i-008", 2); // 2 maços de Couve

        Cesta cesta = controller.confirmarCesta();
        System.out.println("[Main] Cesta confirmada: " + cesta.getItens().size()
            + " tipos de itens — Total R$" + cesta.getTotal());

        // ETAPA 4 — Endereço e Confirmação
        Assinatura assinatura = controller.confirmarAssinatura(
            "01310-100", "Av. Paulista", "1000", "São Paulo"
        );
        System.out.println("[Main] Assinatura criada: " + assinatura.getId()
            + " — Status: " + assinatura.getStatus());

        // ETAPA 5 — Pagamento (PIX — sempre aprovado na simulação)
        Pagamento pagamento = controller.realizarPagamento(
            Pagamento.MetodoPagamento.PIX, "chave-pix@email.com"
        );
        System.out.println("[Main] Resultado do pagamento: " + pagamento.getStatus());

        if (pagamento.getStatus() == Pagamento.StatusPagamento.APROVADO) {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║  ✓ Assinatura ativada com sucesso!       ║");
            System.out.println("║  Próxima entrega: "
                + assinatura.getProximaEntrega() + "      ║");
            System.out.println("╚══════════════════════════════════════════╝");
        }

        controller.encerrarSessao();

        // ── Cenário 2: Pagamento recusado ────────────────────────────────
        System.out.println("\n=== CENÁRIO: Pagamento com cartão inválido ===");

        AssinaturaController controller2 = new AssinaturaController();
        String otp2 = controller2.iniciarAutenticacao("usuario@email.com", "senha123");
        controller2.confirmarOtp("usuario@email.com", otp2);
        controller2.selecionarPlano("p-001");
        controller2.iniciarMontagem();
        controller2.adicionarItem("i-002", 1);
        controller2.confirmarCesta();
        controller2.confirmarAssinatura("04101-300", "Rua Augusta", "500", "São Paulo");

        Pagamento pagRecusado = controller2.realizarPagamento(
            Pagamento.MetodoPagamento.CARTAO_CREDITO, "4111-0000-0000-0000" // contém "0000" → recusado
        );
        System.out.println("[Main] Resultado: " + pagRecusado.getStatus()
            + " — Usuário deve tentar outro método.");

        controller2.encerrarSessao();
    }
}
