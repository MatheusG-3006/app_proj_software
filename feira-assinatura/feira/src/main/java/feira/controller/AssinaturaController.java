package feira.controller;

import feira.model.*;
import feira.service.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Orquestrador do fluxo "Assinar Serviço de Feira".
 *
 * Coordena todas as etapas do diagrama de sequência:
 *   1. Autenticação
 *   2. Seleção de Plano
 *   3. Montagem da Cesta
 *   4. Endereço e Confirmação
 *   5. Pagamento
 */
public class AssinaturaController {

    private final AutenticacaoService autenticacaoService;
    private final PlanoService planoService;
    private final CestaService cestaService;
    private final AssinaturaService assinaturaService;
    private final PagamentoService pagamentoService;

    // Estado da sessão corrente
    private String tokenSessao;
    private Usuario usuarioLogado;
    private Plano planoSelecionado;
    private Cesta cestaAtual;
    private Assinatura assinaturaAtual;

    public AssinaturaController() {
        this.autenticacaoService = new AutenticacaoService();
        this.planoService        = new PlanoService();
        this.cestaService        = new CestaService();
        this.assinaturaService   = new AssinaturaService();
        this.pagamentoService    = new PagamentoService(assinaturaService);
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. AUTENTICAÇÃO
    // ════════════════════════════════════════════════════════════════════════

    /** 1.1–1.2: Envia credenciais e recebe OTP */
    public String iniciarAutenticacao(String email, String senha) {
        System.out.println("\n[Fluxo] ETAPA 1 — Autenticação");
        return autenticacaoService.autenticarCredenciais(email, senha);
    }

    /** 1.3–1.4: Confirma OTP e obtém token de sessão */
    public void confirmarOtp(String email, String otp) {
        tokenSessao  = autenticacaoService.confirmarOtp(email, otp);
        usuarioLogado = autenticacaoService.validarToken(tokenSessao);
        System.out.println("[Fluxo] Usuário autenticado: " + usuarioLogado.getNome());
    }

    /** Cadastro + autenticação de novo usuário */
    public String cadastrarEAutenticar(String email, String senha, String nome) {
        System.out.println("\n[Fluxo] ETAPA 1 — Cadastro + Autenticação");
        return autenticacaoService.cadastrarUsuario(email, senha, nome);
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. SELEÇÃO DE PLANO
    // ════════════════════════════════════════════════════════════════════════

    /** 2.1–2.2: Lista planos disponíveis */
    public List<Plano> listarPlanos() {
        validarSessao();
        System.out.println("\n[Fluxo] ETAPA 2 — Seleção de Plano");
        return planoService.listarPlanos();
    }

    /** 2.3–2.4: Usuário seleciona um plano */
    public void selecionarPlano(String planoId) {
        validarSessao();
        planoSelecionado = planoService.selecionarPlano(planoId);
    }

    // ════════════════════════════════════════════════════════════════════════
    // 3. MONTAGEM DA CESTA
    // ════════════════════════════════════════════════════════════════════════

    /** 3.1–3.2: Cria cesta e retorna catálogo */
    public java.util.Map<String, CestaService.CestaItemInfo> iniciarMontagem() {
        validarSessao();
        validarPlano();
        System.out.println("\n[Fluxo] ETAPA 3 — Montagem da Cesta");
        cestaAtual = cestaService.criarCesta(usuarioLogado.getId(), planoSelecionado.getId());
        return cestaService.listarCatalogo();
    }

    /** 3.3–3.4: Adiciona item à cesta */
    public Cesta adicionarItem(String itemId, int quantidade) {
        validarSessao();
        return cestaService.adicionarItem(cestaAtual.getId(), itemId, quantidade, planoSelecionado);
    }

    /** Remove item da cesta */
    public Cesta removerItem(String itemId) {
        validarSessao();
        return cestaService.removerItem(cestaAtual.getId(), itemId);
    }

    /** 3.5: Confirma a cesta montada */
    public Cesta confirmarCesta() {
        validarSessao();
        return cestaService.confirmarCesta(cestaAtual.getId());
    }

    // ════════════════════════════════════════════════════════════════════════
    // 4. ENDEREÇO E CONFIRMAÇÃO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * 4.1–4.5: Valida endereço, exibe resumo e cria assinatura.
     */
    public Assinatura confirmarAssinatura(String cep, String logradouro, String numero, String cidade) {
        validarSessao();
        System.out.println("\n[Fluxo] ETAPA 4 — Endereço e Confirmação");

        String endereco = assinaturaService.validarEndereco(cep, logradouro, numero, cidade);
        assinaturaService.exibirResumo(planoSelecionado.getNome(), cestaAtual, endereco);

        assinaturaAtual = assinaturaService.criarAssinatura(
            usuarioLogado.getId(),
            planoSelecionado.getId(),
            cestaAtual.getId(),
            endereco
        );
        return assinaturaAtual;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 5. PAGAMENTO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * 5.1–5.4: Processa pagamento e, se aprovado, ativa a assinatura.
     */
    public Pagamento realizarPagamento(Pagamento.MetodoPagamento metodo, String dadosExtra) {
        validarSessao();
        System.out.println("\n[Fluxo] ETAPA 5 — Pagamento");

        return pagamentoService.processarPagamento(
            assinaturaAtual.getId(),
            planoSelecionado.getPreco(),
            metodo,
            dadosExtra
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // Encerramento
    // ════════════════════════════════════════════════════════════════════════

    public void encerrarSessao() {
        if (tokenSessao != null) {
            autenticacaoService.encerrarSessao(tokenSessao);
            tokenSessao = null;
            usuarioLogado = null;
        }
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private void validarSessao() {
        if (tokenSessao == null) throw new IllegalStateException("Usuário não autenticado.");
    }

    private void validarPlano() {
        if (planoSelecionado == null) throw new IllegalStateException("Nenhum plano selecionado.");
    }

    // ── Getters de estado ───────────────────────────────────────────────────

    public String getTokenSessao()        { return tokenSessao; }
    public Usuario getUsuarioLogado()     { return usuarioLogado; }
    public Plano getPlanoSelecionado()    { return planoSelecionado; }
    public Cesta getCestaAtual()          { return cestaAtual; }
    public Assinatura getAssinaturaAtual(){ return assinaturaAtual; }
}
