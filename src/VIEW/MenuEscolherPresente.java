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
public class MenuEscolherPresente {

    private String nomeClasse;
    private String texto;

    private String vetor[];

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
        this.texto = this.dao.getTexto(this.idClasse);
        Class<?> classe = this.dao.getClasseByID(this.idClasse);
        try {
            java.lang.reflect.Method metodo = classe.getMethod("getCampos");
            this.vetor = (String[]) metodo.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        montarPainel();

        return "";
    }

    public void montarPainel() {

        String conteudo = "\nATUALIZAR CAMPO DE " + this.nomeClasse.toUpperCase();

        conteudo = "\nATUALIZAR " + this.nomeClasse.toUpperCase();
        conteudo += "\n" + this.texto + "\n\n";
        conteudo += "\n\nINSIRA: \nID DO PRESENTE ➡ CONFIRMAR/CANCELAR ESCOLHA:" + "\n" + "DIGITE 0                 ➡ PARA VOLTAR";
        String result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInserido = Util.stringToInt(result);
            if (idInserido != 0) {
                try {
                    boolean existe = this.dao.find(this.idClasse, idInserido);
                    if (existe) {
                        Presente presente = (Presente) this.dao.getItemByID(idClasse, idInserido);
                        if (presente != null) {
                            //checa se o presente foi escolhido já
                            if (presente.getEscolhido()) {
                                //checa se a pessoa que está logada agora é a pessoa que escolheu o presente
                                if (this.dao.getUserLogado().getIdPessoa() == presente.getIdPessoa()) {
                                    //altera a escolha
                                    presente.escolher(this.user.getPessoa());
                                    if (presente.getEscolhido()) {
                                        Util.mostrarMSG("Presente escolhido com sucesso!");
                                    } else {
                                        Util.mostrarMSG("Escolha do presente cancelada!");
                                    }
                                } else {
                                    Util.mostrarMSG("Você não é o presenteador!");
                                }
                            } else {
                                presente.escolher(this.user.getPessoa());
                                if (presente.getEscolhido()) {
                                    Util.mostrarMSG("Presente escolhido com sucesso!");
                                } else {
                                    Util.mostrarMSG("Escolha do presente cancelada!");
                                }
                            }

                        } else {
                            Util.mostrarMSG("Presente não encontrado!");
                        }

                        this.exibir(this.dao, this.idClasse);
                        
                    } else {
                        Util.mostrarErro("Elemento de id " + result + " não encontrado!");
                        this.exibir(this.dao, this.idClasse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Util.criarMenuCRUD(this.dao, 1);
            }

        } else {
            Util.criarMenuCRUD(this.dao, 1);
        }

    }

}
