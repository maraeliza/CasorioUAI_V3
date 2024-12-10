/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class Menu_UPDATE {

    private String nomeClasse;
    private String texto;
    private String vetor[];
    private List<Object> valores;
    private Class classe;
    private int nColetados; 
    private DAO dao;
    private int idClasse;

    public void exibir(DAO dao, int idClasse) {
        
      
        this.dao = dao;
        this.idClasse = idClasse;
        this.vetor = new String[10];
        Class<?> classe = this.dao.getClasseByID(this.idClasse);
        this.classe = classe;
        this.nomeClasse = this.dao.getNameClasseById(idClasse);
        try {
            this.getTexto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTexto() {
        this.texto = "";
        this.cleanVetor();
        this.texto = this.dao.getTexto(this.idClasse);
       
        try {
            java.lang.reflect.Method metodo = this.classe.getMethod("getCampos");
            this.vetor = (String[]) metodo.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        montarPainel(texto);

        return "";
    }

    public void montarPainel(String objetos) {

        String conteudo = "\nATUALIZAR CAMPO DE " + this.nomeClasse.toUpperCase();

        this.valores = new ArrayList<>();
        conteudo = "\nATUALIZAR " + this.nomeClasse.toUpperCase();
        conteudo += "\n" + objetos + "\n";
        conteudo += "\n\nINSIRA: \nID DO ITEM ➡ PARA EDITÁ-LO:"+"\n"+"DIGITE 0      ➡ PARA VOLTAR";
        String result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
        if (result != null) {
            int idInserido = Util.stringToInt(result);
            if (idInserido != 0) {
                this.valores.add(result);
                try {
                    boolean existe = this.dao.isInList(this.idClasse, idInserido);
                    if (existe) {
                        this.nColetados = 0;
                        for (int i = 1; i < this.vetor.length; i++) {
                            if (this.vetor[i] != null && result != null) {
                                conteudo = "\nATUALIZAR " + this.nomeClasse.toUpperCase();
                                conteudo += "\n" + this.montarOpcoes(this.nomeClasse, i);
                                conteudo += "\n\nINSIRA " + this.vetor[i].toUpperCase();
                                result = JOptionPane.showInputDialog(null, conteudo, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);
                                this.nColetados++;
                                this.valores.add(result);
                            }
                        }
                        this.dao.atualizar(this.idClasse, this.valores);
                        this.exibir(this.dao, this.idClasse);
                    } else {
                        Util.mostrarErro("Elemento de id " + result + " não encontrado!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            Util.mostrarErro("Atualização cancelada!");
        }

    }

    public boolean add(String atributo) {
        for (int i = 0; i < this.vetor.length; i++) {
            if (this.vetor[i] == null) {
                this.vetor[i] = atributo;
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
    public String montarOpcoes(String nomeClasse, int i) {
        nomeClasse = nomeClasse.toUpperCase();
        String conteudo = "";
        switch (nomeClasse) {
            case "USUÁRIOS" -> {
                switch (i) {
                    case 1 -> {
                        if (this.dao.getTotalClasse(2) > 1) {
                            conteudo += "\nID E NOME DAS PESSOAS:";
                        } else {
                            conteudo += "\nID E NOME DA PESSOA:";
                        }
                        conteudo += this.dao.getNomesPessoasParaCriarUsers();
                        return conteudo;
                    }
                    default -> {
                        break;
                    }
                }
            }
            case "DESPESAS" -> {
                switch (i) {
                    case 1 -> {
                        if (this.dao.getTotalClasse(4) > 1) {
                            conteudo += "\nID E NOME DOS FORNECEDORES::";
                        } else {
                            conteudo += "\nID E NOME DO FORNECEDOR:";
                        }
                        conteudo += this.dao.getNomes(4);
                        return conteudo;
                    }
                    
                    default -> {
                        break;
                    }
                }
            }
            case "CONVIDADO INDIVIDUAL" -> {
                switch (i) {
                    case 1 -> {
                        if (this.dao.getTotalClasse(2) > 1) {
                            conteudo += "\nID E NOME DAS PESSOAS:";
                        } else {
                            conteudo += "\nID E NOME DA PESSOA:";
                        }
                        conteudo += this.dao.getNomesPessoasSemConvidado();
                        return conteudo;
                    }
                    case 2 -> {
                        if (this.dao.getTotalClasse(10) > 1) {
                            conteudo += "\nID E NOME DAS FAMÍLIAS:";
                        } else {
                            conteudo += "\nID E NOME DA FAMÍLIA:";
                        }
                        conteudo += this.dao.getNomes(10);
                        return conteudo;
                    }
                    default -> {
                        break;
                    }
                }
            }
            case "CERIMONIAL" -> {
                switch (i) {
                    case 1 -> {
                        if (this.dao.getTotalClasse(2) > 1) {
                            conteudo += "\nID E NOME DAS PESSOAS:";
                        } else {
                            conteudo += "\nID E NOME DA PESSOA:";
                        }
                        conteudo += this.dao.getCerimoniaisIdNomeDisponiveis();
                        return conteudo;
                    }
                    default -> {
                        break;
                    }
                }
            }
            case "PAGAMENTOS" -> {
                switch (i) {
                    
                    case 1 -> {
                        if (this.dao.getTotalClasse(4) > 1) {
                            conteudo += "\nID E NOME DOS FORNECEDORES:";
                        } else {
                            conteudo += "\nID E NOME DO FORNECEDOR:";
                        }

                        conteudo += this.dao.getNomes(4);
                        return conteudo;

                    }

                    default -> {
                        break;
                    }
                }
            }

            case "EVENTO" -> {
                switch (i) {
                    case 2 -> {
                        if (this.dao.getTotalClasse(7) > 1) {
                            conteudo += "\nID E NOME DAS IGREJAS:";
                        } else {
                            conteudo += "\nID E NOME DA IGREJA:";
                        }
                        conteudo += this.dao.getNomes(7);
                    }
                    case 3 -> {
                        if (this.dao.getTotalClasse(8) > 1) {
                            conteudo += "\nID E NOME DOS CARTÓRIOS:";
                        } else {
                            conteudo += "\nID E NOME DO CARTÓRIO:";
                        }

                        conteudo += this.dao.getNomes(8);
                    }
                    case 4 -> {
                        if (this.dao.getTotalClasse(6) > 1) {
                            conteudo += "\nID E NOME DOS CERIMONIAIS:";
                        } else {
                            conteudo += "\nID E NOME DO CERIMONIAL:";
                        }
                        conteudo += this.dao.getNomes(6);
                    }
                    default -> {
                        break;
                    }
                }
            }

            default -> {
                break;
            }

        }

        return conteudo;

    }

}
