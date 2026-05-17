package feira.service;

import feira.exception.FeiraException;
import feira.model.Plano;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de Seleção de Plano — etapa 2 do diagrama de sequência.
 *
 * Fluxo:
 *   2.1 Usuário solicita lista de planos disponíveis
 *   2.2 Sistema retorna planos com detalhes
 *   2.3 Usuário seleciona um plano
 *   2.4 Sistema confirma disponibilidade e registra seleção
 */
public class PlanoService {

    private final Map<String, Plano> planos = new HashMap<>();

    public PlanoService() {
        // Planos pré-cadastrados
        Plano basico = new Plano("p-001", "Básico",
            new BigDecimal("79.90"), 1, 8);
        basico.setDescricao("1 entrega por semana com até 8 itens");

        Plano plus = new Plano("p-002", "Plus",
            new BigDecimal("139.90"), 2, 12);
        plus.setDescricao("2 entregas por semana com até 12 itens");

        Plano premium = new Plano("p-003", "Premium",
            new BigDecimal("219.90"), 4, 20);
        premium.setDescricao("4 entregas por semana com até 20 itens");

        planos.put(basico.getId(), basico);
        planos.put(plus.getId(), plus);
        planos.put(premium.getId(), premium);
    }

    // ── 2.1 / 2.2 Listar planos ────────────────────────────────────────────

    public List<Plano> listarPlanos() {
        System.out.println("[PlanoService] Retornando " + planos.size() + " planos disponíveis.");
        return new ArrayList<>(planos.values());
    }

    // ── 2.3 / 2.4 Selecionar plano ─────────────────────────────────────────

    public Plano selecionarPlano(String planoId) {
        Plano plano = planos.get(planoId);
        if (plano == null) {
            throw new FeiraException("Plano não encontrado: " + planoId);
        }
        System.out.println("[PlanoService] Plano selecionado: " + plano.getNome());
        return plano;
    }

    public Plano buscarPlano(String planoId) {
        return planos.get(planoId);
    }
}
