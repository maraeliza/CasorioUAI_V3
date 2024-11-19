/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import javax.swing.JOptionPane;
/**
 *
 * @author CAUPT - ALUNOS
 */
public class TelaInicial {
    private DAO dao;
    
    public String exibir(DAO dao) {
        this.dao = dao;
        int o = -1;  // Valor inicial inválido para garantir que o loop execute pelo menos uma vez
        do {
            String texto = "\n🏠          ❤   CASAMENTO   ❤              🏠\n";
            texto += dao.getTextoNoivos();
            texto += "\n\n1. LOGAR";
            texto += "\n2. ENTRAR SEM LOGIN";
            texto += "\n3. FECHAR";
            texto += "\n\nINSIRA SUA ESCOLHA: ";
            String result = JOptionPane.showInputDialog(null, texto, "1");
      
            if (result == null) {
                Util.mostrarMSG("Obrigado por usar nosso programa ❤! \nAté mais!");
                System.exit(0);

            } else {
                int op = Util.stringToInt(result);

                if (op == 1) {
                    MenuLogin menu = new MenuLogin();
                    menu.exibir(this.dao);

                } else if (op == 2) {
                    MenuInicial menu = new MenuInicial();
                    menu.exibir(this.dao, false, null);
                }
                if (op == 3) {
                    Util.mostrarMSG("Obrigado por usar nosso programa :D! \nAté mais!");
                    System.exit(0);

                } else {
                    Util.mostrarErro("Digite uma opção válida!");
                }
                return result;
            }

        } while (o != 0 && o != 3 && o != -1); // O loop continuará até o usuário escolher "Sair" ou "0"
        return null;

    }
}
