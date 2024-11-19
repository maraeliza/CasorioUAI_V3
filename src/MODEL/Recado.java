package MODEL;

import CONTROLLER.DAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List; 

public class Recado implements InterfaceClasse{

    private int id;

    private int idPessoa;
    private Pessoa pessoa;

    private String comentario;
    private String nome;

    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    public static int total;
    private DAO dao;
    public static String[] getCampos() {
        String[] campos = new String[2];
        campos[0] = "ID: ";
        campos[1] = "Comentário: ";
        return campos;
    }
    

    public boolean criar(DAO dao,List<Object> vetor) {
        this.dao = dao;
        boolean alterado = false;
        if (vetor.get(0) != null) {
            this.comentario = (String) vetor.get(0);
            this.dataCriacao = LocalDate.now();
            this.dataModificacao = null;
            this.id = ++Recado.total;
            alterado = true;
        }
        if (this.dao.getUserLogado() != null) {
            this.pessoa = this.dao.getUserLogado().getPessoa();
        }
        return alterado;
    }

    public void update(List<Object> vetor) {
        boolean alterou = false;
        if (vetor.get(0) != null) {
            String comentario = (String) vetor.get(1);
            if (comentario.length() > 0) {
                this.comentario = comentario;
                alterou = true;
            }
        }

        if (alterou) {
            this.atualizarDataModificacao();
        }

    }

    public void update(Usuario user, List<Object> vetor) {
        boolean alterou = false;
        if (vetor.get(0) != null) {
            String comentario = (String) vetor.get(0);
            if (comentario.length() > 0) {
                this.comentario = comentario;
                alterou = true;
            }
        }
        if (user != null) {
            Pessoa p = user.getPessoa();
            if (p != null) {
                this.pessoa = p;
                alterou = true;
            }
        }

        if (alterou) {
            this.atualizarDataModificacao();
        }

    }


    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Adiciona o ID
        resultado.append("\nID: ").append(this.id);

        // Verifica se o comentário não é nulo ou vazio
        if (this.comentario != null && !this.comentario.isEmpty()) {
            resultado.append("\n   Comentário: ").append(this.comentario);
        }

        // Verifica se a pessoa não é nula e se o nome é válido
        if (this.pessoa != null && this.pessoa.getNome() != null && this.pessoa.getNome().length() > 1) {
            resultado.append("\n   Autor: ").append(this.pessoa.getNome());
        }else{
            resultado.append("\n   Autor: Anônimo");
        }

        // Verifica e formata a data de criação
        if (this.dataCriacao != null) {
            resultado.append("\n   Data de Criação: ").append(this.dataCriacao.format(formatter));
        }
        if (this.dataModificacao != null) {
            resultado.append("\n   Data da Última Modificação: ").append(this.dataModificacao.format(formatter));
        }
        return resultado.toString();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public static void setTotal(int t) {
        total = t;
    }

    public String getComentario() {
        return this.comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
        this.dataModificacao = LocalDate.now();
    } 
    public int getIdPessoa() {
        return this.idPessoa;
    }
    public void setIdPessoa(int id) {
        this.idPessoa = id;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
        this.dataModificacao = LocalDate.now();
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void atualizarDataModificacao() {

        this.dataModificacao = LocalDate.now();
    }

    public boolean deletar() {
        return true;
    }
}
