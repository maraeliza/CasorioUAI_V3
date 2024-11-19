/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author Jussie
 */
import CONTROLLER.DAO;
import VIEW.Util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Igreja implements InterfaceClasse, InterfaceBanco {

    private int id;
    private String nome;
    private String endereco;

    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    private boolean eventoVinculado;

    public static int total;
    private DAO dao;

    public static String[] getCampos() {
        String[] campos = new String[3]; // Somente 3 campos necessários
        campos[0] = "ID: ";
        campos[1] = "Nome: ";
        campos[2] = "Endereço: ";

        return campos;
    }

    @Override
    public List<String> getCamposSQL() {

        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("nome");
        campos.add("endereco");
        campos.add("eventoVinculado");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> campos = new ArrayList<>();
        campos.add(this.id);
        campos.add(this.nome);
        campos.add(this.endereco);
        campos.add(this.eventoVinculado);
        campos.add(this.dataCriacao);
        campos.add(this.dataModificacao);

        return campos;
    }

    @Override
    public String getNomeTabela() {
        return "tb_igreja";
    }

    public static String getNomeTabelaByClass() {
        return "tb_igreja";
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
            this.endereco = (String) vetor.get(2);
            alterado = true;
        }
        if (vetor.get(3) != null) {
            if (vetor.get(3) instanceof String) {
                this.eventoVinculado = ((String) vetor.get(3)).equals("true");

            } else if (vetor.get(3) instanceof Boolean) {
                this.eventoVinculado = (Boolean) vetor.get(3);
            } else {
                this.eventoVinculado = false;
            }

            alterado = true;
        }
        if (vetor.get(4) != null) {
            this.dataCriacao = (LocalDate) vetor.get(4);
            alterado = true;
        }
        if (vetor.get(5) != null) {
            this.dataModificacao = (LocalDate) vetor.get(5);
            alterado = true;
        }
        return alterado;

    }

    @Override
    public boolean criar(DAO dao, List<Object> vetor) {
        boolean alterado = false;

        if (vetor.get(0) != null) {
            this.nome = (String) vetor.get(0);
            alterado = true;
        }

        if (vetor.get(1) != null) {
            this.endereco = (String) vetor.get(1);
            alterado = true;
        }

        if (alterado) {
            this.dataCriacao = LocalDate.now();
            this.dataModificacao = null;
            this.id = ++Igreja.total;
        }

        return alterado;
    }

    @Override
    public void update(List<Object> vetor) {
        boolean alterou = false;

        if (vetor.get(1) != null) {
            String novoNome = (String) vetor.get(1);
            if (novoNome.length() > 0) {
                this.nome = novoNome;
                alterou = true;
            }
        }

        if (vetor.get(2) != null) {
            String novoEndereco = (String) vetor.get(2);
            if (novoEndereco.length() > 0) {
                this.endereco = novoEndereco;
                alterou = true;
            }
        }

        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    // Método para atualizar a data de modificação
    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    @Override
    // Método para deletar igreja
    public boolean deletar() {
        if (this.isEventoVinculado()) {
            Util.mostrarErro("Não é possível excluir a Igreja " + this.getNome() + ", pois está vinculada a um evento");
            return false;
        } else {
            return true;
        }
    }

    // Método para ler os dados da igreja
    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Adiciona informações da igreja
        resultado.append("Igreja ").append(this.id);
        resultado.append("\nNome: ").append(this.nome);
        resultado.append("\nEndereço: ").append(this.endereco);

        // Verifica e formata a data de criação
        if (this.dataCriacao != null) {
            resultado.append("\nData de Criação: ").append(this.dataCriacao.format(formatter));
        }

        // Verifica e formata a data de modificação
        if (this.dataModificacao != null) {
            resultado.append("\nData da Última Modificação: ").append(this.dataModificacao.format(formatter));
        }

        resultado.append("\n\n");
        return resultado.toString();
    }

    public boolean isEventoVinculado() {
        return eventoVinculado;
    }

    public void setEventoVinculado(boolean eventoVinculado) {
        this.eventoVinculado = eventoVinculado;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getTotal() {
        return Igreja.total;
    }

    public static void setTotal(int total) {
        Igreja.total = total;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.dataModificacao = LocalDate.now();
    }

    public String getEndereco() {
        return this.endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
        this.dataModificacao = LocalDate.now();
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }
}
