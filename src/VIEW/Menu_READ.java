/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class Menu_READ {

    private String nomeClasse;
    private String texto;
    private String vetor[];

    private int nColetados;
    private DAO dao;
    private int idClasse;

    public void exibir(DAO dao, int idClasse) {
        this.idClasse = idClasse;
        this.dao = dao;
        try {

            int n = dao.getTotalClasse(idClasse);
            if (n > 0) {
                this.nomeClasse = this.dao.getNameClasseById(idClasse);

                try {
                    String texto = "";
                    
                    texto = dao.getTexto(idClasse);

                    int res = JOptionPane.showConfirmDialog(null, texto, "RELATÓRIO DE " + this.nomeClasse.toUpperCase().replace("_", " "), JOptionPane.OK_CANCEL_OPTION);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Util.mostrarMSG("Nenhum item encontrado ainda!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exibir(DAO dao, int idClasse, boolean relatorioPagamento) {
        this.idClasse = idClasse;
        this.dao = dao;
        try {

            int n = dao.getTotalClasse(idClasse);
            if (n > 0) {
                this.nomeClasse = this.dao.getNameClasseById(idClasse);

                try {
                    String texto = this.dao.getPagamentosNoivos();
                    int res = JOptionPane.showConfirmDialog(null, texto, "RELATÓRIO DE " + this.nomeClasse.toUpperCase(), JOptionPane.OK_CANCEL_OPTION);

                    System.out.println("TEXTO: " + texto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Util.mostrarMSG("Nenhum item encontrado ainda!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void montarPainel() {

    }

    public boolean add(String p) {
        for (int i = 0; i < this.vetor.length; i++) {
            if (this.vetor[i] == null) {
                this.vetor[i] = p;
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
