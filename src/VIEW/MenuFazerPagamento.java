/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import MODEL.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class MenuFazerPagamento {

    private String texto;
    private DAO dao;
    private Usuario user;

    public void exibir(DAO dao, int idClasse) {
        this.dao = dao;
        this.user = this.dao.getUserLogado();

        try {
            this.definirTexto();
            this.montarPainel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void definirTexto() {
        this.texto = "\n\n💲 FAZER PAGAMENTO 💸💲";
        this.texto += "\n\nEscolha a opção a seguir:  ";

        this.texto += "\n1. Pagar despesa";
        this.texto += "\n2. Pagar parcela de despesa";
        this.texto += "\n3. Lançar pagamento avulso";
        this.texto += "\n0. Para voltar";

        this.texto += "\n\nDigite aqui o número da sua opção: ";

    }

    public void montarPainel() {
        String result = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInserido = Util.stringToInt(result);
            if (idInserido != 0) {
                switch (idInserido) {
                    case 1 ->
                        this.pagarDespesa();
                    case 2 ->
                        this.pagarParcela();
                    case 3 ->
                        this.pagamentoAvulso();
                    default ->
                        this.criarMenuCRUD(this.dao, 11);
                }
            } else {
                this.criarMenuCRUD(this.dao, 11);
            }
        } else {
            Util.mostrarErro("Pagamento cancelado!");
        }

    }

    public void criarMenuCRUD(DAO dao, int idClasse) {
        Menu_CRUD menu = new Menu_CRUD();
        menu.exibir(this.dao, idClasse);
    }

    public void pagarParcela() {
        //mostrar o painel com as despesas e pede para escolher uma despesa
        String conteudo = "\nDESPESAS PENDENTES DE PAGAMENTO\n\n";
        conteudo += this.dao.getDespesasParceladasPendentes();
        String result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInseridoDespesa = Util.stringToInt(result);
            if (idInseridoDespesa != 0) {
                conteudo = "\nPARCELAS PENDENTES DE PAGAMENTO\n\n";
                conteudo += this.dao.getParcelasPendentes(idInseridoDespesa);
                result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
                if (result != null) {
                    int idInseridoParcela = Util.stringToInt(result);
                    if (idInseridoParcela != 0) {
                        Parcela parcela = (Parcela) this.dao.getItemByID(13, idInseridoParcela);
                        if (parcela != null && parcela.getIdDespesa() == idInseridoDespesa) {
                            parcela.pagar(false);
                            Util.mostrarMSG("Pagamento da parcela feito com sucesso!");
                        } else {
                            Util.mostrarMSG("Parcela não encontrada!");
                        }
                    }
                }
            }
        }

    }

    public void pagamentoAvulso() {
        System.out.println("Lançando pagamento avulso no sistema");
        Menu_CREATE menu = new Menu_CREATE();

        menu.exibir(this.dao, 11, this.user);
    }

    public void pagarDespesa() {
        String conteudo = "\nDESPESAS PENDENTES DE PAGAMENTO\n\n";
        conteudo += this.dao.getDespesasPendentes();
        String result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInserido = Util.stringToInt(result);
            if (idInserido != 0) {
                LocalDate hoje = LocalDate.now();
                Despesa despesa = (Despesa) this.dao.getItemByID(12, idInserido);
                if (despesa != null && !despesa.isPago()) {
                    despesa.pagar();
                    Util.mostrarMSG("Pagamento da despesa feito com sucesso!");
                } else {
                    Util.mostrarMSG("Despesa não encontrada ou já está paga!");
                }

            } else {
                this.criarMenuCRUD(this.dao, 11);
            }
        } else {
            this.criarMenuCRUD(this.dao, 11);
        }
    }

}
