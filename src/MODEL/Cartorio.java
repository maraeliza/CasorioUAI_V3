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
import java.util.List;


public class Cartorio implements InterfaceClasse, InterfaceBanco {

    private int id;
    private String nome;
    private String telefone;
    private String endereco;
    private boolean eventoVinculado;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private DAO dao;
    public static int total;

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("nome");
        campos.add("telefone");
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
        campos.add(this.telefone);
        campos.add(this.endereco);
        campos.add(this.eventoVinculado);
        campos.add(this.dataCriacao);
        campos.add(this.dataModificacao);
        return campos;
    }

    @Override
    public String getNomeTabela() {
        return "tb_cartorio";
    }
    public static String getNomeTabelaByClass() {
        return "tb_cartorio";
    }

    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        boolean alterado = false;

        if (vetor.get(0) != null) {
            this.id = (int) vetor.getFirst();
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
            this.endereco = (String) vetor.get(3);
            alterado = true;
        }
        if (vetor.get(4) != null) {
            this.eventoVinculado = (boolean) vetor.get(4);
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
        vetor.clear();
        return alterado;
    }

    @Override
    public boolean criar(DAO dao,List<Object> vetor) {
        boolean alterado = false;

        if (vetor.get(0) != null) {
            this.nome = (String) vetor.getFirst();
            alterado = true;
        }

        if (vetor.get(1) != null) {
            this.telefone = (String) vetor.get(1);
            alterado = true;
        }

        if (vetor.get(2) != null) {
            this.endereco = (String) vetor.get(2);
            alterado = true;
        }

        if (alterado) {
            this.dataCriacao = LocalDate.now();
            this.dataModificacao = null;
            this.id = this.dao.getTotalClasse(8) +1; // Supondo que 'total' é um contador de IDs
        }
        vetor.clear();
        return alterado;
    }
    
    @Override
    public void update(List<Object> vetor) {
        boolean alterou = false;

        if (vetor.get(1) != null) {
            String novoNome = (String) vetor.get(1);
            if (!novoNome.isEmpty() && !this.nome.equals(novoNome)) {
                this.nome = novoNome;
                alterou = true;
            }
        }

        if (vetor.get(2) != null) {
            String novoTelefone = (String) vetor.get(2);
            if (!novoTelefone.isEmpty() && !this.telefone.equals(novoTelefone)) {
                this.telefone = novoTelefone;
                alterou = true;
            }
        }

        if (vetor.get(3) != null) {
            String novoEndereco = (String) vetor.get(3);
            if (!novoEndereco.isEmpty() && !this.endereco.equals(novoEndereco)) {
                this.endereco = novoEndereco;
                alterou = true;
            }
        }
        vetor.clear();
        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "Nome: ";
        campos[2] = "Telefone: ";
        campos[3] = "Endereço: ";
        return campos;
    }
    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public boolean deletar() {
        if (this.isEventoVinculado()) {
            Util.mostrarErro("Não é possível excluir o cartório " + this.getNome() + ", pois ele está vinculado a um evento");
            return false;
        } else {
            return true;
        }
    }
    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        resultado.append("Cartório ").append(this.id);
        resultado.append("\nNome: ").append(this.nome);

        if (this.telefone != null && !this.telefone.isEmpty()) {
            resultado.append("\nTelefone: ").append(this.telefone);
        }

        if (this.endereco != null && !this.endereco.isEmpty()) {
            resultado.append("\nEndereço: ").append(this.endereco);
        }

        if (this.dataCriacao != null) {
            resultado.append("\nData de Criação: ").append(this.dataCriacao.format(formatter));
        }

        if (this.dataModificacao != null) {
            resultado.append("\nData da Última Modificação: ").append(this.dataModificacao.format(formatter));
        }

        resultado.append("\n\n");
        return resultado.toString();
    }

    // Getters e Setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getTotal() {
        return Cartorio.total;
    }

    public static void setTotal(int total) {
        Cartorio.total = total;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.dataModificacao = LocalDate.now();
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public boolean isEventoVinculado() {
        return eventoVinculado;
    }

    public void setEventoVinculado(boolean eventoVinculado) {
        this.eventoVinculado = eventoVinculado;
    }
    
}
