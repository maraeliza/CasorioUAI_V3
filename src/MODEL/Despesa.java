/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.Menu_READ;
import VIEW.Util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Despesa implements InterfaceClasse, InterfaceBanco {

    private int id;

    private int idFornecedor;
    private Fornecedor fornecedor;

    private double valorTotal;

    private LocalDate dataPrimeiroVencimento;
    private LocalDate dataUltimoVencimento;
    private LocalDate dataAgendamento;
    private LocalDate dataQuitacao;
    private boolean pago;
    private boolean agendado;
    private boolean parcelado;
    private int nParcelas;
    private List<Parcela> vParcelas;

    private String descricao;
    private String nome;

    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    private DAO dao;

    private static int total;

    public Despesa() {
        vParcelas = new ArrayList<>();
        this.pago = false;
        this.agendado = false;
        this.parcelado = false;
        this.getvParcelas().clear();
    }

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("idFornecedor");
        campos.add("valorTotal");
        campos.add("descricao");
        campos.add("nome");
        campos.add("dataPrimeiroVencimento");
        campos.add("dataUltimoVencimento");
        campos.add("dataAgendamento");
        campos.add("dataQuitacao");
        campos.add("pago");
        campos.add("agendado");
        campos.add("parcelado");
        campos.add("nParcelas");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.idFornecedor);
        valores.add(this.valorTotal);
        valores.add(this.descricao);
        valores.add(this.nome);
        valores.add(this.dataPrimeiroVencimento);
        valores.add(this.dataUltimoVencimento);
        valores.add(this.dataAgendamento);
        valores.add(this.dataQuitacao);
        valores.add(this.pago);
        valores.add(this.agendado);
        valores.add(this.parcelado);
        valores.add(this.nParcelas);
        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        return valores;
    }

    @Override
    public String getNomeTabela() {
        return "TB_DESPESA";
    }

    public static String getNomeTabelaByClass() {
        return "TB_DESPESA";
    }

    @Override    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {

        this.getvParcelas().clear();
        boolean alterado = vetor.get(0) != null;
        if (!alterado) {
            return alterado;
        } else {
            this.dao = dao;
            this.id = (int) vetor.get(0);
            int idFornecedor = (int) vetor.get(1);
            Object objB = this.dao.getItemByID(4, idFornecedor); // Assumindo o ID é 3 para fornecedor

            if (objB != null) {
                if (this.dao.getBanco().findByItem((InterfaceBanco) objB)) {

                    this.idFornecedor = idFornecedor;
                    this.fornecedor = (Fornecedor) objB;

                } else {
                    System.out.println("Fornecedor não encontrado no banco");
                }
            }
            this.valorTotal = (vetor.get(2) != null) ? (double) vetor.get(2) : 0.0;
            this.descricao = (vetor.get(3) != null) ? (String) vetor.get(3) : "";
            this.nome = (vetor.get(4) != null) ? (String) vetor.get(4) : "";
            this.dataPrimeiroVencimento = (vetor.get(5) != null) ? (LocalDate) vetor.get(5) : null;
            this.dataUltimoVencimento = (vetor.get(6) != null) ? (LocalDate) vetor.get(6) : null;
            this.dataAgendamento = (vetor.get(7) != null) ? (LocalDate) vetor.get(7) : null;
            this.dataQuitacao = (vetor.get(8) != null) ? (LocalDate) vetor.get(8) : null;
            this.pago = vetor.get(9) != null && (boolean) vetor.get(9);
            this.agendado = vetor.get(10) != null && (boolean) vetor.get(10);
            this.parcelado = vetor.get(11) != null && (boolean) vetor.get(11);
            this.nParcelas = (vetor.get(12) != null) ? (int) vetor.get(12) : 0;
            this.dataCriacao = (vetor.get(13) != null) ? (LocalDate) vetor.get(13) : null;
            this.dataModificacao = (vetor.get(14) != null) ? (LocalDate) vetor.get(14) : null;

            return alterado;

        }

    }


    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "ID DO FORNECEDOR: ";

        campos[2] = "NOME DA DESPESA: ";

        campos[3] = "DESCRIÇÃO: ";
        campos[4] = "VALOR TOTAL: ";

        campos[5] = "Nº DE PARCELAS (1 se for à vista): ";
        campos[6] = "DATA DO PRIMEIRO VENCIMENTO (DD/MM/YYYY) (0 SE FOR À VISTA): ";
        return campos;
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        this.getvParcelas().clear();
        this.dao = dao;
        boolean alterado = false;
        if (this.dao != null) {

            this.trocarFornecedor(Util.stringToInt((String) vetor.get(0)));

            if (vetor.get(0) != null && vetor.get(2) != null && vetor.get(3) != null && vetor.get(4) != null && vetor.get(5) != null) {
                if (vetor.get(1) != null && vetor.get(1) instanceof String) {
                    this.setNome(((String) vetor.get(1)).toUpperCase()); // Nome
                    if (vetor.get(2) != null && vetor.get(2) instanceof String) {

                        this.setDescricao((String) vetor.get(2));

                        double valorTotal = Util.stringToDouble((String) vetor.get(3));
                        this.setValorTotal(valorTotal);

                        int nParcelas = Util.stringToInt((String) vetor.get(4));
                        this.setnParcelas(nParcelas);
                        if (this.getnParcelas() > 1 && !((String) vetor.get(5)).equals("0")) {
                            this.setParcelado(true);
                            this.setDataPrimeiroVencimento((String) vetor.get(5));
                        } else {
                            this.setParcelado(false);
                        }
                        this.id = this.dao.getTotalClasse(12) + 1;

                    }
                }

                alterado = true;

            }
            if (alterado) {

                // Atribui o ID único e define as datas de criação e modificação
                total++;
                this.dataCriacao = LocalDate.now();
                this.dataModificacao = null; // Nenhuma modificação inicial
            }

        }

        return alterado;
    }

    public void criarParcelas() {

        Double valor = this.getValorTotal() / this.getnParcelas();
        this.vParcelas = new ArrayList<>();
        for (int i = 0; i < this.getnParcelas(); i++) {
            LocalDate dataVencimento = this.getDataPrimeiroVencimento().plusMonths(i);
            List<Object> infos = new ArrayList<>(Arrays.asList(this.getId(), dataVencimento, valor, i + 1, this.getnParcelas(), this.getNome()));
            try {
                Parcela objeto = new Parcela();
                boolean criado = objeto.criar(this.dao, infos);
                System.out.println("PARCELA CRIADA? "+criado);
                if (criado) {
                    System.out.println("ADD EM VPARCELAS " + this.vParcelas.size());
                    this.vParcelas.add(objeto);
                    System.out.println("ADICIONADO!!" + this.vParcelas.size());
                    this.dao.addVetor(13, objeto);

                }
            } catch (Exception e) {
                System.err.println("Erro ao criar parcela: " + e.getMessage());
            }
        }
        LocalDate dataVencimento = this.getDataPrimeiroVencimento().plusMonths(this.getnParcelas() - 1);
        this.setDataUltimoVencimento(dataVencimento);

    }

    public void addVParcelaNoBanco() {
        for (Parcela objeto : this.getvParcelas()) {
            if (!this.dao.getBanco().findByItem(objeto)) {
                this.dao.getBanco().addItemBanco(objeto);
            }
        }
    }


    public void trocarFornecedor(int idFornecedor) {
        if (idFornecedor != 0) {
            Fornecedor fornecedor = (Fornecedor) this.dao.getItemByID(4, idFornecedor);

            if (fornecedor != null) {
                //checa se o id é diferente
                if (this.getIdFornecedor() == 0 || this.getIdFornecedor() != idFornecedor) {
                    this.setIdFornecedor(idFornecedor);
                    this.setFornecedor(fornecedor);
                    this.getFornecedor().atualizarValores();
                }
            }
        }

    }

    public boolean deletar() {
        return true;
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        resultado.append("\nID: ").append(this.id).append("      ");

        if (this.nome != null && !this.nome.isEmpty()) {
            resultado.append("DESPESA: ").append(this.nome).append("\n");
        }
        if (this.descricao != null && !this.descricao.isEmpty()) {
            resultado.append("Descrição: ").append(this.descricao).append("\n");
        }

        if (this.fornecedor != null && this.fornecedor.getNome() != null && !this.fornecedor.getNome().isEmpty()) {
            resultado.append("Fornecedor: ").append(this.fornecedor.getNome()).append("\n");
        }

        resultado.append("Valor Total: R$").append(String.format("%.2f", this.valorTotal)).append("\n");

        resultado.append("Pago: ").append(this.pago ? "Sim" : "Não").append("\n");
        if (!this.pago) {
            resultado.append("Pagamento Agendado: ").append(this.agendado ? "Sim" : "Não").append("\n");
            if (this.isAgendado()) {
                resultado.append("Data do Agendamento: ").append(this.dataAgendamento.format(formatter)).append("\n");

            }
            if (this.dataPrimeiroVencimento != null && !this.parcelado) {
                resultado.append("Data de Vencimento: ").append(this.dataPrimeiroVencimento.format(formatter)).append("\n");
            }
        } else {
            if (this.dataQuitacao != null) {
                resultado.append("Data de Quitação: ").append(this.dataQuitacao.format(formatter)).append("\n");
            }
        }

        if (this.parcelado) {
            if (this.dataPrimeiroVencimento != null && !this.pago) {
                resultado.append("Data do Primeiro Vencimento: ").append(this.dataPrimeiroVencimento.format(formatter)).append("\n");
            }

            if (this.dataUltimoVencimento != null) {
                resultado.append("Data do Último Vencimento: ").append(this.dataUltimoVencimento.format(formatter)).append("\n");
            }

            resultado.append("Modo de Pagamento: Parcelado\n");
            resultado.append("Número de Parcelas: ").append(this.nParcelas).append("\n");

        } else {
            resultado.append("Modo de Pagamento: À vista\n");
        }
        if (this.agendado) {
            if (this.dataCriacao != null) {
                resultado.append("Data de Criação: ").append(this.dataCriacao.format(formatter)).append("\n");
            }
        }

        resultado.append("\n");
        return resultado.toString();
    }

    public void update(List<Object> vetor) {
        boolean alterou = false;

        // Atualiza a data de modificação caso tenha havido alguma alteração
        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    public void cancelarAgendamento() {

        this.setAgendado(false);
        this.setDataAgendamento(null);
        if (this.isParcelado() && !this.getvParcelas().isEmpty()) {
            for (int p = 0; p < this.getnParcelas(); p++) {
                Parcela parcela = (Parcela) this.getvParcelas().get(p);
                if (parcela != null && !parcela.isPago()) {
                    parcela.cancelarAgendamento();
                }

            }
        }
    }

    public boolean agendar(LocalDate dataAgendamento, boolean entrandoNoSistema) {

        if (this.isAgendado()) {
            this.cancelarAgendamento();
        } else {

            this.setAgendado(true);
            this.setDataAgendamento(dataAgendamento);
            if (this.isParcelado() && !this.getvParcelas().isEmpty()) {
                for (int p = 0; p < this.getvParcelas().size(); p++) {
                    Parcela parcela = (Parcela) this.getvParcelas().get(p);
                    if (parcela != null && !parcela.isPago()) {
                        parcela.agendarForce(dataAgendamento);
                    }

                }
            }
        }
        return true;

    }

    public boolean agendar(LocalDate dataAgendamento) {

        if (this.isAgendado()) {
            this.cancelarAgendamento();
        } else {

            this.setAgendado(true);
            this.setDataAgendamento(dataAgendamento);
            if (this.isParcelado() && !this.getvParcelas().isEmpty()) {
                for (int p = 0; p < this.getnParcelas(); p++) {
                    Parcela parcela = (Parcela) this.getvParcelas().get(p);
                    if (parcela != null && !parcela.isPago()) {
                        parcela.agendarForce(dataAgendamento);
                    }

                }
            }
            Util.mostrarMSG("Agendamento feito com sucesso!");
        }
        this.atualizarDataModificacao();
        this.dao.getBanco().updateItemBanco(this);
        return true;

    }

    public void pagar() {
        if (!this.isPago()) {
            LocalDate hoje = LocalDate.now();
            this.setPago(true);
            this.setDataQuitacao(hoje);
            this.setAgendado(false);
            if (this.isParcelado() && !this.getvParcelas().isEmpty()) {
                for (int p = 0; p < this.getnParcelas(); p++) {
                    Parcela parcela = (Parcela) this.getvParcelas().get(p);
                    if (parcela != null && !parcela.isPago()) {
                        parcela.pagar(true);
                    }
                }
                Menu_READ menuVer = new Menu_READ();
                menuVer.exibir(this.dao, 11);
            } else {
                ArrayList<Object> infos = new ArrayList<>(Arrays.asList(this.getIdFornecedor(), hoje, this.getDescricao(), this.getValorTotal(), 1, this.getId(), 1));
                try {
                    this.dao.cadastrar(11, infos);
                    Menu_READ menuVer = new Menu_READ();
                    menuVer.exibir(this.dao, 11);
                } catch (Exception e) {
                    System.err.println("Despesa.java pagar() \nErro ao cadastrar: " + e.getMessage());
                }

            }
            this.atualizarDataModificacao();
            this.dao.getBanco().updateItemBanco(this);
        }

    }

    public void cancelarPagamento() {
        if (this.isPago()) {
            this.setPago(false);
            this.setDataQuitacao(null);

            if (this.isParcelado() && !this.getvParcelas().isEmpty()) {
                for (int p = 0; p < this.getnParcelas(); p++) {
                    Parcela parcela = (Parcela) this.getvParcelas().get(p);
                    if (parcela != null && parcela.isPago()) {
                        parcela.cancelarPagamento(); // Método em Parcela para cancelar pagamento individual da parcela
                    }
                }
            }
            this.atualizarDataModificacao();
            this.dao.getBanco().updateItemBanco(this);
        }
    }

    public void pagar(boolean entrandoNoSistema) {
        if (!this.isPago()) {
            LocalDate hoje = LocalDate.now();
            this.setPago(true);
            this.setDataQuitacao(hoje);
            this.setAgendado(false);
            if (this.isParcelado() && !this.getvParcelas().isEmpty()) {
                for (int p = 0; p < this.getnParcelas(); p++) {
                    Parcela parcela = this.getvParcelas().get(p);
                    if (parcela != null && !parcela.isPago()) {
                        parcela.pagar(true, entrandoNoSistema);
                    }

                }

            } else {
                ArrayList<Object> infos = new ArrayList<>(Arrays.asList(this.getIdFornecedor(), hoje, this.getDescricao(), this.getValorTotal(), 1, this.getId(), 1));
                try {

                    this.dao.cadastrar(11, infos);

                } catch (Exception e) {

                }
            }
            this.atualizarDataModificacao();
            this.dao.getBanco().updateItemBanco(this);
        }

    }

    public void atualizarDataModificacao() {

        this.dataModificacao = LocalDate.now();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataPrimeiroVencimento() {
        return dataPrimeiroVencimento;
    }

    public void setDataPrimeiroVencimento(String dataPrimeiroVencimento) {
        this.dataPrimeiroVencimento = Util.stringToDate(dataPrimeiroVencimento);
    }

    public LocalDate getDataUltimoVencimento() {
        return dataUltimoVencimento;
    }

    public void setDataUltimoVencimento(LocalDate dataUltimoVencimento) {
        this.dataUltimoVencimento = dataUltimoVencimento;
    }

    public LocalDate getDataQuitacao() {
        return dataQuitacao;
    }

    public void setDataQuitacao(LocalDate dataQuitacao) {
        this.dataQuitacao = dataQuitacao;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public int getnParcelas() {
        return nParcelas;
    }

    public void setnParcelas(int nParcelas) {
        this.nParcelas = nParcelas;
    }

    public List<Parcela> getvParcelas() {
        return vParcelas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        Despesa.total = total;
    }

    public boolean isAgendado() {
        return agendado;
    }

    public void setAgendado(boolean agendado) {
        this.agendado = agendado;
    }

    public boolean isParcelado() {
        return parcelado;
    }

    public void setParcelado(boolean parcelado) {
        this.parcelado = parcelado;
    }


    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

}
