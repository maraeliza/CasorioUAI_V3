/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import MODEL.InterfaceClasse;
import MODEL.Usuario;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class MenuCalendario {

    private String texto;
    private String op;
    private boolean logou;
    private String opcoes[];
    private Usuario userLogado;
    private String listaNomeClasses[] = new String[20];
    private LocalDate dataHoje;
    private int nOps;
    private DAO dao;

    public MenuCalendario() {

        setLista();

    }

    private void setLista() {
        String listaClasses[] = new String[20];
        listaClasses[0] = "EVENTOS PARA HOJE";
        listaClasses[1] = "PESQUISAR EVENTO POR DATA";
        this.listaNomeClasses = listaClasses;
    }

    private String[] getLista() {
        return this.listaNomeClasses;
    }

    private void definirTexto() {
        this.texto = "";
        this.texto += "\n\nCALENDÁRIO";
        this.texto += "\n\nEscolha a opção a seguir ";

        this.texto += this.definirOpcoes();

        this.texto += "\n\nDigite aqui o número da sua opção: ";

    }

    private String definirOpcoes() {

        String Opcoes = "";
        this.nOps = 1;

        for (int n = 0; n < this.listaNomeClasses.length; n++) {
            if (this.listaNomeClasses[n] != null) {
                Opcoes += "\n" + Util.intToString(this.nOps) + ". " + this.listaNomeClasses[n];
                this.nOps++;
            }

        }

        Opcoes += "\n" + Util.intToString(this.nOps) + ". SAIR";
        return Opcoes;
    }

    public void exibir(DAO dao, Usuario user, boolean logou) {
        this.dataHoje = LocalDate.now();
        this.userLogado = user;
        this.logou = logou;
        this.dao = dao;
        int o = -1;
        do {
            this.definirTexto();

            this.op = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
            if (this.op != null) {
                o = Util.stringToInt(this.op);
                this.lidarEscolha(o);
            } else {
                TelaInicial menu = new TelaInicial();
                op = menu.exibir(this.dao);
            }
        } while (o != 0 && this.op != null);

    }

    private void lidarEscolha(int o) {

        switch (o) {
            case 1 -> {
                this.exibirDados(5);
                break;
            }
            case 2 -> {

                this.exibirDados(5, this.perguntarData());
                break;
            }
           
           default -> {
               
                break;
            }
        }
        
        if (o >= this.nOps) {
            MenuInicial menu = new MenuInicial();
            menu.exibir(this.dao, this.logou, this.userLogado);
        }

    }

    public LocalDate perguntarData() {
        this.texto = "\nINSIRA A DATA QUE DESEJA CONSULTAR: ";
        this.op = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (this.op != null) {
            LocalDate dataInserida = Util.stringToDate(this.op);
            return dataInserida;
        } else {
            return null;
        }
    }

    public void exibirDados(int idClasse) {
        this.texto = "";
        switch (idClasse) {
            case 5 -> {
                List<InterfaceClasse> eventos = this.dao.getEventosByData(this.dataHoje);
                this.texto += this.dao.getTexto(5,eventos);
                break;
            }
            case 12 -> {

                List<InterfaceClasse> parcelas =this.dao.getParcelasByDataVencimento(this.dataHoje);
                this.texto += this.dao.getTexto(12, parcelas);
                break;
            }
        }

        JOptionPane.showConfirmDialog(null, this.texto, "RELATÓRIO", JOptionPane.OK_CANCEL_OPTION);

    }

    public void exibirDados(int idClasse, LocalDate dataConsulta) {
        if (dataConsulta != null) {
            this.texto = "";
            switch (idClasse) {
                case 5 -> {
                    List<InterfaceClasse> eventos = this.dao.getEventosByData(dataConsulta);
                    this.texto += this.dao.getTexto(5, eventos);
                    break;
                }
                case 12 -> {
                    List<InterfaceClasse> parcelas =this.dao.getParcelasByDataVencimento(dataConsulta);
                    this.texto += this.dao.getTexto(12, parcelas);
                    break;
                }
            }

            JOptionPane.showConfirmDialog(null, this.texto, "RELATÓRIO", JOptionPane.OK_CANCEL_OPTION);
        }

    }

}
