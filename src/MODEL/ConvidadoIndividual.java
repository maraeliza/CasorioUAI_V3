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

/**
 *
 * @author CAUPT - ALUNOS
 */
public class ConvidadoIndividual implements InterfaceClasse {

    private int id;
    private int idPessoa;
    private String nome;
    private Pessoa pessoa;
    private int idFamilia;
    private ConvidadoFamilia familia;
    private int idUser;
    private Usuario user;
    private Usuario userLogado;

    private String parentesco;
    private boolean confirmacao;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    public static int total;

    private DAO dao;

    public static String[] getCampos() {
        String[] campos = new String[10]; // Somente 3 campos necessários
        campos[0] = "ID: ";
        campos[1] = "ID da pessoa: ";
        campos[2] = "ID da Familia: ";
        campos[3] = "Parentesco: ";
        return campos;
    }

    public void setPessoa(Pessoa pessoa) {
        if (pessoa != null) {
            this.pessoa = pessoa;
            this.pessoa.setConvidadoVinculado(true);

            this.setIdPessoa(this.pessoa.getId());
        }
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public boolean trocarFamilia(int idFamilia, ConvidadoFamilia familia) {
        // Verifica se o ID é diferente e se a família ainda não tem convidado vinculado
        if ((this.getIdFamilia() == 0 || this.getIdFamilia() != idFamilia)
                && familia != null) {

            // Define a nova família e atualiza o estado de vinculação
            this.setFamilia(familia);
            this.setIdFamilia(this.familia.getId());

            return true;
        }
        return false;
    }

    public void setFamilia(ConvidadoFamilia familia) {
        if (familia != null) {
            this.familia = familia;
            this.setIdFamilia(this.familia.getId());
        }
    }

    public boolean trocarPessoa(int idPessoa, Pessoa p) {

        //checa se o id é diferente e se pessoa já não tem convidado vinculado
        if ((this.getIdPessoa() == 0 || this.getIdPessoa() != idPessoa)
                && p != null && !p.isConvidadoVinculado()) {

            if (this.getIdPessoa() > 0 && this.getPessoa() != null) {
                this.getPessoa().setConvidadoVinculado(false);
            }

            this.setPessoa(p);

            return true;
        }
        return false;
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        this.dao = dao;
        boolean alterado = false;

        if (this.dao != null) {

            int idPessoaC = Util.stringToInt((String) vetor.get(0));

            Pessoa p = (Pessoa) this.dao.getItemByID(2, idPessoaC);

            if (p != null) {

                if (!p.isConvidadoVinculado() && p.getTipo().toUpperCase().equals("CONVIDADO")) {

                    if (!p.isUserVinculado()) {
                        this.trocarPessoa(idPessoaC, p);
                        int idFamilia = Util.stringToInt((String) vetor.get(1));

                        ConvidadoFamilia familia = (ConvidadoFamilia) this.dao.getItemByID(10, idFamilia);
                        if (familia != null) {
                            this.trocarFamilia(idFamilia, familia);

                            /* Código para criar acesso (usuario ) do convidado */
                            ArrayList<Object> userDados = new ArrayList<>(Arrays.asList((String) vetor.get(0), p.getNome().toUpperCase(), familia.getAcesso()));
                            try {
                                this.dao.cadastrar(3, userDados);
                                Usuario user = this.dao.getUserByIdPessoa(p.getId());
                            } catch (Exception e) {

                            }
                            if (user != null) {
                                this.setUser(user);
                                String parentesco = (String) vetor.get(2);
                                if (this.getPessoa() != null
                                        && this.getFamilia() != null
                                        && this.getUser() != null
                                        && parentesco.length() > 0) {
                                    this.setParentesco(parentesco);
                                    alterado = true;
                                }

                                if (alterado) {
                                    this.dataCriacao = LocalDate.now();
                                    this.dataModificacao = null;
                                    this.id = ++ConvidadoIndividual.total; // Incrementa o contador de IDs
                                }
                            }

                        }

                    }

                } else {
                    if (p.isConvidadoVinculado()) {
                        Util.mostrarErro("A conta de convidado para " + p.getNome() + " já existe!");

                    } else {
                        Util.mostrarErro("A pessoa " + p.getNome() + " não é do tipo 'convidado'!");

                    }

                }

            } else {
                Util.mostrarErro("Pessoa de ID " + idPessoaC + " não encontrada.");
            }
        }

        return alterado;
    }

    public void update(List<Object> vetor) {
        boolean alterou = false;

        // Atualiza a data de modificação caso tenha havido alguma alteração
        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Informações básicas do convidado
        resultado.append("\n\nConvidado ").append(this.id);
        resultado.append("\n\n Nome: ").append(this.getNome());
        // Informações de família associada
        if (this.familia != null) {
            resultado.append("\n Nome da Família: ").append(this.familia.getNome());
        }
        if (this.parentesco != null && this.parentesco.length() > 0) {
            // Informações adicionais
            resultado.append("\n Parentesco: ").append(this.parentesco);
        }
        resultado.append("\n Confirmação: ").append(this.confirmacao ? "Confirmado" : "Não Confirmado");
        // Informações do usuário associado
        if (this.user != null) {
            resultado.append("\n\n Login: ").append(this.user.getLogin());
            resultado.append("\n Senha: ").append(this.user.getSenha());
        }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(int idFamilia) {
        this.idFamilia = idFamilia;
    }

    public ConvidadoFamilia getFamilia() {
        return familia;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public boolean isConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(boolean confirmacao) {
        this.confirmacao = confirmacao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(LocalDate dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        ConvidadoIndividual.total = total;
    }

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        if (user != null) {
            this.setIdUser(user.getId());
            this.user = user;
        }

    }

    public Usuario getUserLogado() {
        return userLogado;
    }

    public void setUserLogado(Usuario userLogado) {
        this.userLogado = userLogado;
    }

    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public boolean deletar() {
        /*lembrar de deletar usuário tbm*/

        return true;
    }

    public String getNome() {
        return this.pessoa.getNome();
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.pessoa.setNome(nome);
    }
}
