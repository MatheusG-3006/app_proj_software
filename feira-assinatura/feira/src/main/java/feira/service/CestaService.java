package feira.service;

import feira.exception.FeiraException;
import feira.model.Cesta;
import feira.model.Plano;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Serviço de Montagem da Cesta — etapa 3 do diagrama de sequência.
 *
 * Fluxo:
 *   3.1 Usuário solicita catálogo de itens (frutas, verduras, legumes)
 *   3.2 Sistema retorna itens disponíveis com preços
 *   3.3 Usuário adiciona/remove itens da cesta
 *   3.4 Sistema valida limite do plano e atualiza a cesta
 *   3.5 Usuário confirma a cesta
 */
public class CestaService {

    // Catálogo de itens disponíveis: id -> Item modelo
    private final Map<String, CestaItemInfo> catalogo = new HashMap<>();
    // Cestas em montagem: cestaId -> Cesta
    private final Map<String, Cesta> cestas = new HashMap<>();

    /** Representa um item do catálogo da feira */
    public record CestaItemInfo(String id, String nome, String categoria, String unidade, BigDecimal preco) {}

    public CestaService() {
        catalogo.put("i-001", new CestaItemInfo("i-001", "Banana",  "Frutas",   "kg",  new BigDecimal("4.50")));
        catalogo.put("i-002", new CestaItemInfo("i-002", "Maçã",    "Frutas",   "kg",  new BigDecimal("7.90")));
        catalogo.put("i-003", new CestaItemInfo("i-003", "Laranja", "Frutas",   "kg",  new BigDecimal("5.20")));
        catalogo.put("i-004", new CestaItemInfo("i-004", "Alface",  "Verduras", "uni", new BigDecimal("2.50")));
        catalogo.put("i-005", new CestaItemInfo("i-005", "Tomate",  "Legumes",  "kg",  new BigDecimal("6.80")));
        catalogo.put("i-006", new CestaItemInfo("i-006", "Cenoura", "Legumes",  "kg",  new BigDecimal("3.90")));
        catalogo.put("i-007", new CestaItemInfo("i-007", "Chuchu",  "Legumes",  "kg",  new BigDecimal("2.80")));
        catalogo.put("i-008", new CestaItemInfo("i-008", "Couve",   "Verduras", "maço",new BigDecimal("3.00")));
    }

    // ── 3.1 / 3.2 Catálogo ─────────────────────────────────────────────────

    public Map<String, CestaItemInfo> listarCatalogo() {
        System.out.println("[CestaService] Retornando catálogo com " + catalogo.size() + " itens.");
        return catalogo;
    }

    // ── Criar cesta para o usuário ──────────────────────────────────────────

    public Cesta criarCesta(String usuarioId, String planoId) {
        String id = "c-" + UUID.randomUUID().toString().substring(0, 8);
        Cesta cesta = new Cesta(id, usuarioId, planoId);
        cestas.put(id, cesta);
        System.out.println("[CestaService] Cesta criada: " + id);
        return cesta;
    }

    // ── 3.3 / 3.4 Adicionar item ───────────────────────────────────────────

    public Cesta adicionarItem(String cestaId, String itemId, int quantidade, Plano plano) {
        Cesta cesta = getCesta(cestaId);
        CestaItemInfo info = catalogo.get(itemId);
        if (info == null) {
            throw new FeiraException("Item não encontrado no catálogo: " + itemId);
        }

        // Valida limite do plano
        int totalItens = cesta.getItens().size();
        boolean itemJaExiste = cesta.getItens().stream().anyMatch(i -> i.getId().equals(itemId));
        if (!itemJaExiste && totalItens >= plano.getMaxItens()) {
            throw new FeiraException("Limite de itens do plano atingido (" + plano.getMaxItens() + " itens).");
        }

        Cesta.Item novoItem = new Cesta.Item(info.id(), info.nome(), quantidade, info.unidade(), info.preco());
        cesta.adicionarItem(novoItem);

        System.out.println("[CestaService] Item adicionado: " + novoItem);
        return cesta;
    }

    // ── Remover item ────────────────────────────────────────────────────────

    public Cesta removerItem(String cestaId, String itemId) {
        Cesta cesta = getCesta(cestaId);
        cesta.removerItem(itemId);
        System.out.println("[CestaService] Item removido: " + itemId);
        return cesta;
    }

    // ── 3.5 Confirmar cesta ─────────────────────────────────────────────────

    public Cesta confirmarCesta(String cestaId) {
        Cesta cesta = getCesta(cestaId);
        if (cesta.getItens().isEmpty()) {
            throw new FeiraException("A cesta está vazia. Adicione pelo menos um item.");
        }
        cesta.setStatus("CONFIRMADA");
        System.out.println("[CestaService] Cesta confirmada: " + cesta);
        return cesta;
    }

    public Cesta getCesta(String cestaId) {
        Cesta cesta = cestas.get(cestaId);
        if (cesta == null) {
            throw new FeiraException("Cesta não encontrada: " + cestaId);
        }
        return cesta;
    }
}
