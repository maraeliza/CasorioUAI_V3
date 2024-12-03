package main;

import CONTROLLER.DAO;
import VIEW.TelaInicial;

public class TrabalhoPOO {

    public static void main(String[] args) {
        try {
            DAO dao = new DAO("root", "senhadobanco", "bancoCasorioUAI");
            dao.addInfosIniciais();
            TelaInicial menu = new TelaInicial();
            menu.exibir(dao);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

//JoséMaria15122024iyai

