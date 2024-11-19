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
public class Menu_DEL {

    private String nomeClasse;
    private String texto;
    private String vetor[];
    private String valores[];
    private int nColetados;
    private DAO dao;
    private int idClasse;

    public void exibir(DAO dao, int idClasse) {
        this.dao = dao;
        this.idClasse = idClasse;
        this.nomeClasse = this.dao.getNameClasseById(idClasse);;

        try {
            String texto = "";
            if (idClasse == 2) {
                texto = this.dao.getNomesPessoasSemUsers();
            } else {
                texto = this.dao.getTexto(idClasse);
            }
            texto += "\n\nDigite o ID para excluir: ";
            String res = JOptionPane.showInputDialog(null, texto, "0");
            if (res != null && res.length() > 0) {
                int id = Util.stringToInt(res);
                if (id != 0) {
                    boolean sucess = false;
                    
                    sucess = this.dao.delItemByID(this.idClasse, id);

                    if (sucess) {
                        JOptionPane.showMessageDialog(null, "Elemento " + id + " excluído com sucesso!", "DELETADO", JOptionPane.INFORMATION_MESSAGE);
                        this.exibir(this.dao, this.idClasse);

                    } else {
                        Util.mostrarErro("Não foi possível fazer a exclusão");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
