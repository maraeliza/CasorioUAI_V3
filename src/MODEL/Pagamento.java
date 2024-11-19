/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.Util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pagamento implements InterfaceClasse {

    private int id;
    private LocalDate data;
    private int idPessoa;
    private Pessoa pessoa;

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

    private int idUser;
    private Usuario user;

    public static String[] getCampos() {
        String[] campos = new String[10];
        campos[0] = "ID: ";
        campos[1] = "ID DO FORNECEDOR (0 PARA NENHUM FORNECEDOR): ";
        campos[2] = "DATA DO PAGAMENTO (DD/MM/YYYY): ";
        campos[3] = "DESCRIÇÃO: ";
        campos[4] = "VALOR: ";

        return campos;
    }

    public boolean trocarDespesa(int id, Despesa despesa) {

        //checa se o id é diferente
        if ((this.getIdDespesa() == 0 || this.getIdDespesa() != id)
                && despesa != null) {
            this.setIdDespesa(id);
            this.setDespesa(despesa);

            return true;
        }
        return false;
    }

    public boolean trocarParcela(int id, Parcela parcela) {

        //checa se o id é diferente
        if ((this.getIdParcela() == 0 || this.getIdParcela() != id)
                && parcela != null) {

            this.setIdParcela(id);
            this.setParcela(parcela);

            return true;
        }
        return false;
    }

    public boolean trocarPessoa(int idPessoa, Pessoa p) {

        //checa se o id é diferente
        if ((this.getIdPessoa() == 0 || this.getIdPessoa() != idPessoa)
                && p != null) {

            this.setPessoa(p);

            return true;
        }
        return false;
    }

    public boolean trocarFornecedor(int idFornecedor) {
        if (idFornecedor != 0) {
            Fornecedor fornecedor = (Fornecedor) this.dao.getItemByID(4, idFornecedor);

            if (fornecedor != null) {
                //checa se o id é diferente
                if (this.getIdFornecedor() == 0 || this.getIdFornecedor() != idFornecedor) {
                    System.out.println("Vinculando o fornecedor " + fornecedor.getNome() + " ao pagamento ");
                    this.setIdFornecedor(idFornecedor);
                    this.setFornecedor(fornecedor);
                    this.getFornecedor().atualizarValores();

                    return true;
                }
            }
        }

        return false;
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        System.out.println("Lançando pagamento!");
        this.dao = dao;
        boolean alterado = false;
        if (this.dao != null) {
            Pessoa pessoa = null;
            if (this.dao.getUserLogado() != null) {
                pessoa = this.dao.getUserLogado().getPessoa();
            } else {
                pessoa = (Pessoa) this.dao.getItemByID(2, 0);
            }

            if (pessoa != null) {
                this.trocarPessoa(pessoa.getId(), pessoa);
            }
            int idFornecedor = 0;

            if (vetor.get(0) instanceof String) {
                // Converte o elemento para String e então para int
                idFornecedor = Util.stringToInt((String) vetor.get(0));
            } else if (vetor.get(0) instanceof Integer) {
                // Faz o cast direto para int se o elemento já for Integer
                idFornecedor = (Integer) vetor.get(0);
            } else {
                throw new IllegalArgumentException("Tipo não suportado no vetor.get(0)");
            }
            if (idFornecedor != 0) {

                this.trocarFornecedor(idFornecedor);

            }
            if (vetor.get(0) != null && vetor.get(1) != null && vetor.get(2) != null && vetor.get(3) != null) {

                if (vetor.get(1) instanceof String) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String dateStr = (String) vetor.get(1);
                    this.data = LocalDate.parse(dateStr, formatter);

                } else if (vetor.get(1) instanceof LocalDate) {
                    this.data = (LocalDate) vetor.get(1);

                } else {
                    throw new IllegalArgumentException("Tipo não suportado no vetor.get(0)");
                }

                if (this.dao.getDataHoje().isBefore(this.data)) {
                    System.out.println("Não foi possível cadastrar pagamento para o futuro, se deseja agendar, lance uma despesa e agende o pagamento!");
                    alterado = false;
                } else {
                    this.descricao = (String) vetor.get(2); // Descrição

                    double valor;

                    if (vetor.get(3) instanceof String) {
                        valor = Util.stringToDouble((String) vetor.get(3));
                    } else if (vetor.get(3) instanceof Double) {
                        valor = (Double) vetor.get(3);
                    } else if (vetor.get(3) instanceof Integer) {
                        valor = ((Integer) vetor.get(3)).doubleValue();
                    } else {
                        throw new IllegalArgumentException("Tipo não suportado no vetor.get(3)");
                    }
                    if (valor > 0) {
                        this.setValor(valor);
                    }
                    if (vetor.get(5) != null) {
                        int idDespesa;
                        if (vetor.get(5) instanceof String) {
                            idDespesa = Util.stringToInt((String) vetor.get(5));
                        } else if (vetor.get(5) instanceof Integer) {
                            idDespesa = (Integer) vetor.get(5);
                        } else {
                            throw new IllegalArgumentException("Tipo não suportado no vetor.get(5)");
                        }
                        Despesa despesa = (Despesa) dao.getItemByID(12, idDespesa);

                        if (despesa != null) {

                            this.trocarDespesa(despesa.getId(), despesa);
                        }
                    }
                    if (vetor.get(6) != null) {

                        int idParcela;

                        if (vetor.get(6) instanceof String) {
                            idParcela = Util.stringToInt((String) vetor.get(6));
                        } else if (vetor.get(6) instanceof Integer) {
                            idParcela = (Integer) vetor.get(6);
                        } else {
                            throw new IllegalArgumentException("Tipo não suportado no vetor.get(6)");
                        }

                        Parcela parcela = (Parcela) dao.getItemByID(13, idParcela);
                        if (parcela != null) {

                            this.trocarParcela(parcela.getId(), parcela);

                            int nParcela = 0;
                            if (vetor.get(4) instanceof String) {
                                nParcela = Util.stringToInt((String) vetor.get(4));
                            } else if (vetor.get(4) instanceof Integer) {
                                nParcela = (Integer) vetor.get(4);
                            } else {
                                throw new IllegalArgumentException("Tipo não suportado no vetor.get(0)");
                            }
                            if (nParcela != 0) {
                                this.setnParcela(nParcela);
                            }

                        }
                    }

                    alterado = true;
                }

            }
            if (alterado) {
                this.id = ++Pagamento.total;
                this.dataCriacao = LocalDate.now();
                this.dataModificacao = null;
            }

        }

        return alterado;
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

    // Método para verificar e atualizar o estado do pagamento
    public void verificarPagamentoAgendado() {
        LocalDate hoje = LocalDate.now();
        if (data.isEqual(hoje) && valor > 0) {
            // Lógica para atualizar o estado do pagamento
            // Se for uma parcela única ou se todas as parcelas foram pagas
            if (this.nParcela == 1 || valor <= 0) {
                // Atualizar o estado do fornecedor
                this.fornecedor.setEstado(1);
            }
        }
    }

    public void update(List<Object> vetor) {
        System.out.println(" " + vetor.get(0) + " " + vetor.get(1) + " " + vetor.get(2) + " " + vetor.get(3) + " " + vetor.get(4) + " " + vetor.get(5) + " " + vetor.get(6));
        boolean alterou = false;
        if (vetor.get(1) != null || !vetor.get(1).equals('0')) {
            int idFornecedor = Util.stringToInt((String) vetor.get(1));
            if (idFornecedor != 0) {
                this.trocarFornecedor(idFornecedor);
            }
        }

        // Verifica e atualiza data do pagamento (vetor.get(3))
        if (vetor.get(2) != null && !((String) vetor.get(2)).isEmpty()) {
            this.data = Util.stringToDate((String) vetor.get(2));

            alterou = true;
        }

        // Verifica e atualiza descrição (vetor.get(4))
        if (vetor.get(3) != null && !((String) vetor.get(3)).isEmpty()) {
            this.descricao = (String) vetor.get(3);

            alterou = true;
        }

        // Verifica e atualiza valor (vetor.get(5))
        if (vetor.get(4) != null && !((String) vetor.get(4)).isEmpty()) {
            this.valor = Util.stringToDouble((String) vetor.get(4));

            alterou = true;
        }
        // Atualiza a data de modificação caso tenha havido alguma alteração
        if (alterou) {
            this.atualizarDataModificacao();
        }
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

    public void setFornecedor(Fornecedor forn) {
        if (forn != null) {
            this.fornecedor = forn;
            this.setIdFornecedor(this.fornecedor.getId());
        }
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

    public boolean criar(DAO dao, Usuario user, List<Object> vetor) {
        if (user != null) {
            this.idUser = user.getId();
            this.user = user;
            return criar(dao, vetor);
        }
        return criar(dao, vetor);
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
        return nome;
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
