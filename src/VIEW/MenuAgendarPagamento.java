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
public class MenuAgendarPagamento {

    private String nomeClasse;
    private String texto;
    
    private int idDespesa;
    private int idParcela;
    private int idClasse;

    private Despesa despesa;
    private Parcela parcela;
    private Usuario user;
    private DAO dao;

    public void exibir(DAO dao, int idClasse, Usuario user) {
        this.user = user;
        this.dao = dao;
        this.idClasse = idClasse;
        this.nomeClasse = this.dao.getNameClasseById(idClasse);

        try {
            this.montarPainel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTextoDespesa() {
        this.texto = "";
        this.texto = "\n AGENDAR PAGAMENTO ";

        this.texto += "\n DESPESAS";
        this.texto += "\n" + this.dao.getNomes(12) + "\n\n";
        this.texto += "\n\nINSIRA: \nID DA DESPESA ➡ CONFIRMAR/CANCELAR AGENDAMENTO:" + "\n" + "DIGITE 0                ➡ PARA VOLTAR";

        return this.texto;
    }

    public String getTextoParcela(int idDespesa) {
        this.texto = "";
        this.texto = "\n AGENDAR PAGAMENTO ";

        this.texto += "\n PARCELAS";
        this.texto += "\n" + this.dao.getParcelasPendentes(idDespesa) + "\n";
        this.texto += "\nINSIRA:\nID DA PARCELA ➡ AGENDAR/CANCELAR PAGAMENTO DA PARCELA:\nDIGITE A                 ➡ QUITAR/CANCELAR PAGAMENTO DA DESPESA \nDIGITE 0                 ➡ PARA VOLTAR ";
        String result = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);

        return result;
    }

    public LocalDate getDataAgendamento() {
        //senão, perguntar a data
        this.texto = "";
        this.texto = "\n AGENDAR PAGAMENTO ";
        this.texto += "\n\n INSIRA A DATA QUE DESEJA QUE O PAGAMENTO SEJA REALIZADO: ";
        String result = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            LocalDate dataAgendamento = Util.stringToDate(result);
            return dataAgendamento;
        }
        return null;
    }

    public void lidarEscolha(String textoInserido) {
        if (textoInserido.equals("A") || !textoInserido.equals("0")) {

            //pega a despesa
            if (despesa != null) {
                //checa se a despesa não está paga
                if (!despesa.isPago()) {
                    //checa se a pessoa quis editar uma parcela especifica
                    if (!textoInserido.equals("A")) {

                        int idParcela = Util.stringToInt(textoInserido);
                        if (idParcela != 0) {
                            this.setIdParcela(idParcela);
                            if (this.getParcela() != null) {
                                //se estiver agendada
                                if (this.getParcela().isAgendado()) {
                                    //cancela agendamento
                                    this.getParcela().cancelarAgendamento();
                                    this.verResultado(13);
                                } else {
                                    //senão, pede a data de agendamento
                                    LocalDate data = this.getDataAgendamento();
                                    LocalDate hoje = LocalDate.now();
                                    //checa se a data está no futuro
                                    if (hoje.isBefore(data)) {
                                        //se sim, realiza o agendamento
                                        this.getParcela().agendar(data);
                                        this.dao.mostrarPagamentosAgendados();
                                        this.criarMenuCRUD(this.dao, 11);
                                    }

                                }
                            }

                        }

                    } else {
                        //perguntar a data para agendar
                        LocalDate data = this.getDataAgendamento();
                        LocalDate hoje = LocalDate.now();
                        if (hoje.isBefore(data)) {
                            //agendar a quitação da despesa
                            this.getDespesa().agendar(data);
                            this.dao.mostrarPagamentosAgendados();
                            this.criarMenuCRUD(this.dao, 11);
                        }else{
                            Util.mostrarErro("Não é possível agendar pagamentos para o passado!");
                            this.criarMenuCRUD(this.dao, 11);
                        }
                    }

                }
            }
        } else {
            //voltar para o menu anterior
            this.criarMenuCRUD(this.dao, 11);
        }

    }

    public void montarPainel() {
        String result = JOptionPane.showInputDialog(null, this.getTextoDespesa(), "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInserido = Util.stringToInt(result);
            if (idInserido != 0) {

                try {
                    boolean existe = this.dao.find(12, idInserido);
                    if (existe) {
                        this.setIdDespesa(idInserido);
                        if (this.getDespesa() != null) {
                            if (this.getDespesa().isAgendado()) {
                                this.getDespesa().cancelarAgendamento();
                                Util.mostrarMSG("Agendamento de pagamento foi cancelado com sucesso!");
                                this.dao.mostrarPagamentosAgendados();
                                this.criarMenuCRUD(this.dao, 11);
                                
                            } else {
                                this.lidarEscolha(this.getTextoParcela(idInserido));
                            }
                        }

                    } else {
                        Util.mostrarErro("Elemento de id " + result + " não encontrado!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.criarMenuCRUD(this.dao, 11);
            }

        } else {
            Util.mostrarErro("Atualização cancelada!");
        }

    }

    public void verResultado(int idClasse){
        Menu_READ menuVer = new Menu_READ();
        menuVer.exibir(this.dao, idClasse);
        this.criarMenuCRUD(this.dao, 11);
    }
   
    public void criarMenuCRUD(DAO dao, int idClasse) {
        Menu_CRUD menu = new Menu_CRUD();
        menu.exibir(this.dao, idClasse);
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public int getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(int idDespesa) {
        this.idDespesa = idDespesa;
        Despesa despesa = (Despesa) this.dao.getItemByID(12, this.idDespesa);
        this.setDespesa(despesa);
    }

    public int getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(int idParcela) {
        this.idParcela = idParcela;
        Parcela parcela = (Parcela) this.dao.getItemByID(13, this.idParcela);
        this.setParcela(parcela);
    }

    public Despesa getDespesa() {
        return despesa;
    }

    public void setDespesa(Despesa despesa) {
        if (despesa != null) {
            this.despesa = despesa;
        }

    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        if (parcela != null) {
            this.parcela = parcela;
        }
    }

}
