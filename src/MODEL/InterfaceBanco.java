/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MODEL;

import java.util.List;

import CONTROLLER.DAO;

/**
 *
 * @author Mara
 */
public interface InterfaceBanco {
    List<Object> getValoresSQL();
    List<String> getCamposSQL();
    int getId();
    String getNomeTabela();
    boolean criarObjetoDoBanco(DAO dao, List<Object> vetor);
   
}