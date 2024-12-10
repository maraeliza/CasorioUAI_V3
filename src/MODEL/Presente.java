/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mara
 */
public class Presente implements InterfaceClasse, InterfaceBanco {

    private int id;
    private String nome;
    private String tipo;

    private String link;

    private int idPessoa;
    private Pessoa pessoa;

    private LocalDate dataCompra;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private boolean escolhido;

    public static int total;

    private boolean comprado;
    private DAO dao;

    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "NOME: ";
        campos[2] = "TIPO: ";
        campos[3] = "LINK DE COMPRA: ";
        return campos;
    }

    public Presente() {
        this.idPessoa = 0;
        this.escolhido = false;
        this.comprado = false;
    }

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("nome");
        campos.add("tipo");
        campos.add("link");
        campos.add("idPessoa");
        campos.add("dataCompra");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        campos.add("escolhido");
        campos.add("comprado");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.nome);
        valores.add(this.tipo);
        valores.add(this.link);
        valores.add(this.idPessoa);
        valores.add(this.dataCompra);
        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        valores.add(this.escolhido);
        valores.add(this.comprado);
        return valores;
    }

    @Override
    public String getNomeTabela() {
        return "TB_PRESENTE";
    }

    public static String getNomeTabelaByClass() {
        return "TB_PRESENTE";
    }

    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        boolean alterado = vetor.get(1) != null && vetor.get(2) != null;

        if (!alterado) {
            return false;
        } else {
            this.dao = dao;
            this.id = (int) vetor.get(0);
            this.nome = (String) vetor.get(1);
            this.tipo = (String) vetor.get(2);
            this.link = (String) vetor.get(3);

            if (vetor.get(4) != null && (int) vetor.get(4) != 0) {
                this.idPessoa = (int) vetor.get(4);
                Object objB = this.dao.getItemByID(2, this.idPessoa);
                if (this.dao.getBanco().findByItem((InterfaceBanco) objB)) {
                    this.pessoa = (Pessoa) objB;
                }
            }

            this.dataCompra = vetor.get(5) != null ? (LocalDate) vetor.get(5) : null;
            this.dataCriacao = vetor.get(6) != null ? (LocalDate) vetor.get(6) : null;
            this.dataModificacao = vetor.get(7) != null ? (LocalDate) vetor.get(7) : null;
            this.escolhido = (boolean) vetor.get(8);
            this.comprado = (boolean) vetor.get(9);
            return true;
        }
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        boolean alterado = false;
        this.dao = dao;
        if (vetor.get(0) != null && vetor.get(0) instanceof String) {
            this.nome = (String) vetor.get(0); // Nome
            if (vetor.get(1) != null && vetor.get(1) instanceof String) {
                this.tipo = (String) vetor.get(1); // Tipo
                if (vetor.get(2) != null && vetor.get(2) instanceof String) {
                    this.link = (String) vetor.get(2); // Tipo
                    alterado = true;
                }
            }

        }
        if (alterado) {
            // Atribui o ID único e define as datas de criação e modificação
            this.id = this.dao.getTotalClasse(1) + 1;
            this.dataCriacao = LocalDate.now();
            this.dataModificacao = null; // Nenhuma modificação inicial
            this.escolhido = false;
        }
        return alterado;
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Adiciona o ID
        resultado.append("\n\nID: ").append(this.id);

        // Verifica e adiciona o nome
        if (this.nome != null && !this.nome.isEmpty()) {
            resultado.append("\nNome: ").append(this.nome);
        }

        // Verifica e adiciona o tipo
        if (this.tipo != null && !this.tipo.isEmpty()) {
            resultado.append("\nTipo: ").append(this.tipo);
        }
        // Verifica e adiciona o tipo
        if (this.link != null && !this.link.isEmpty()) {
            resultado.append("\nLink de compra: ").append(this.link);
        }
        if (this.comprado) {
            resultado.append("\nComprado: SIM");
            if (this.pessoa != null && this.pessoa.getNome() != null && !this.pessoa.getNome().isEmpty()) {
                resultado.append("\nComprador(a): ").append(this.pessoa.getNome());
            }
        } else {
            resultado.append("\nComprado: NÃO");
        }
        // Verifica se foi escolhido e adiciona informações da pessoa
        if (this.escolhido) {
            resultado.append("\nEscolhido: SIM");
            if (this.pessoa != null && this.pessoa.getNome() != null && !this.pessoa.getNome().isEmpty()) {
                resultado.append("\nPresenteador(a): ").append(this.pessoa.getNome());
            }
        } else {
            resultado.append("\nEscolhido: NÃO");
        }

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

    public void update(List<Object> vetor) {
        boolean alterou = false;

        if (vetor.get(1) != null) {
            String nome = (String) vetor.get(1);
            if (!nome.isEmpty()) {
                this.nome = nome;
                alterou = true;

            }
        }
        if (vetor.get(2) != null) {
            String tipo = (String) vetor.get(2);
            if (!tipo.isEmpty()) {
                this.tipo = tipo;
                alterou = true;

            }
        }
        if (vetor.get(3) != null) {
            String link = (String) vetor.get(3);
            if (!link.isEmpty()) {
                this.link = link;
                alterou = true;

            }
        }

        if (alterou) {
            this.atualizarDataModificacao();
        }

    }

    public boolean comprar(Pessoa p) {
        boolean alterou = false;
        if (p != null && !this.comprado) {
            this.setPessoa(p);
            this.comprado = true;
            this.setIdPessoa(this.pessoa.getId());
            alterou = true;
        } else if (this.comprado) {
            this.comprado = false;
            alterou = true;
        }
        if (alterou) {
            this.atualizarDataModificacao();
            this.dao.getBanco().updateItemBanco(this);
        }
        return alterou;
    }

    public boolean escolher(Pessoa p) {
        boolean alterou = false;
        if (p != null && !this.escolhido) {
            this.setPessoa(p);
            this.escolhido = true;

            alterou = true;
        } else if (this.escolhido) {
            this.setPessoa(null);
            this.escolhido = false;
            alterou = true;
        }
        if (alterou) {
            this.atualizarDataModificacao();
            this.dao.getBanco().updateItemBanco(this);
        }
        return alterou;
    }

    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public boolean deletar() {

        return true;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalPresentes() {
        return total;
    }

    public static void setTotal(int t) {
        total = t;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.dataModificacao = LocalDate.now();
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
        this.dataModificacao = LocalDate.now();
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        if (pessoa != null) {

            this.pessoa = pessoa;
            this.setIdPessoa(this.pessoa.getId());
        } else {
            this.pessoa = null;
            this.setIdPessoa(0);
        }
        this.dataModificacao = LocalDate.now();
    }

    public boolean getEscolhido() {
        return this.escolhido;

    }

    public void setEscolhido(boolean escolhido) {
        this.escolhido = escolhido;
        this.dataModificacao = LocalDate.now();
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }
}
