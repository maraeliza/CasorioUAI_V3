/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.Util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author CAUPT - ALUNOS
 */
public class Usuario implements InterfaceClasse, InterfaceBanco {

    private int id;
    private int idPessoa;
    private Pessoa pessoa;
    private int tipo;
    private String login;
    private String senha;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private DAO dao;
    public static int total;

    public Usuario() {

        this.idPessoa = 0;
        this.setTipo(1);
    }

    public static String[] getCampos() {

        String[] campos = new String[5];
        campos[0] = "ID: ";
        campos[1] = "ID DA PESSOA: ";
        campos[2] = "Login: ";
        campos[3] = "Senha: ";
        return campos;
    }


    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("idPessoa");
        campos.add("tipo");
        campos.add("login");
        campos.add("senha");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.idPessoa);
        valores.add(this.tipo);
        valores.add(this.login);
        valores.add(this.senha);
        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        return valores;
    }

    @Override
    public String getNomeTabela() {
        return "TB_USUARIO";
    }

    public static String getNomeTabelaByClass() {
        return "TB_USUARIO";
    }


    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        System.out.println(vetor);
        boolean alterado = vetor.get(0) != null && vetor.get(1) != null && vetor.get(2) != null && vetor.get(3) != null;
        if (!alterado) {

            return alterado;
        } else {

            this.dao = dao;
            this.id = (int) vetor.get(0);
            int idPessoa = (int) vetor.get(1);
            Object objB = this.dao.getItemByID(2, idPessoa);
            System.out.println(objB);
            if (objB != null) {
                if (this.dao.getBanco().findByItem((InterfaceBanco) objB)) {
                    Pessoa pessoa = (Pessoa) objB;
                    System.out.println("Definindo valores");
                    this.pessoa = pessoa;
                    this.idPessoa = idPessoa;
                    this.tipo = (int) vetor.get(2);
                    this.login = (String) vetor.get(3);
                    this.senha = (String) vetor.get(4);
                    this.dataCriacao = (LocalDate) vetor.get(5);
                    this.dataModificacao = vetor.get(6) != null ? (LocalDate) vetor.get(6) : null;
                    return alterado;

                } else {
                    System.out.println("Objeto nao encontrado no banco");
                }
            }
        }
        return false;
    }

    public void setPessoa(Pessoa pessoa) {
        if (pessoa != null) {
            this.pessoa = pessoa;
            this.pessoa.setUserVinculado(true);

            this.setIdPessoa(this.pessoa.getId());
        }
    }

    public void setDataModificacao(LocalDate dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public void update(List<Object> vetor) {
        boolean alterou = false;
        if (vetor.get(1) != null && !((String) vetor.get(1)).isEmpty()) {
            int idPessoa = Util.stringToInt((String) vetor.get(1));
            if (idPessoa != 0 && this.getId() != idPessoa) {
                Pessoa pessoa = (Pessoa) this.dao.getItemByID(2, idPessoa);
                this.trocarPessoa(idPessoa, pessoa);
                alterou = true;

            }
        }

        if (vetor.get(2) != null && !((String) vetor.get(1)).isEmpty()) {
            String login = (String) vetor.get(2);
            if (!login.isEmpty()) {
                this.login = login;
                alterou = true;

            }
        }
        if (vetor.get(3) != null && !((String) vetor.get(1)).isEmpty()) {
            String senha = (String) vetor.get(3);
            if (!senha.isEmpty()) {
                this.senha = senha;
                alterou = true;

            }
        }
        if (vetor.get(4) != null && !((String) vetor.get(1)).isEmpty()) {
            int tipoUsuario = Util.stringToInt((String) vetor.get(4));
            if (tipoUsuario != 0) {
                this.tipo = tipoUsuario;
                alterou = true;

            }
        }
        if (alterou) {
            this.atualizarDataModificacao();
        }

    }

    public boolean criar(DAO dao, List<Object> vetor) {
        this.dao = dao;
        boolean criou = false;
        if (this.dao != null) {

            int idP = Util.stringToInt((String) vetor.get(0));
            Pessoa pessoa = (Pessoa) dao.getItemByID(2, idP);
            if (pessoa != null) {

                if (!pessoa.isUserVinculado()) {
                    this.id = this.dao.getTotalClasse(3);
                    this.trocarPessoa(idP, pessoa);
                    //Pessoa pessoa, String login, String senha, int tipo
                    if (vetor.get(0) != null && vetor.get(1) != null && vetor.get(2) != null) {
                        String login = (String) vetor.get(1);
                        String senha = (String) vetor.get(2);

                        if (!login.isEmpty() && !senha.isEmpty()) {


                            this.login = login;
                            this.senha = senha;
                            this.pessoa.setUserVinculado(true);
                            this.dataCriacao = LocalDate.now();
                            this.dataModificacao = null;
                            if (this.pessoa.getTipo().equalsIgnoreCase("ADMIN")
                                    || this.pessoa.getTipo().equalsIgnoreCase("NOIVO")
                                    || this.pessoa.getTipo().equalsIgnoreCase("NOIVA")
                                    || this.pessoa.getTipo().equalsIgnoreCase("CERIMONIAL")) {

                                this.tipo = 1;
                            } else {
                                this.tipo = 2;
                            }

                            criou = true;

                        }

                    }
                } else if (pessoa.isUserVinculado()) {
                    Util.mostrarErro("A conta de usuário de " + pessoa.getNome() + " já existe!");
                }

            } else {
                Util.mostrarErro("Pessoa de id " + vetor.getFirst() + " não encontrada");

            }
        }

        return criou;
    }


    public boolean checarTipo(int tipo) {
        return (tipo >= 0 && tipo <= 2);
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Adiciona informações do usuário
        resultado.append("\n\nUsuário ").append(this.id);
        resultado.append("\n Nome: ").append(this.getNome());
        resultado.append("\n Login: ").append(this.login);
        resultado.append("\n Senha: ").append(this.senha);
        resultado.append("\n Tipo: ").append(this.pessoa.getTipo());

        // Verifica e formata a data de criação
        if (this.dataCriacao != null) {
            resultado.append("\n Data de Criação: ").append(this.dataCriacao.format(formatter));
        }

        // Verifica e formata a data de modificação
        if (this.dataModificacao != null) {
            resultado.append("\n Data da Última Modificação: ").append(this.dataModificacao.format(formatter));
        }

        return resultado.toString();
    }


    public void atualizar() {
        this.dataModificacao = LocalDate.now();
    }

    public boolean deletar() {
        this.pessoa.setUserVinculado(false);
        return true;
    }


    public boolean trocarPessoa(int idPessoa, Pessoa p) {
        //checa se o id é diferente e se pessoa já não tem user vinculado
        if (
                (this.getIdPessoa() == 0 || this.getIdPessoa() != idPessoa)
                        && p != null && !p.isUserVinculado()
        ) {

            if (this.getIdPessoa() > 0 && this.getPessoa() != null) {
                this.getPessoa().setUserVinculado(false);
            }

            this.setPessoa(p);

            return true;
        }
        return false;
    }

    public void apagar() {
        this.pessoa.setUserVinculado(false);
        this.pessoa.setCerimonialVinculado(false);

        try {
            this.dao.delItemByID(3, this.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        Usuario.total = total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public String getSenha() {
        return this.senha;
    }

    public String getNome() {
        return this.pessoa.getNome();
    }

    public int getId() {
        return this.id;
    }

    public int getTipo() {
        return this.tipo;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }


    public void atualizarDataModificacao() {

        this.dataModificacao = LocalDate.now();
    }

    public LocalDate getDataModificacao() {
        return dataModificacao;
    }

    public static int getTotalUsuario() {
        return total;
    }

    public static void setTotalUsuario(int totalUsuario) {
        Usuario.total = totalUsuario;
    }


    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
