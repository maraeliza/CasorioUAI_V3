/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import MODEL.Usuario;
import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class Menu_CRUD {

    private String texto;
    private String op;
    private int o;
    private String tipoClasse;
    private boolean logou;

    private int tipoUsuario;
    private Usuario user;
    private DAO dao;
    private int idClasse;
    private int nOps;

    private void definirTexto(String classNome) {
        this.texto = "\n\nMENU DE " + classNome;
        this.texto += "\n\nEscolha a opção a seguir ";

        // Definindo um vetor de opções com tamanho máximo suficiente
        String[] opcoes = new String[10];
        int contador = 0; // Contador para armazenar a posição no vetor

        // Adiciona opções de acordo com o tipo de usuário e idClasse
        if (this.user != null && this.user.getTipo() == 1) { // Usuário administrador
            switch (this.idClasse) {
                case 1 -> {
                    opcoes[contador++] = "Adicionar novo";
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Editar " + classNome.toLowerCase();
                    opcoes[contador++] = "Deletar";
                    opcoes[contador++] = "Escolher Presente";
                    opcoes[contador++] = "Comprar Presente";
                    opcoes[contador++] = "Voltar";
                }
                case 11 -> {
                    opcoes[contador++] = "Lançar Pagamento";
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Editar " + classNome.toLowerCase();
                    opcoes[contador++] = "Deletar";
                    opcoes[contador++] = "Agendar Pagamento";
                    opcoes[contador++] = "Voltar";
                }
                case 12 -> {
                    opcoes[contador++] = "Adicionar novo";
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Voltar";
                }
                case 13 -> {
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Voltar";
                }
                default -> {
                    opcoes[contador++] = "Adicionar novo";
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Editar " + classNome.toLowerCase();
                    opcoes[contador++] = "Deletar";
                    opcoes[contador++] = "Voltar";
                }
            }
        } else { // Usuário não administrador
            switch (this.idClasse) {
                case 0 -> {
                    opcoes[contador++] = "Adicionar novo";
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Voltar";
                }
                case 1 -> {
                    opcoes[contador++] = "Adicionar novo";
                    opcoes[contador++] = "Ver todos";
                    opcoes[contador++] = "Voltar";
                }

                default -> {
                    break;
                }
            }
        }
        this.nOps = contador;
        // Adiciona o número e texto de cada opção ao menu
        for (int i = 0; i < contador; i++) {
            this.texto += "\n" + (i + 1) + ". " + opcoes[i];
        }

        this.texto += "\n\nDigite aqui o número da sua opção: ";
    }

    public void exibir(DAO dao, int idClasse) {
        this.dao = dao;
        this.idClasse = idClasse;
        this.user = this.dao.getUserLogado();

        if (this.user != null) {
            this.tipoUsuario = user.getTipo();
        } else {
            this.tipoUsuario = 0;
        }

        do {

            this.tipoClasse = this.dao.getNameClasseById(idClasse);
            this.definirTexto(this.tipoClasse);

            this.op = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro ", JOptionPane.QUESTION_MESSAGE);
            this.lidarEscolha();

        } while (this.o != 5 && this.o != 0);
    }

    private void lidarEscolha() {
        if (this.op == null || Util.stringToInt(this.op) >= this.nOps) {
            MenuInicial menu = new MenuInicial();
            menu.exibir(this.dao, this.logou, this.user);
        } else {
            this.o = Util.stringToInt(this.op);
            if (this.o < 0) {
                MenuInicial menu = new MenuInicial();
                menu.exibir(this.dao, this.logou, this.user);
            } else {
                switch (this.o) {

                    case 1 -> {
                        if (this.idClasse == 11 && this.user != null && this.user.getTipo() == 1) {
                            MenuFazerPagamento menu = new MenuFazerPagamento();
                            menu.exibir(this.dao, this.idClasse);
                            break;

                        } else if (this.idClasse != 13) {
                            Menu_CREATE menuAdd = new Menu_CREATE();
                            menuAdd.exibir(this.dao, this.idClasse, this.user);

                        } else if (this.user != null && this.user.getTipo() == 1) {
                            Menu_READ menuVer = new Menu_READ();
                            menuVer.exibir(this.dao, this.idClasse);
                        }

                        break;
                    }
                    case 2 -> {
                        Menu_READ menuVer = new Menu_READ();
                        menuVer.exibir(this.dao, this.idClasse);
                        break;
                    }
                    case 3 -> {
                        if (this.idClasse != 12
                                && this.idClasse != 13
                                && this.user != null
                                && this.user.getTipo() == 1) {
                            Menu_UPDATE menuUp = new Menu_UPDATE();
                            menuUp.exibir(this.dao, this.idClasse);
                        } else {
                            MenuInicial menu = new MenuInicial();
                            menu.exibir(this.dao, this.logou, this.user);
                        }

                        break;
                    }
                    case 4 -> {
                        if (this.idClasse != 12
                                && this.idClasse != 13
                                && this.user != null
                                && this.user.getTipo() == 1) {
                            Menu_DEL menuDel = new Menu_DEL();
                            menuDel.exibir(this.dao, this.idClasse);
                        } else {
                            MenuInicial menu = new MenuInicial();
                            menu.exibir(this.dao, this.logou, this.user);
                        }

                        break;
                    }
                    case 5 -> {

                        if (this.idClasse == 1 && this.user != null) {

                            MenuEscolherPresente menu = new MenuEscolherPresente();
                            menu.exibir(this.dao, this.idClasse);
                            break;

                        } else if (this.idClasse == 11 && this.user != null) {
                            MenuAgendarPagamento menu = new MenuAgendarPagamento();
                            menu.exibir(this.dao, this.idClasse, this.user);
                            break;

                        } else {
                            MenuInicial menu = new MenuInicial();
                            menu.exibir(this.dao, this.logou, this.user);
                        }

                        break;
                    }
                    case 6 -> {
                        if (this.idClasse == 1 && this.user != null) {

                            MenuComprarPresente menu = new MenuComprarPresente();
                            menu.exibir(this.dao, this.idClasse);
                            break;

                        } else {
                            MenuInicial menu = new MenuInicial();
                            menu.exibir(this.dao, this.logou, this.user);
                        }
                        break;
                    }

                    default -> {
                        MenuInicial menu = new MenuInicial();
                        menu.exibir(this.dao, this.logou, this.user);
                        break;
                    }

                }
            }

        }

    }
}
