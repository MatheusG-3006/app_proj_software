package feira.service;

import feira.exception.AutenticacaoException;
import feira.model.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Serviço de Autenticação — implementa as etapas 1.1 a 1.4 do diagrama de sequência.
 *
 * Fluxo:
 *   1.1 Usuário envia credenciais (e-mail + senha)
 *   1.2 Sistema valida e gera OTP
 *   1.3 Usuário confirma OTP
 *   1.4 Sistema retorna token de sessão
 */
public class AutenticacaoService {

    // Repositório in-memory (substituir por banco em produção)
    private final Map<String, Usuario> usuarios = new HashMap<>();
    // OTPs pendentes: email -> codigo
    private final Map<String, String> otpsPendentes = new HashMap<>();
    // Sessões ativas: token -> usuarioId
    private final Map<String, String> sessoes = new HashMap<>();

    public AutenticacaoService() {
        // Usuário de demonstração
        Usuario demo = new Usuario("u-001", "usuario@email.com", "senha123", "Maria Silva");
        usuarios.put(demo.getEmail(), demo);
    }

    // ── 1.1 Enviar credenciais ──────────────────────────────────────────────

    /**
     * Valida e-mail e senha. Em caso de sucesso, gera e "envia" um OTP.
     * @return o código OTP gerado (em produção seria enviado por SMS/e-mail)
     */
    public String autenticarCredenciais(String email, String senha) {
        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new AutenticacaoException("Credenciais inválidas.");
        }

        // 1.2 Gerar OTP de 6 dígitos
        String otp = String.format("%06d", (int)(Math.random() * 1_000_000));
        otpsPendentes.put(email, otp);

        System.out.println("[AutenticacaoService] OTP gerado para " + email + ": " + otp);
        return otp; // Em produção: enviar por SMS/e-mail e não retornar diretamente
    }

    // ── 1.3 Confirmar OTP ───────────────────────────────────────────────────

    /**
     * Verifica o OTP informado pelo usuário e, se válido, cria uma sessão.
     * @return token de sessão
     */
    public String confirmarOtp(String email, String otp) {
        String otpEsperado = otpsPendentes.get(email);
        if (otpEsperado == null || !otpEsperado.equals(otp)) {
            throw new AutenticacaoException("OTP inválido ou expirado.");
        }

        otpsPendentes.remove(email);

        // 1.4 Gerar token de sessão
        String token = UUID.randomUUID().toString();
        Usuario usuario = usuarios.get(email);
        usuario.setToken(token);
        sessoes.put(token, usuario.getId());

        System.out.println("[AutenticacaoService] Sessão criada para " + email + " — token: " + token);
        return token;
    }

    // ── Cadastro de novo usuário ────────────────────────────────────────────

    /**
     * Registra um novo usuário e inicia o fluxo de autenticação.
     * @return OTP para confirmação
     */
    public String cadastrarUsuario(String email, String senha, String nome) {
        if (usuarios.containsKey(email)) {
            throw new AutenticacaoException("E-mail já cadastrado.");
        }
        Usuario novo = new Usuario(UUID.randomUUID().toString(), email, senha, nome);
        usuarios.put(email, novo);

        System.out.println("[AutenticacaoService] Novo usuário cadastrado: " + email);
        return autenticarCredenciais(email, senha);
    }

    // ── Validação de token (usada pelos demais serviços) ────────────────────

    public Usuario validarToken(String token) {
        String usuarioId = sessoes.get(token);
        if (usuarioId == null) {
            throw new AutenticacaoException("Sessão inválida ou expirada.");
        }
        return usuarios.values().stream()
            .filter(u -> u.getId().equals(usuarioId))
            .findFirst()
            .orElseThrow(() -> new AutenticacaoException("Usuário não encontrado."));
    }

    public void encerrarSessao(String token) {
        sessoes.remove(token);
        System.out.println("[AutenticacaoService] Sessão encerrada: " + token);
    }
}
