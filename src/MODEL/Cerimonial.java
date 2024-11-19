/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.Util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cerimonial implements InterfaceClasse {

    private int id;
    private int idUsuario;
    private int idPessoa;
    private Pessoa pessoa;
    private Usuario user;
    private String nome;

    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private boolean eventoVinculado;
    private DAO dao;

    public static int total;

    public Cerimonial() {
        this.eventoVinculado = false;
    }

    public static String[] getCampos() {
        String[] campos = new String[2];
        campos[0] = "ID: ";
        campos[1] = "ID da pessoa: ";

        return campos;
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        this.dao = dao;
        boolean alterado = false;
        if (this.dao != null) {

            int idPessoaC = Util.stringToInt((String) vetor.get(0));
            Pessoa p = (Pessoa) this.dao.getItemByID(2, idPessoaC);
            if (p != null) {

                if (!p.isCerimonialVinculado()
                        && p.getTipo().toUpperCase().equals("CERIMONIAL")
                        && !p.isUserVinculado()) {

                    this.trocarPessoa(idPessoaC, p);
                    ArrayList<Object> userDados = new ArrayList<>(Arrays.asList((String) vetor.get(0), p.getNome().toUpperCase(), "senhaCasorioUai"));
                    try {
                        this.dao.cadastrar(3, userDados);
                    } catch (Exception e) {

                    }

                    Usuario user = this.dao.getUserByIdPessoa(p.getId());
                    this.setUser(user);

                    if (this.getPessoa() != null && this.getUser() != null) {
                        alterado = true;
                    }

                    if (alterado) {
                        this.dataCriacao = LocalDate.now();
                        this.dataModificacao = null;
                        this.id = ++Cerimonial.total; // Supondo que 'total' é um contador de IDs
                    }

                } else {
                    if (p.isCerimonialVinculado() && p.isUserVinculado()) {
                        Util.mostrarErro("A conta de cerimonial de " + p.getNome() + " já existe!");
                    } else {
                        Util.mostrarErro("A pessoa " + p.getNome() + " não é do tipo 'cerimonial'!");
                    }

                }

            } else {
                Util.mostrarErro("Pessoa de id " + idPessoaC + " não encontrada");

            }
        }

        return alterado;
    }

    public void update(List<Object> vetor) {
        boolean alterou = false;
        int idPessoaC = Util.stringToInt((String) vetor.get(1));
        Pessoa p = (Pessoa) this.dao.getItemByID(2, idPessoaC);
        if (p != null) {
            if (!p.isCerimonialVinculado() && !p.isUserVinculado()
                    && p.getTipo().toUpperCase().equals("CERIMONIAL")) {
                this.trocarPessoa(idPessoaC, p);

                ArrayList<Object> userDados = new ArrayList<>(Arrays.asList((String) vetor.get(1), p.getNome().toUpperCase(), "senhaCasorioUai"));
                try {
                    this.dao.cadastrar(3, userDados);
                } catch (Exception e) {

                }

                Usuario user = this.dao.getUserByIdPessoa(p.getId());
                this.trocarUser(user);
                if (this.getPessoa() != null && this.getUser() != null) {
                    alterou = true;
                }

                if (alterou) {
                    this.atualizarDataModificacao();
                }
            } else {
                if (p.isCerimonialVinculado() && p.isUserVinculado()) {
                    Util.mostrarErro("A conta de cerimonial de " + p.getNome() + " já existe!");
                } else {
                    Util.mostrarErro("A pessoa " + p.getNome() + " não é do tipo 'cerimonial'!");
                }

            }

        } else {
            Util.mostrarErro("Pessoa de id " + vetor.get(0) + " não encontrada");

        }

    }

    // Método para deletar cerimonial
    public boolean deletar() {
        if (this.isEventoVinculado()) {
            Util.mostrarErro("Não é possível excluir o cerimonial " + this.getNome() + ", pois ele está vinculado a um evento");
            return false;
        } else {
            this.getUser().apagar();
            return true;
        }

    }

    // Método para ler os dados do cerimonial
    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Adiciona informações do cerimonial
        resultado.append("\n\nCERIMONIAL ").append(this.id);
        resultado.append("\nNome: ").append(this.nome);
        resultado.append("\nLogin: ").append(this.user.getLogin());
        resultado.append("\nSenha de acesso: ").append(this.user.getSenha());
        // Verifica e formata a data de criação
        if (this.dataCriacao != null) {
            resultado.append("\nData de Criação: ").append(this.dataCriacao.format(formatter));
        }

        // Verifica e formata a data de modificação
        if (this.dataModificacao != null) {
            resultado.append("\nData da Última Modificação: ").append(this.dataModificacao.format(formatter));
        }
        return resultado.toString();
    }

    public boolean trocarUser(Usuario user) {
        if (user != null) {

            if (this.getIdUsuario() == 0 && user.getId() != 0
                    || this.getIdUsuario() != user.getId() && user.getId() != 0) {

                if (this.getIdUsuario() != 0 && this.getUser() != null) {

                    this.getUser().apagar();
                }
                this.setIdUsuario(user.getId());
                this.setUser(user);

                return true;
            }
        }
        return false;
    }

    public boolean trocarPessoa(int idPessoa, Pessoa p) {

        //checa se o id é diferente e se pessoa já não tem cerimonial vinculado
        if ((this.getIdPessoa() == 0 || this.getIdPessoa() != idPessoa)
                && p != null && !p.isCerimonialVinculado()) {
            if (this.pessoa != null) {

            }

            if (this.getIdPessoa() > 0 && this.getPessoa() != null) {
                this.getPessoa().setCerimonialVinculado(false);
            }
            this.setIdPessoa(idPessoa);
            this.setPessoa(p);
            this.setNome(this.pessoa.getNome());

            return true;
        }
        return false;
    }

    public boolean isEventoVinculado() {
        return eventoVinculado;
    }

    public void setEventoVinculado(boolean eventoVinculado) {
        this.eventoVinculado = eventoVinculado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        if (this.pessoa != null) {
            this.pessoa.setUserVinculado(false);
            this.pessoa.setCerimonialVinculado(false);
        }

        this.pessoa = pessoa;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {

        this.user = user;
    }

    // Getters e Setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getTotal() {
        return Cerimonial.total;
    }

    public static void setTotal(int total) {
        Cerimonial.total = total;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.dataModificacao = LocalDate.now();
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }

    // Método para criar um novo cerimonial
    // Método para atualizar a data de modificação
    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }
}
