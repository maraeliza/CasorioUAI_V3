/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author Mara
 */
public class Util {

    String mensagem;

    public static void mostrarErro(String erroMSG) {
        JOptionPane.showMessageDialog(null, erroMSG, "ERRO!", JOptionPane.ERROR_MESSAGE);

    }

    public static void mostrarMSG(String MSG) {
        JOptionPane.showMessageDialog(null, MSG, "MENSAGEM", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Double stringToDouble(String str) {
        try {
            if (str != null
                    && str.length() > 0) {
                Double n = Double.parseDouble(str);
                return n;
            }

        } catch (NumberFormatException e) {
            return 0.0;
        }
        return 0.0;
    }

    public static int stringToInt(String str) {
        try {
            if (str != null
                    && str.length() > 0) {
                int n = Integer.parseInt(str);
                return n;
            }

        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    public static String intToString(int n) {
        try {
            String str = String.valueOf(n);
            return str;
        } catch (NumberFormatException e) {
            mostrarErro("Valor invalido!");
            return "";
        }

    }

    public static String dateToString(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Converte o LocalDate para String usando o formatter
        String dataFormatada = data.format(formatter);
        return dataFormatada;
    }

    public static LocalDate stringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            Util.mostrarErro("Data inserida no formato incorreto");
            return null;
        }
    }

    public static void criarMenuCRUD(DAO dao, int idClasse) {
        Menu_CRUD menu = new Menu_CRUD();
        menu.exibir(dao, idClasse);
    }

}
