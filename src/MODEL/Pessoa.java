/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.Util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CAUPT - ALUNOS
 */
public class Pessoa implements InterfaceClasse, InterfaceBanco {

    private int id;
    private int idade;
    private String nome;
    private String telefone;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private LocalDate nascimento;
    private boolean userVinculado;
    private boolean cerimonialVinculado;
    private boolean convidadoVinculado;

    private String tipo;
    public static int total;
    private DAO dao;

    public static String[] getCampos() {
        String[] campos = new String[5];
        campos[0] = "ID: ";
        campos[1] = "NOME: ";
        campos[2] = "TELEFONE: ";
        campos[3] = "TIPO (noivo/noiva, cerimonial, etc): ";
        campos[4] = "DATA DE NASCIMENTO: ";
        return campos;
    }

    public Pessoa() {
        this.userVinculado = false;
        this.cerimonialVinculado = false;
        this.convidadoVinculado = false;
    }

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");

        campos.add("nome");
        campos.add("telefone");
        campos.add("tipo");
        campos.add("nascimento");

        campos.add("dataCriacao");
        campos.add("dataModificacao");

        campos.add("idade");
        campos.add("userVinculado");
        campos.add("cerimonialVinculado");
        campos.add("convidadoVinculado");

        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);

        valores.add(this.nome);
        valores.add(this.telefone);
        valores.add(this.tipo);
        valores.add(this.nascimento);

        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        valores.add(this.idade);

        valores.add(this.userVinculado);
        valores.add(this.cerimonialVinculado);
        valores.add(this.convidadoVinculado);

        return valores;
    }

    @Override
    public String getNomeTabela() {
        return "tb_pessoa";
    }

    public static String getNomeTabelaByClass() {
        return "tb_pessoa";
    }
    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        boolean alterado = false;
    
        if (vetor.get(0) != null) {
            this.id = (int) vetor.get(0);
            alterado = true;
        }
        if (vetor.get(1) != null) {
            this.nome = (String) vetor.get(1);
            alterado = true;
        }
        if (vetor.get(2) != null) {
            this.telefone = (String) vetor.get(2);
            alterado = true;
        }
        if (vetor.get(3) != null) {
            this.tipo = (String) vetor.get(3);
            alterado = true;
        }
        if (vetor.get(4) != null) {
            this.nascimento = (LocalDate) vetor.get(4);
            alterado = true;
        }
        if (vetor.get(5) != null) {
            this.dataCriacao = (LocalDate) vetor.get(5);
            alterado = true;
        }
        if (vetor.get(6) != null) {
            this.dataModificacao = (LocalDate) vetor.get(6);
            alterado = true;
        }
        if (vetor.get(7) != null) {
            this.idade = (int) vetor.get(7);
            alterado = true;
        }
        if (vetor.get(8) != null) {
            if (vetor.get(8) instanceof String) {
                this.userVinculado = ((String) vetor.get(8)).equalsIgnoreCase("true");
            } else if (vetor.get(8) instanceof Boolean) {
                this.userVinculado = (Boolean) vetor.get(8);
            } else {
                this.userVinculado = false;
            }
            alterado = true;
        }
        if (vetor.get(9) != null) {
            if (vetor.get(9) instanceof String) {
                this.cerimonialVinculado = ((String) vetor.get(9)).equalsIgnoreCase("true");
            } else if (vetor.get(9) instanceof Boolean) {
                this.cerimonialVinculado = (Boolean) vetor.get(9);
            } else {
                this.cerimonialVinculado = false;
            }
            alterado = true;
        }
        if (vetor.get(10) != null) {
            if (vetor.get(10) instanceof String) {
                this.convidadoVinculado = ((String) vetor.get(10)).equalsIgnoreCase("true");
            } else if (vetor.get(10) instanceof Boolean) {
                this.convidadoVinculado = (Boolean) vetor.get(10);
            } else {
                this.convidadoVinculado = false;
            }
            alterado = true;
        }
    
        return alterado;
    }
    

    public void calcularIdade() {
        if (this.nascimento != null) {
            LocalDate hoje = LocalDate.now();
            int idade = hoje.getYear() - this.nascimento.getYear();

            // Verifica se a data de nascimento já ocorreu este ano
            if (hoje.getMonthValue() < this.nascimento.getMonthValue()
                    || (hoje.getMonthValue() == this.nascimento.getMonthValue() && hoje.getDayOfMonth() < this.nascimento.getDayOfMonth())) {
                idade--;
            }
            this.setIdade(idade);
        }

    }

    public void update(List<Object> vetor) {
        boolean alterou = false;

        // Atualiza o nome
        if (vetor.get(1) != null && vetor.get(1) instanceof String) {
            String nome = (String) vetor.get(1);
            if (!nome.isEmpty()) {
                this.nome = nome;
                alterou = true;
            }
        }

        // Atualiza o telefone
        if (vetor.get(2) != null && vetor.get(2) instanceof String) {
            String telefone = (String) vetor.get(2);
            if (!telefone.isEmpty()) {
                this.telefone = telefone;
                alterou = true;
            }
        }
        // Atualiza o tipo
        if (vetor.get(3) != null && vetor.get(3) instanceof String) {
            String tipo = (String) vetor.get(3);
            if (!tipo.isEmpty()) {
                this.tipo = tipo;
                alterou = true;
            }
        }
        if (vetor.get(4) != null && vetor.get(4) instanceof String) {
            String nascimentoStr = (String) vetor.get(4);
            try {
                if (nascimentoStr.length() > 0) {
                    this.nascimento = Util.stringToDate(nascimentoStr);
                    this.calcularIdade();
                    alterou = true;
                }
               
            } catch (DateTimeParseException e) {
            }
        }

        // Se alguma alteração foi feita, atualiza a data de modificação
        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        boolean criado = false;
        this.dao = dao;
        if (this.dao != null) {
            if (vetor.get(0) != null && vetor.get(0) instanceof String) {
                this.nome = (String) vetor.get(0); // Nome
                if (vetor.get(1) != null && vetor.get(1) instanceof String) {
                    this.telefone = (String) vetor.get(1); // Telefone
                    if (vetor.get(2) != null && vetor.get(2) instanceof String) {
                        this.tipo = (String) vetor.get(2); // Tipo
                        if (vetor.get(3) != null && vetor.get(3) instanceof String) {
                            String nascimentoStr = (String) vetor.get(3);
                            try {
                                this.nascimento = Util.stringToDate(nascimentoStr);
                                if (this.nascimento != null) {
                                    this.calcularIdade();
                                    criado = true;
                                }

                            } catch (DateTimeParseException e) {
                                Util.mostrarErro("Formato de data inválido: " + nascimentoStr);
                            }
                        }
                    }

                }

            }
            if (criado) {
                // Atribui o ID único e define as datas de criação e modificação
                this.id = ++Pessoa.total;
                this.dataCriacao = LocalDate.now();
                this.dataModificacao = null; // Nenhuma modificação inicial

            }
        }

        return criado;
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Adiciona informações da pessoa
        resultado.append("\nID ").append(this.id);
        resultado.append("            NOME: ").append(this.nome.toUpperCase());

        // Verifica e formata a data de nascimento
        if (this.nascimento != null) {
            resultado.append("\nData de Nascimento: ").append(this.nascimento.format(formatter));
        }
        if (this.idade != 0) {
            resultado.append("\nIdade: ").append(this.idade);
        }
        // Verifica e adiciona o telefone
        if (this.telefone != null && !this.telefone.isEmpty()) {
            resultado.append("      Telefone: ").append(this.telefone);
        }
        // Verifica e adiciona o tipo
        if (this.tipo != null && !this.tipo.isEmpty()) {
            resultado.append("\nTIPO: ").append(this.tipo.toUpperCase());
        }
        resultado.append("\nUsuário Cadastrado: ");
        // Verifica e adiciona o usuario
        if (this.isUserVinculado()) {
            resultado.append("SIM");
        } else {
            resultado.append("NÃO");
        }

        // Verifica e formata a data de modificação
        if (this.dataModificacao != null) {
            resultado.append("\nData da Última Modificação: ").append(this.dataModificacao.format(formatter));
        }
        resultado.append("\n");

        return resultado.toString();
    }

    public boolean deletar() {
        if (this.userVinculado) {
            Util.mostrarErro(this.getNome() + " não pode ser deletado, tem usuário vinculado!");

            return false;
        } else {

            return true;
        }

    }

    public String getNome() {
        return this.nome;

    }

    public String getTipo() {
        return this.tipo.toUpperCase();

    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }

    public void atualizarDataModificacao() {

        this.dataModificacao = LocalDate.now();
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public boolean isConvidadoVinculado() {
        return convidadoVinculado;
    }

    public void setConvidadoVinculado(boolean convidadoVinculado) {
        this.convidadoVinculado = convidadoVinculado;
    }

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public boolean isCerimonialVinculado() {
        return cerimonialVinculado;
    }

    public void setCerimonialVinculado(boolean cerimonialVinculado) {
        this.cerimonialVinculado = cerimonialVinculado;
    }

    public boolean isUserVinculado() {
        return userVinculado;
    }

    public void setUserVinculado(boolean userVinculado) {
        this.userVinculado = userVinculado;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataModificacao(LocalDate dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public static int getTotal() {
        return Pessoa.total;
    }

    public static void setTotal(int total) {
        Pessoa.total = total;
    }

}
