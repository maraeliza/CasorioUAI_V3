/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import MODEL.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class MenuComprarPresente {

    private String nomeClasse;
    private String texto;
    private String vetor[];
    private String valores[];
    private Class classe;
    private int nColetados;
    private DAO dao;
    private int idClasse;
    private Usuario user;

    public void exibir(DAO dao, int idClasse) {
        this.user = dao.getUserLogado();
        this.dao = dao;
        this.idClasse = idClasse;
        this.vetor = new String[10];
        this.nomeClasse = this.dao.getNameClasseById(idClasse);
        try {
            this.getTexto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTexto() {
        this.texto = "";
        this.cleanVetor();
        this.texto = this.dao.getPresentesEscolhidos(this.user);
        montarPainel(texto);

        return "";
    }

    public void montarPainel(String objetos) {
        String conteudo = "";
        conteudo = "\nCONFIRMAR COMPRA DE " + this.nomeClasse.toUpperCase();
        conteudo += "\n" + objetos + "\n\n";
        conteudo += "\n\nINSIRA: \nID DO PRESENTE ➡ PARA CONFIRMAR/CANCELAR COMPRA:" + "\n" + "DIGITE 0                 ➡ PARA VOLTAR";
        String result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInserido = Util.stringToInt(result);
            if (idInserido != 0) {
                try {
                    boolean existe = this.dao.isInList(this.idClasse, idInserido);
                    if (existe) {
                        Presente presente = (Presente) this.dao.getItemByID(idClasse, idInserido);
                        if (presente != null && presente.getEscolhido()) {
                            //checa se a pessoa que está logada agora é a pessoa que escolheu o presente
                            if (this.dao.getUserLogado().getIdPessoa() == presente.getIdPessoa()) {
                                if (presente.isComprado()) {

                                    presente.comprar(this.user.getPessoa());
                                    if (presente.isComprado()) {
                                        Util.mostrarMSG("Compra do presente " + presente.getNome() + " confirmada com sucesso!");
                                    } else {
                                        Util.mostrarMSG("Confirmação de compra do presente cancelada!");
                                    }

                                } else {
                                    presente.comprar(this.user.getPessoa());
                                    if (presente.isComprado()) {
                                        Util.mostrarMSG("Compra do presente " + presente.getNome() + " confirmada com sucesso!");
                                    } else {
                                        Util.mostrarMSG("Confirmação de compra do presente cancelada!");
                                    }
                                }
                            }else{
                                Util.mostrarMSG("Não foi possível alterar a confirmação de compra, pois o presente de id " + idInserido + " não foi escolhido por você!");
                            }
                        } else {
                            if (presente == null) {
                                Util.mostrarMSG("Presente com id " + result + "não encontrado!");
                            } else {
                                Util.mostrarMSG("Não foi possível confirmar a compra, pois o presente de id " + idInserido + " ainda não foi escolhido!");
                            }

                        }

                        this.exibir(this.dao, this.idClasse);
                    } else {
                        Util.mostrarErro("Elemento de id " + result + " não encontrado!");
                        this.exibir(this.dao, this.idClasse);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Util.criarMenuCRUD(this.dao, 1);
            }

        } else {
            Util.criarMenuCRUD(this.dao, 1);
        }

    }

    public boolean add(String atributo) {
        for (int i = 0; i < this.vetor.length; i++) {
            if (this.vetor[i] == null) {
                this.vetor[i] = atributo;
                return true;
            }
        }
        return false;
    }

    public boolean cleanVetor() {
        for (int i = 0; i < this.vetor.length; i++) {
            if (this.vetor[i] != null) {
                this.vetor[i] = null;
                return true;
            }
        }
        return false;
    }

}
