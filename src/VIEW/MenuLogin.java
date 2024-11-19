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
public class MenuLogin {

    public void exibir(DAO dao) {
        String texto = "\nLOGAR\n";
        texto += "\nINSIRA SEU LOGIN DE ACESSO: ";
        String user = JOptionPane.showInputDialog(null, texto, "admin");

        if (user != null && user.length() > 1) {
            texto = "INSIRA SUA SENHA DE ACESSO: ";
            String senha = JOptionPane.showInputDialog(null, texto, "1234");
            if (senha != null && senha.length() > 3) {
                dao.logar(user, senha);
            } else {
                Util.mostrarErro("Digite sua senha!");
            }
        } else {
            Util.mostrarErro("Digite seu login!");

        }
    }

}
