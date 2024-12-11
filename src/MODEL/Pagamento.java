package MODEL;

import CONTROLLER.DAO;
import VIEW.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pagamento implements InterfaceClasse, InterfaceBanco {

    private int id;
    private LocalDate data;
    private int idPessoa;
    private Pessoa pessoa;

    private int idUser;
    private Usuario user;

    private int idDespesa;
    private Despesa despesa;

    private int idParcela;
    private Parcela parcela;

    private String descricao;
    private String nome;

    private int idFornecedor;
    private Fornecedor fornecedor;

    private double valor;

    private int nParcela;

    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    private DAO dao;
    private static int total;

    @Override
    public String getNomeTabela() {
        return "tb_pagamento";
    }

    public static String getNomeTabelaByClass() {
        return "tb_pagamento";
    }

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("data");
        campos.add("idPessoa");
        campos.add("idUser");
        campos.add("idDespesa");
        campos.add("idParcela");
        campos.add("descricao");
        campos.add("nome");
        campos.add("idFornecedor");
        campos.add("valor");
        campos.add("nParcela");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.data);
        valores.add(this.idPessoa);
        valores.add(this.idUser);
        valores.add(this.idDespesa);
        valores.add(this.idParcela);
        valores.add(this.descricao);
        valores.add(this.nome);
        valores.add(this.idFornecedor);
        valores.add(this.valor);
        valores.add(this.nParcela);
        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        return valores;
    }

    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        boolean alterado = vetor.get(0) != null && vetor.get(1) != null;
        if (!alterado) {
            return false;
        } else {
            try {
                this.dao = dao;
                this.id = (int) vetor.get(0);
                this.data = vetor.get(1) != null ? (LocalDate) vetor.get(1) : null;
                int idPessoa = (int) vetor.get(2);
                Object objB = this.dao.getItemByID(2, idPessoa);
                if (objB != null) {
                    if (this.dao.getBanco().findByItem((InterfaceBanco) objB)) {
                        this.pessoa = (Pessoa) objB;
                        this.idPessoa = idPessoa;
                    }
                }
                int idUser = (int) vetor.get(3);
                Object objUser = dao.getItemByID(3, idUser);
                if (objUser != null && dao.getBanco().findByItem((InterfaceBanco) objUser)) {
                    this.user = (Usuario) objUser;
                    this.idUser = idUser;
                }

                int idDespesa = (int) vetor.get(4);
                Object objDespesa = dao.getItemByID(12, idDespesa);
                if (objDespesa != null && dao.getBanco().findByItem((InterfaceBanco) objDespesa)) {
                    this.despesa = (Despesa) objDespesa;
                    this.idDespesa = idDespesa;
                }

                int idParcela = (int) vetor.get(5);
                Object objParcela = dao.getItemByID(13, idParcela);
                if (objParcela != null && dao.getBanco().findByItem((InterfaceBanco) objDespesa)) {
                    this.parcela = (Parcela) objParcela;
                    this.idParcela = idParcela;
                }

                this.descricao = (String) vetor.get(6);
                this.nome = (String) vetor.get(7);

                int idFornecedor = (int) vetor.get(8);
                Object objFornecedor = dao.getItemByID(4, idFornecedor);
                if (objFornecedor != null && dao.getBanco().findByItem((InterfaceBanco) objFornecedor)) {
                    this.fornecedor = (Fornecedor) objFornecedor;
                    this.idFornecedor = idFornecedor;

                }

                this.valor = (double) vetor.get(9);
                this.nParcela = (int) vetor.get(10);

                this.dataCriacao = vetor.get(11) != null ? (LocalDate) vetor.get(11) : null;
                this.dataModificacao = vetor.get(12) != null ? (LocalDate) vetor.get(12) : null;

                return true;
            } catch (Exception e) {
                System.err.println("ERRO AO DEFINIR VALORES");
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "ID DO FORNECEDOR (0 PARA NENHUM FORNECEDOR): ";
        campos[2] = "DESCRIÇÃO: ";
        campos[3] = "VALOR: ";

        return campos;
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        this.dao = dao;
        boolean alterado = false;
        if (this.dao != null) {
            for (int i = 0; i < vetor.size(); i++){
                System.out.println(" ID "+i+ " "+vetor.get(i));
            }
            if(vetor.size() >= 7){
                this.data = vetor.get(6) != null ? (LocalDate) vetor.get(6) : null;

            }else{
                this.data = LocalDate.now();
            }
            Pessoa pessoa = null;
            if (this.dao.getUserLogado() != null) {
                this.user = this.dao.getUserLogado();
                this.idUser = this.user.getId();
                this.pessoa = this.dao.getUserLogado().getPessoa();
                this.idPessoa = this.getPessoa().getId();

            } else {
                pessoa = (Pessoa) this.dao.getItemByID(2, 0);
            }

            if (pessoa != null) {
                this.trocarPessoa(pessoa.getId(), pessoa);
            }
            int idFornecedor = 0;

            if (vetor.get(0) instanceof String) {
                idFornecedor = Util.stringToInt((String) vetor.getFirst());
            } else if (vetor.getFirst() instanceof Integer) {
                idFornecedor = (Integer) vetor.getFirst();
            } else {
                throw new IllegalArgumentException("Tipo não suportado no vetor.getFirst()");
            }
            if (idFornecedor != 0) {

                this.trocarFornecedor(idFornecedor);

            }
            if (vetor.getFirst() != null && vetor.get(1) != null && vetor.get(2) != null) {

                this.descricao = (String) vetor.get(1); // Descrição

                double valor;

                if (vetor.get(2) instanceof String) {
                    valor = Util.stringToDouble((String) vetor.get(2));
                } else if (vetor.get(2) instanceof Double) {
                    valor = (Double) vetor.get(2);
                } else if (vetor.get(2) instanceof Integer) {
                    valor = ((Integer) vetor.get(2)).doubleValue();
                } else {
                    throw new IllegalArgumentException("Tipo não suportado no vetor.get(3)");
                }
                if (valor > 0) {
                    this.setValor(valor);
                }

                if (vetor.size() > 3) {

                    if (vetor.get(4) != null) {
                        int idDespesa;
                        if (vetor.get(4) instanceof String) {
                            idDespesa = Util.stringToInt((String) vetor.get(4));
                        } else if (vetor.get(4) instanceof Integer) {
                            idDespesa = (Integer) vetor.get(4);
                        } else {
                            throw new IllegalArgumentException("Tipo não suportado no vetor.get(4)");
                        }
                        Despesa despesa = (Despesa) dao.getItemByID(12, idDespesa);

                        if (despesa != null) {

                            this.trocarDespesa(despesa.getId(), despesa);
                        }
                    }
                    if (vetor.get(5) != null) {

                        int idParcela;

                        if (vetor.get(5) instanceof String) {
                            idParcela = Util.stringToInt((String) vetor.get(5));
                        } else if (vetor.get(5) instanceof Integer) {
                            idParcela = (Integer) vetor.get(5);
                        } else {
                            throw new IllegalArgumentException("Tipo não suportado no vetor.get(5)");
                        }

                        Parcela parcela = (Parcela) dao.getItemByID(13, idParcela);
                        if (parcela != null) {

                            this.trocarParcela(parcela.getId(), parcela);

                            int nParcela = 0;
                            if (vetor.get(3) instanceof String) {
                                nParcela = Util.stringToInt((String) vetor.get(3));
                            } else if (vetor.get(3) instanceof Integer) {
                                nParcela = (Integer) vetor.get(3);
                            } else {
                                throw new IllegalArgumentException("Tipo não suportado no vetor.getFirst()");
                            }
                            if (nParcela != 0) {
                                this.setnParcela(nParcela);
                            }

                        }
                    }
                }

                alterado = true;


            }
            if (alterado) {

                this.id = this.dao.getTotalClasse(11) + 1;
                this.dataCriacao = LocalDate.now();
                this.dataModificacao = null;
            }

        }

        return alterado;
    }

    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        resultado.append("\nID: ").append(this.id).append("\n");
        resultado.append("Valor Pago: R$").append(String.format("%.2f", this.valor)).append("\n");
        if (this.descricao != null && !this.descricao.isEmpty()) {

            if (this.despesa != null) {
                resultado.append("DESPESA: ").append(this.despesa.getNome()).append("            ");
                resultado.append("Valor Total: R$").append(String.format("%.2f", this.despesa.getValorTotal())).append("\n");
                if (this.despesa.isAgendado()) {
                    if (this.despesa.getDataAgendamento() != null) {
                        resultado.append("Data Agendada: ").append(this.despesa.getDataAgendamento().format(formatter)).append("\n");
                    }
                }
            }
            resultado.append("Descrição: ").append(this.descricao).append("\n");
        }

        if (this.fornecedor != null && this.fornecedor.getNome() != null && !this.fornecedor.getNome().isEmpty()) {
            resultado.append("Fornecedor: ").append(this.fornecedor.getNome()).append("\n");
        }
        if (this.data != null) {
            resultado.append("Data do Pagamento: ").append(this.data.format(formatter)).append("\n");
            if (this.parcela != null) {
                if (this.parcela.getDataVencimento() != null) {
                    resultado.append("Data de Vencimento: ")
                            .append(this.parcela.getDataVencimento().format(formatter)).append("\n");
                }

                if (this.parcela.isAgendado()) {
                    if (this.parcela.getDataAgendamento() != null) {
                        resultado.append("Data Agendada: ").append(this.parcela.getDataAgendamento().format(formatter)).append("\n");
                    }
                }
            }

        }

        if (this.pessoa != null && this.pessoa.getNome() != null && !this.pessoa.getNome().isEmpty()) {
            resultado.append("Pagador: ").append(this.pessoa.getNome()).append("\n");
        }

        if (this.dataModificacao != null) {
            resultado.append("Data da Última Modificação: ").append(this.dataModificacao.format(formatter)).append("\n");
        }

        return resultado.toString();
    }

    public void verificarPagamentoAgendado() {
        LocalDate hoje = LocalDate.now();
        if (data.isEqual(hoje) && valor > 0) {
            if (this.nParcela == 1) {
                this.fornecedor.setEstado(1);
            }
        }
    }

    public void update(List<Object> vetor) {

        boolean alterou = false;
        if (vetor.get(1) != null || !vetor.get(1).equals('0')) {
            int idFornecedor = Util.stringToInt((String) vetor.get(1));
            if (idFornecedor != 0) {
                this.trocarFornecedor(idFornecedor);
            }
        }
        if (vetor.get(2) != null && !((String) vetor.get(2)).isEmpty()) {
            this.data = Util.stringToDate((String) vetor.get(2));
            alterou = true;
        }

        if (vetor.get(3) != null && !((String) vetor.get(3)).isEmpty()) {
            this.descricao = (String) vetor.get(3);
            alterou = true;
        }

        if (vetor.get(4) != null && !((String) vetor.get(4)).isEmpty()) {
            this.valor = Util.stringToDouble((String) vetor.get(4));
            alterou = true;
        }
        if (alterou) {
            this.atualizarDataModificacao();
        }
    }

    public void trocarDespesa(int id, Despesa despesa) {
        if ((this.getIdDespesa() == 0 || this.getIdDespesa() != id)
                && despesa != null) {
            this.setIdDespesa(id);
            this.setDespesa(despesa);
        }
    }

    public void trocarParcela(int id, Parcela parcela) {
        if ((this.getIdParcela() == 0 || this.getIdParcela() != id)
                && parcela != null) {
            this.setIdParcela(id);
            this.setParcela(parcela);
        }
    }

    public void trocarPessoa(int idPessoa, Pessoa p) {
        if ((this.getIdPessoa() == 0 || this.getIdPessoa() != idPessoa)
                && p != null) {
            this.setPessoa(p);

        }
    }

    public void trocarFornecedor(int idFornecedor) {
        if (idFornecedor != 0) {
            Fornecedor fornecedor = (Fornecedor) this.dao.getItemByID(4, idFornecedor);

            if (fornecedor != null) {
                if (this.getIdFornecedor() == 0 || this.getIdFornecedor() != idFornecedor) {

                    this.setIdFornecedor(idFornecedor);
                    this.setFornecedor(fornecedor);
                    this.getFornecedor().atualizarValores();

                }
            }
        }

    }

    public void setFornecedor(Fornecedor forn) {
        if (forn != null) {
            this.fornecedor = forn;
            this.setIdFornecedor(this.fornecedor.getId());
        }
    }

    public boolean deletar() {
        if (this.idDespesa != 0) {
            this.despesa.cancelarPagamento();
        }
        if (this.idParcela != 0) {
            this.parcela.cancelarPagamento();
        }

        return true;
    }

    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public int getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(int idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Despesa getDespesa() {
        return despesa;
    }

    public void setDespesa(Despesa despesa) {
        this.despesa = despesa;
    }

    public int getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(int idParcela) {
        this.idParcela = idParcela;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public int getnParcela() {
        return nParcela;
    }

    public void setnParcela(int nParcela) {
        this.nParcela = nParcela;
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
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getNParcela() {
        return nParcela;
    }

    public void setParcela(int parcela) {
        this.nParcela = parcela;
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

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNome() {
        return descricao.split(" ")[0];
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        Pagamento.total = total;
    }
}
