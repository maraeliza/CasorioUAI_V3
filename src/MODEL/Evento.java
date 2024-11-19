/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import CONTROLLER.DAO;
import VIEW.Util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Evento implements InterfaceClasse {

    // Atributos da classe
    private int id;

    private String nome;
    private Cerimonial cerimonial;
    private int idIgreja;
    private int idCerimonial;
    private int idCartorio;
    private Igreja igreja;
    private Cartorio cartorio;
    private int idNoiva;
    private Pessoa noiva;
    private int idNoivo;
    private Pessoa noivo;

    private LocalDate data;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;
    
    private DAO dao;


    public String getEndereco(){
        if (this.getIgreja()!=null) {
            return this.getIgreja().getEndereco();
        }else{
            if (this.getCartorio()!=null) {
                return this.getCartorio().getEndereco();
            }
        }
        return "N/A";
    }

    public void setCerimonial(Cerimonial cerimonial) {
        if (this.cerimonial != null) {
            this.cerimonial.setEventoVinculado(false);
        }
        if (cerimonial != null) {
            this.cerimonial = cerimonial;
            this.cerimonial.setEventoVinculado(true);
            this.idCerimonial = this.cerimonial.getId(); 
            this.dataModificacao = LocalDate.now();
        }
    }

    public Igreja getIgreja() {
        return this.igreja;
    }

    public void setCartorio(Cartorio cartorio) {
        if (this.cartorio != null) {
            this.cartorio.setEventoVinculado(false);
        }
        if (cartorio != null) {
            this.cartorio = cartorio;
            this.cartorio.setEventoVinculado(true);
            this.idCartorio = this.cartorio.getId();
            this.dataModificacao = LocalDate.now();
        }
    }
    
    public void setIgreja(Igreja igreja) {
        if (this.igreja != null) {
            this.igreja.setEventoVinculado(false);
        }
        if (igreja != null) {
            this.igreja = igreja;
            this.igreja.setEventoVinculado(true);
            this.idIgreja = this.igreja.getId();
            this.dataModificacao = LocalDate.now();
        }
    }
    
    public Cartorio getCartorio() {
        return this.cartorio;
    }

    public Pessoa getNoiva() {
        return this.noiva;
    }

    public void setNoiva(Pessoa noiva) {
        if (noiva != null
                && noiva.getTipo().toUpperCase().equals("NOIVA")) {
            this.noiva = noiva;
            this.idNoiva = this.noiva.getId();
            this.dataModificacao = LocalDate.now();
        }

    }

    public Pessoa getNoivo() {
        return this.noivo;
    }

    public void setNoivo(Pessoa noivo) {
        if (noivo != null
                && noivo.getTipo().toUpperCase().equals("NOIVO")) {
            this.noivo = noivo;
            this.idNoivo = this.noivo.getId();
            this.dataModificacao = LocalDate.now();
        }
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
        this.dataModificacao = LocalDate.now();
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }

    public static String[] getCampos() {
        String[] campos = new String[11]; // Aumentando o tamanho do array para 11
        campos[0] = "ID: ";
        campos[1] = "Data: ";
        campos[2] = "ID da Igreja: ";
        campos[3] = "ID do Cartório: ";
        campos[4] = "ID do Cerimonial: ";
        campos[5] = "Nome: ";
        return campos;
    }


    public boolean criar(DAO dao, List<Object> vetor) {
        boolean alterado = false;
        if (dao != null) {
            this.dao = dao;
    
            // Busca noiva e noivo no DAO
            Pessoa noiva = dao.getNoivos(1);
            Pessoa noivo = dao.getNoivos(0);
    
            // Verifica se idIgreja é válido e preenche o campo se possível
            int idIgreja = Util.stringToInt((String) vetor.get(1));
            Igreja igreja = idIgreja != 0 ? (Igreja) dao.getItemByID(7, idIgreja) : null;
    
            // Verifica se idCartorio é válido e preenche o campo se possível
            int idCartorio = Util.stringToInt((String) vetor.get(2));
            Cartorio cartorio = idCartorio != 0 ? (Cartorio) dao.getItemByID(8, idCartorio) : null;
    
            // Verifica se idCerimonial é válido e preenche o campo se possível
            int idCerimonial = Util.stringToInt((String) vetor.get(3));
            Cerimonial cerimonial = idCerimonial != 0 ? (Cerimonial) dao.getItemByID(6, idCerimonial) : null;
    
            // Checa a existência dos itens, preenchendo apenas se existirem
            if (noiva != null) this.setNoiva(noiva);
            if (noivo != null) this.setNoivo(noivo);
            if (igreja != null) this.setIgreja(igreja);
            if (cartorio != null) this.setCartorio(cartorio);
            if (cerimonial != null) this.setCerimonial(cerimonial);
    
            // Converte a data caso fornecida no vetor
            if (vetor.get(0) != null && vetor.get(0) instanceof String) {
                String data = (String) vetor.get(0);
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
                try {
                    this.data = LocalDate.parse(data, formato);
                    alterado = true;
                } catch (DateTimeParseException e) {
                    // Log ou tratamento de erro
                }
            }
    
            // Preenche o nome caso esteja presente no vetor
            if (vetor.get(4) != null && vetor.get(4) instanceof String) {
                this.nome = (String) vetor.get(4);
            }
    
            // Atualiza os dados de criação e modificação se houver alteração
            if (alterado) {
                this.id = ++Evento.total;
                this.dataCriacao = LocalDate.now();
                this.dataModificacao = null;
            }
        }
    
        return alterado;
    }
    



    @Override
    public boolean deletar() {
        if (this.getId() > 1) {
            return true;
        } else {
            Util.mostrarErro(this.getNome() + " não pode ser deletado, pois é o evento principal!");
            return false;
        }

    }


    public String ler() {
        StringBuilder resultado = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (this.nome != null && this.nome.length() > 0) {
            resultado.append("\n");
            resultado.append("\n\nID: ").append(this.id);
            resultado.append("  EVENTO: ").append(this.nome != null ? this.nome.toUpperCase() : "N/A");
            // Formatação da data do evento
            if (this.data != null) {
                resultado.append("\nData do Evento: ").append(this.data.format(formatter));
            }
            if (this.igreja != null) {
                resultado.append("\n\nIgreja: ").append(this.igreja != null ? this.igreja.getNome() : "N/A");
                resultado.append("\nEndereço da Igreja: ").append(this.igreja != null ? this.igreja.getEndereco() : "N/A");
            }
            if (this.cartorio != null) {
                resultado.append("\n\nCartório: ").append(this.cartorio != null ? this.cartorio.getNome() : "N/A");
                resultado.append("\nEndereço do Cartório: ").append(this.cartorio != null ? this.cartorio.getEndereco() : "N/A");
            }
            if (this.cerimonial != null) {
                resultado.append("\n\nCerimonial: ").append(this.cerimonial != null ? this.cerimonial.getNome() : "N/A");
            }
            if (this.noiva != null && (this.igreja != null && this.cartorio != null )) {
                if (this.noivo != null) {
                    resultado.append("\nNoiva: ").append(this.noiva != null ? this.noiva.getNome() : "N/A");
                    resultado.append("\nNoivo: ").append(this.noivo != null ? this.noivo.getNome() : "N/A");
                }

            }
        
            // Formatação da data de modificação
            if (this.dataModificacao != null) {
                resultado.append("\nData da Última Modificação: ").append(this.dataModificacao.format(formatter));
            }
        }

        return resultado.toString();
    }

    public void update(List<Object> vetor) {
        
        boolean alterou = false;
        // Atualiza a data de nascimento (recebe como String e converte para LocalDate)
        if (vetor.get(1) != null && vetor.get(1) instanceof String) {
            String dataStr = (String) vetor.get(1);
            if (dataStr.length() > 0) {
                try {
                    // Define o formato da data esperado (por exemplo, "dd/MM/yyyy")
                    this.data = Util.stringToDate(dataStr);
                    alterou = true;
                } catch (DateTimeParseException e) {
                 }
            }
        }
        if (vetor.get(5) != null && vetor.get(5) instanceof String) {
            String nome = (String) vetor.get(5);
            if (nome.length() > 0) {
                this.nome = nome;
                alterou = true;

            }

        }

        if (vetor.get(2) != null && vetor.get(2) instanceof String) {
            int idIgreja = Util.stringToInt((String) vetor.get(2));
            if (idIgreja != 0) {
                Igreja igreja = (Igreja) this.dao.getItemByID(7, idIgreja); // 7 representa a classe Igreja
                this.setIgreja(igreja);
                alterou = true;
            }
        }

        if (vetor.get(3) != null && vetor.get(3) instanceof String && !vetor.get(3).equals("0")) {
            int idCartorio = Util.stringToInt((String) vetor.get(3));
            if (idCartorio != 0) {
                Cartorio cartorio = (Cartorio) this.dao.getItemByID(8, idCartorio);
                this.setCartorio(cartorio);
                alterou = true;
            }
        }

        if (vetor.get(4) != null && vetor.get(4) instanceof String && !vetor.get(4).equals("0")) {
            int idCerimonial = Util.stringToInt((String) vetor.get(4));
            if (idCerimonial != 0) {
                Cerimonial cerimonial = (Cerimonial) this.dao.getItemByID(6, idCerimonial);
                this.setCerimonial(cerimonial);
                alterou = true;
            }
        }

        if (alterou) {
            this.atualizarDataModificacao();
        }
    }
    // Método para atualizar a data de modificação
    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static int total;

    public int getIdIgreja() {
        return idIgreja;
    }

    public void setIdIgreja(int idIgreja) {
        this.idIgreja = idIgreja;
    }

    public int getIdCerimonial() {
        return idCerimonial;
    }

    public void setIdCerimonial(int idCerimonial) {
        this.idCerimonial = idCerimonial;
    }

    public int getIdCartorio() {
        return idCartorio;
    }

    public void setIdCartorio(int idCartorio) {
        this.idCartorio = idCartorio;
    }

    public int getIdNoiva() {
        return idNoiva;
    }

    public void setIdNoiva(int idNoiva) {
        this.idNoiva = idNoiva;
    }

    public int getIdNoivo() {
        return idNoivo;
    }

    public void setIdNoivo(int idNoivo) {
        this.idNoivo = idNoivo;
    }

    // Getters e Setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        Evento.total = total;
    }

    public Cerimonial getCerimonial() {
        return this.cerimonial;
    }
}
