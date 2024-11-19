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

public class Fornecedor implements InterfaceClasse, InterfaceBanco {

    private int id;
    private String nome;
    private String cnpj;
    private String telefone;
    private double valorAPagar;
    private double valorTotal;
    private double valorPago;
    private int parcelas;
    private int estado; // "pago" ou "em pagamento"
    private boolean quitado;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    public static int total;
    private DAO dao;

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("nome");
        campos.add("cnpj");
        campos.add("telefone");
        campos.add("valorAPagar");
        campos.add("valorTotal");
        campos.add("valorPago");
        campos.add("parcelas");
        campos.add("estado");
        campos.add("quitado");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.nome);
        valores.add(this.cnpj);
        valores.add(this.telefone);
        valores.add(this.valorAPagar);
        valores.add(this.valorTotal);
        valores.add(this.valorPago);
        valores.add(this.parcelas);
        valores.add(this.estado);
        valores.add(this.quitado);
        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        return valores;
    }

    @Override
    public String getNomeTabela() {
        return "tb_fornecedor";
    }

    public static String getNomeTabelaByClass() {
        return "tb_fornecedor";
    }

    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        boolean alterado = false;
        this.dao = dao;
        if (vetor.get(0) != null) {
            this.id = (int) vetor.get(0);
            alterado = true;
        }
        if (vetor.get(1) != null) {
            this.nome = (String) vetor.get(1);
            alterado = true;
        }
        if (vetor.get(2) != null) {
            this.cnpj = (String) vetor.get(2);
            alterado = true;
        }
        if (vetor.get(3) != null) {
            this.telefone = (String) vetor.get(3);
            alterado = true;
        }
        if (vetor.get(4) != null) {
            this.valorAPagar = (double) vetor.get(4);
            alterado = true;
        }
        if (vetor.get(5) != null) {
            this.valorTotal = (double) vetor.get(5);
            alterado = true;
        }
        if (vetor.get(6) != null) {
            this.valorPago = (double) vetor.get(6);
            alterado = true;
        }
        if (vetor.get(7) != null) {
            this.parcelas = (int) vetor.get(7);
            alterado = true;
        }
        if (vetor.get(8) != null) {
            this.estado = (int) vetor.get(8);
            alterado = true;
        }
        if (vetor.get(9) != null) {
            if (vetor.get(9) instanceof String) {
                this.quitado = ((String) vetor.get(9)).equals("true");
            } else if (vetor.get(9) instanceof Boolean) {
                this.quitado = (Boolean) vetor.get(9);
            } else {
                this.quitado = false;
            }
            alterado = true;
        }
        if (vetor.get(10) != null) {
            this.dataCriacao = (LocalDate) vetor.get(10);
            alterado = true;
        }
        if (vetor.get(11) != null) {
            this.dataModificacao = (LocalDate) vetor.get(11);
            alterado = true;
        }
        return alterado;
    }

    @Override
    public boolean criar(DAO dao, List<Object> vetor) {
        this.dao = dao;
        if (this.dao != null) {
            this.nome = (String) vetor.get(0);
            this.cnpj = (String) vetor.get(1);
            this.telefone = (String) vetor.get(2);
            this.dataCriacao = LocalDate.now();
            this.dataModificacao = null;
            this.id = ++Fornecedor.total; // Aumenta o contador de IDs
        }
        this.atualizarValores();
        return true;
    }

    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "nome: ";
        campos[2] = "CNPJ: ";
        campos[3] = "telefone: ";
        return campos;
    }

    public void update(List<Object> vetor) {
        boolean alterado = false;

        // Verifica e atualiza o nome
        if (vetor.get(1) != null && !((String) vetor.get(1)).trim().isEmpty()) {
            this.nome = (String) vetor.get(1);
            alterado = true;
        }

        // Verifica e atualiza o CNPJ
        if (vetor.get(2) != null && !((String) vetor.get(2)).trim().isEmpty()) {
            this.cnpj = (String) vetor.get(2);
            alterado = true;
        }

        // Verifica e atualiza o telefone
        if (vetor.get(3) != null && !((String) vetor.get(3)).trim().isEmpty()) {
            this.telefone = (String) vetor.get(3);
            alterado = true;
        }
        this.atualizarValores();
        if (alterado) {
            this.atualizarDataModificacao(); // Atualiza a data de modificação
        }
    }

    @Override
    public boolean deletar() {
        return true;
    }

    public void atualizarValores() {
        this.valorAPagar = 0.0;
        this.valorPago = 0.0;
        this.valorTotal = 0.0;

        //pega todas as despesas
        List<Object> despesas = this.dao.getTodosOsVetores().get(12);
        for (int n = 0; n < despesas.size(); n++) {

            if (despesas.get(n) != null) {

                Despesa despesa = (Despesa) despesas.get(n);

                if (despesa.getIdFornecedor() == this.getId()) {
                    this.valorTotal += despesa.getValorTotal();

                    //verifica se a despesa está paga  e adiciona ao valor pago
                    if (despesa.isPago()) {
                        this.valorPago += despesa.getValorTotal();
                    } else {
                        //caso seja parcelada e não quitada, percorre cada parcela, verifica se a parcela está paga e adiciona em valor pago
                        if (despesa.isParcelado()) {
                            for (int i = 0; i < despesa.getvParcelas().size(); i++) {
                                Parcela p = despesa.getvParcelas().get(i);
                                if (p != null) {
                                    if (p.isPago()) {
                                        this.valorPago += p.getValor();
                                    }
                                }
                            }
                        }

                    }

                }

            }
        }
        //calcula a diferença e define o valor a pagar
        this.valorAPagar = this.valorTotal - this.valorPago;
        this.setQuitado(this.valorAPagar == 0 && this.valorTotal > 0);
        try {
            this.dao.getBanco().updateItemBanco((InterfaceBanco) this);
        } catch (Exception e) {
        }
    }

    public String ler() {
        this.atualizarValores();
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        resultado.append("\nID: ").append(this.id).append("\n");

        if (this.nome != null && !this.nome.isEmpty()) {
            resultado.append("Nome: ").append(this.nome).append("\n");
        }

        if (this.cnpj != null && !this.cnpj.isEmpty()) {
            resultado.append("CNPJ: ").append(this.cnpj).append("\n");
        }

        if (this.telefone != null && !this.telefone.isEmpty()) {
            resultado.append("Telefone: ").append(this.telefone).append("\n");
        }
        if (this.valorTotal > 0) {
            resultado.append("Valor Total: ").append(this.valorTotal).append("            ");
            if (this.valorAPagar > 0) {
                resultado.append("Valor a Pagar: ").append(this.valorAPagar).append("\n");
                if (this.parcelas > 0) {
                    resultado.append("Parcelas: ").append(this.parcelas).append("\n");
                }
            }
            String estadoDescricao = this.quitado ? "Pago" : "Em andamento";
            resultado.append("Estado: ").append(estadoDescricao).append("\n");
        }

        if (this.dataModificacao != null) {
            resultado.append("Data de Modificação: ").append(this.dataModificacao.format(formatter));
        }
        resultado.append("\n");
        return resultado.toString();
    }

    public boolean isQuitado() {
        return quitado;
    }

    public void setQuitado(boolean quitado) {
        this.quitado = quitado;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        Fornecedor.total = total;
    }

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }
    // Getters e Setters

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        atualizarDataModificacao();
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
        atualizarDataModificacao();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
        atualizarDataModificacao();
    }

    public double getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(double valorAPagar) {
        this.valorAPagar = valorAPagar;
        atualizarDataModificacao();
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        if (parcelas != this.parcelas) {
            this.parcelas = parcelas;
        }

        atualizarDataModificacao();
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
        atualizarDataModificacao();
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }

    @Override
    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

}
