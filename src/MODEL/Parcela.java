/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parcela implements InterfaceClasse {

    private int id;

    private int idDespesa;
    private Despesa despesa;
    private String nome;
    private double valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private LocalDate dataAgendamento;
    private boolean pago;
    private boolean agendado;
    private String status;

    private int n;
    private int nTotal;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    private Usuario user;
    private DAO dao;

    private static int total;

    public Parcela() {
        this.pago = false;
        this.agendado = false;
    }

    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "ID DA DESPESA: ";
        campos[2] = "DATA DE VENCIMENTO (DD/MM/YYYY): ";
        campos[3] = "VALOR: ";
        return campos;
    }

    @Override
    public boolean criar(DAO dao, List<Object> vetor) {

        this.dao = dao;
        boolean alterado = false;

        if (vetor.get(0) != null) {

            this.idDespesa = (int) vetor.get(0);

            if (this.idDespesa != 0) {

                Despesa despesa = (Despesa) this.dao.getItemByID(12, this.idDespesa);

                if (despesa != null) {

                    this.setDespesa(despesa);

                    if (vetor.get(1) != null) {
                        this.dataVencimento = (LocalDate) vetor.get(1);

                        if (vetor.get(2) != null) {

                            double valorFormatado = (double) vetor.get(2);
                            this.valor = valorFormatado;
                            if (vetor.get(3) != null) {
                                this.n = (int) vetor.get(3);
                            }
                            if (vetor.get(4) != null) {
                                this.setNTotal((int) vetor.get(4));
                            }
                            if (vetor.get(5) != null) {
                                this.setNome((String) vetor.get(5));
                            }
                            alterado = true;

                            // Atribui o ID único e define as datas de criação e modificação
                            this.id = ++Parcela.total;
                            this.dataCriacao = LocalDate.now();
                            this.dataModificacao = null; // Nenhuma modificação inicial
                            this.pago = false;
                            this.agendado = false;
                            this.status = this.isVencida() ? "VENCIDA" : "PENDENTE";
                        }
                    }
                }
            }

        }

        return alterado;
    }

    public boolean isVencida() {
        return this.dataVencimento != null && this.dataVencimento.isBefore(LocalDate.now());
    }

    public boolean deletar() {

        return true;
    }

    public String lerParcelaAgendada() {
        String texto = "";
        texto += "\nID DA DESPESA: " + this.getIdDespesa() + "\nNOME DA DESPESA: " + this.getDespesa().getNome();
        texto += "\nPARCELA: " + this.getN() + " de " + this.getNTotal();
        texto += "\nVALOR: " + this.getValor();

        texto += "\nDATA DO PAGAMENTO AGENDADO: " + this.getDataAgendamento() + "\n";
        return texto;
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        resultado.append("\nID: ").append(this.id).append("         ");

        if (this.nome != null && !this.nome.isEmpty()) {
            resultado.append("         DESPESA: ").append(this.nome).append("\n");
        }

        resultado.append("Valor: R$").append(String.format("%.2f", this.valor)).append("\n");

        if (this.dataVencimento != null) {
            resultado.append("Data de Vencimento: ").append(this.dataVencimento.format(formatter)).append("\n");
        }

        if (this.dataPagamento != null) {
            resultado.append("Data de Pagamento: ").append(this.dataPagamento.format(formatter)).append("\n");
        }

        resultado.append("Pago: ").append(this.pago ? "Sim" : "Não").append("\n");
        if (!this.pago) {
            resultado.append("Pagamento Agendado: ").append(this.agendado ? "Sim" : "Não").append("\n");
            if (this.isAgendado()) {
                resultado.append("Data do Agendamento: ").append(this.dataAgendamento.format(formatter)).append("\n");
            }
            if (this.status != null && !this.status.isEmpty()) {
                resultado.append("Status: ").append(this.status).append("\n");
            }

        }
        resultado.append("Parcela: ").append(this.n).append(" de ").append(this.getNTotal()).append("\n");

        return resultado.toString();
    }

    public boolean agendar(LocalDate dataAgendamento) {
        LocalDate hoje = LocalDate.now();
        if (hoje.isAfter(dataAgendamento)) {
            Util.mostrarErro("Não é possível agendar para pagamento para o passado!");
            return false;
        } else {
            if (this.isAgendado()) {
                Util.mostrarErro("Agendamento cancelado!");
                this.setAgendado(false);
                this.setDataAgendamento(null);
            } else {
                Util.mostrarMSG("Agendamento feito com sucesso!");
                this.setAgendado(true);
                this.setDataAgendamento(dataAgendamento);
            }
            return true;
        }

    }

    public void cancelarAgendamento() {
        this.setAgendado(false);
        this.setDataAgendamento(null);
    }

    public void agendarForce(LocalDate dataAgendamento) {
        LocalDate hoje = LocalDate.now();
        if (hoje.isBefore(dataAgendamento)) {
            this.setAgendado(true);
            this.setDataAgendamento(dataAgendamento);
        }
    }

    public void pagar(boolean quitandoDespesa, boolean entrandoNoSistema) {
        if (!this.isPago()) {

            LocalDate hoje = LocalDate.now();
            this.setPago(true);
            this.setDataPagamento(hoje);
            this.setStatus("PAGA");
            this.setAgendado(false);

            if (this.despesa != null) {
                //cadastrar pagamento da parcela
                ArrayList<Object> infos = new ArrayList<>(Arrays.asList(this.despesa.getIdFornecedor(), hoje, this.despesa.getDescricao(), this.getValor(), this.getN(), this.getIdDespesa(), this.getId()));
                try {
                    this.dao.cadastrar(11, infos);
                } catch (Exception e) {
                }

            }
        }

    }

    public void pagar(boolean quitandoDespesa) {
        if (!this.isPago()) {

            LocalDate hoje = LocalDate.now();
            this.setPago(true);
            this.setDataPagamento(hoje);
            this.setStatus("PAGA");
            this.setAgendado(false);

            if (this.despesa != null) {
                ArrayList<Object> infos = new ArrayList<>(Arrays.asList(this.despesa.getIdFornecedor(), hoje, this.despesa.getDescricao(), this.getValor(), this.getN(), this.getIdDespesa(), this.getId()));
                try {
                    this.dao.cadastrar(11, infos);
                    if (!quitandoDespesa) {
                        Menu_READ menuVer = new Menu_READ();
                        menuVer.exibir(this.dao, 11);
                    }
                } catch (Exception e) {
                }

            }
        }

    }

    public void cancelarPagamento() {
        if (this.isPago()) {
            this.setPago(false); // Marca a parcela como não paga
            this.setDataPagamento(null); // Remove a data de pagamento
            this.status = this.isVencida() ? "VENCIDA" : "PENDENTE"; // Atualiza o status

        }
    }

    @Override
    public void update(List<Object> vetor) {

        boolean alterou = false;

        // Atualiza a data de modificação caso tenha havido alguma alteração
        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    public boolean trocarDespesa(int id) {
        Despesa despesa = (Despesa) this.dao.getItemByID(12, id);

        //checa se o id é diferente
        if ((this.getIdDespesa() == 0 || this.getIdDespesa() != id)
                && despesa != null) {
            this.setIdDespesa(id);
            this.setDespesa(despesa);

            return true;
        }
        return false;
    }

    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(int idDespesa) {
        this.idDespesa = idDespesa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome + " " + this.getN() + "/" + this.getNTotal();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public boolean isAgendado() {
        return agendado;
    }

    public void setAgendado(boolean agendado) {
        this.agendado = agendado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
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

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        Parcela.total = total;
    }

    public Despesa getDespesa() {
        return despesa;
    }

    public void setDespesa(Despesa despesa) {
        this.despesa = despesa;
    }

    public int getnTotal() {
        return nTotal;
    }

    public void setnTotal(int nTotal) {
        this.nTotal = nTotal;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public int getNTotal() {
        return this.nTotal;
    }

    public void setNTotal(int nTotal) {
        this.nTotal = nTotal;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

}
