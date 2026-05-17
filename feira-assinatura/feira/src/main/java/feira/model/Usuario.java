package feira.model;

public class Usuario {
    private String id;
    private String email;
    private String senha;
    private String nome;
    private String token;
    private String enderecoEntrega;

    public Usuario() {}

    public Usuario(String id, String email, String senha, String nome) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    @Override
    public String toString() {
        return "Usuario{id='" + id + "', email='" + email + "', nome='" + nome + "'}";
    }
}
