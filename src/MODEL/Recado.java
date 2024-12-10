package MODEL;

import CONTROLLER.DAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Recado implements InterfaceClasse, InterfaceBanco {

    private int id;

    private int idPessoa;
    private Pessoa pessoa;

    private String comentario;
    private String nome;

    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    public static int total;
    private DAO dao;

    public Recado() {
        this.idPessoa = 0;
    }

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("idPessoa");
        campos.add("comentario");

        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.idPessoa);
        valores.add(this.comentario);

        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        return valores;
    }

    @Override
    public String getNomeTabela() {
        return "TB_RECADO";
    }

    public static String getNomeTabelaByClass() {
        return "TB_RECADO";
    }

    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {

        boolean alterado = vetor.get(3) != null && vetor.getFirst() != null;

        if (!alterado) {
            return alterado;
        } else {
            this.dao = dao;
            this.id = (int) vetor.get(0);
            if (vetor.get(1) != null && ((int) vetor.get(1)) != 0) {
                this.idPessoa = (int) vetor.get(1);
                Object objB = this.dao.getItemByID(2, this.idPessoa);
                if (this.dao.getBanco().findByItem((InterfaceBanco) objB)) {
                    this.pessoa = (Pessoa) objB;
                    this.nome = this.pessoa.getNome();
                }
            }

            this.comentario = (String) vetor.get(2);
            if (vetor.get(3) != null) {
                this.dataCriacao = (LocalDate) vetor.get(3);
            }
            if (vetor.get(4) != null) {
                this.dataModificacao = (LocalDate) vetor.get(4);
            }
            return alterado;
        }
    }

    public static String[] getCampos() {
        String[] campos = new String[2];
        campos[0] = "ID: ";
        campos[1] = "Comentário: ";
        return campos;
    }


    public boolean criar(DAO dao, List<Object> vetor) {
        this.dao = dao;
        boolean alterado = false;
        if (vetor.getFirst() != null) {
            this.comentario = (String) vetor.getFirst();
            this.dataCriacao = LocalDate.now();
            this.dataModificacao = null;
            this.id = this.dao.getTotalClasse(0) + 1;
            alterado = true;
        }
        if (this.dao.getUserLogado() != null) {
            this.idPessoa = this.dao.getUserLogado().getIdPessoa();
            this.pessoa = this.dao.getUserLogado().getPessoa();
        }
        return alterado;
    }

    public void update(List<Object> vetor) {
        boolean alterou = false;
        if (vetor.get(0) != null) {
            String comentario = (String) vetor.get(1);
            if (!comentario.isEmpty()) {
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
        if (vetor.getFirst() != null) {
            String comentario = (String) vetor.getFirst();
            if (!comentario.isEmpty()) {
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

        resultado.append("\nID: ").append(this.id);

        if (this.comentario != null && !this.comentario.isEmpty()) {
            resultado.append("\n   Comentário: ").append(this.comentario);
        }

        if (this.pessoa != null && this.pessoa.getNome() != null && this.pessoa.getNome().length() > 1) {
            resultado.append("\n   Autor: ").append(this.pessoa.getNome());
        } else {
            resultado.append("\n   Autor: Anônimo");
        }

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
