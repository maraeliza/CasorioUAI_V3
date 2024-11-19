/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Mara
 */
public interface InterfaceClasse {

    int getId();

    void setId(int id);

    String ler();

    String getNome();

    boolean deletar();

    boolean criar(DAO dao, List<Object> vetor);

    void update(List<Object> vetor);

    LocalDate getDataCriacao();

    LocalDate getDataModificacao();

    void atualizarDataModificacao();

}
