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
import java.util.ArrayList;
import java.util.List;

public class Evento implements InterfaceClasse, InterfaceBanco {

    private int id;

    private String nome;

    private int idIgreja;
    private int idCerimonial;
    private int idCartorio;
    private int idNoiva;
    private int idNoivo;

    private Igreja igreja;
    private Cerimonial cerimonial;
    private Cartorio cartorio;

    private Pessoa noiva;
    private Pessoa noivo;

    private LocalDate data;
    private LocalDate dataCriacao;
    private LocalDate dataModificacao;

    private DAO dao;


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

    @Override
    public List<String> getCamposSQL() {
        List<String> campos = new ArrayList<>();
        campos.add("id");
        campos.add("nome");
        campos.add("idCerimonial");
        campos.add("idIgreja");
        campos.add("idCartorio");
        campos.add("idNoiva");
        campos.add("idNoivo");
        campos.add("data");
        campos.add("dataCriacao");
        campos.add("dataModificacao");
        return campos;
    }

    @Override
    public String getNomeTabela() {
        return "tb_evento";
    }

    public static String getNomeTabelaByClass() {
        return "tb_evento";
    }

    @Override
    public List<Object> getValoresSQL() {
        List<Object> valores = new ArrayList<>();
        valores.add(this.id);
        valores.add(this.nome);
        valores.add(this.idCerimonial);
        valores.add(this.idIgreja);
        valores.add(this.idCartorio);
        valores.add(this.idNoiva);
        valores.add(this.idNoivo);
        valores.add(this.data);
        valores.add(this.dataCriacao);
        valores.add(this.dataModificacao);
        return valores;
    }


    @Override
    public boolean criarObjetoDoBanco(DAO dao, List<Object> vetor) {
        boolean alterado = vetor.get(0) != null && vetor.get(1) != null;
        if (!alterado) {
            return alterado;
        } else {
            try {
                this.id = (int) vetor.get(0);
                this.nome = (String) vetor.get(1);
                if (vetor.get(2) != null && (int) vetor.get(2) != 0) {
                    this.idCerimonial = (int) vetor.get(2);
                    this.cerimonial = (Cerimonial) dao.getBanco().getItemByIDBanco(6, this.idCerimonial);

                }

                if (vetor.get(3) != null && (int) vetor.get(3) != 0) {
                    this.idIgreja = (int) vetor.get(3);
                    this.igreja = (Igreja) dao.getItemByID(7, this.idIgreja);

                }

                if (vetor.get(4) != null && (int) vetor.get(4) != 0) {
                    this.idCartorio = (int) vetor.get(4);
                    this.cartorio = (Cartorio) dao.getItemByID(8, this.idCartorio);

                }

                if (vetor.get(5) != null && (int) vetor.get(5) != 0) {
                    this.idNoiva = (int) vetor.get(5);
                    this.noiva = (Pessoa) dao.getItemByID(2, this.idNoiva);

                }

                if (vetor.get(6) != null && (int) vetor.get(6) != 0) {
                    this.idNoivo = (int) vetor.get(6);
                    this.noivo = (Pessoa) dao.getItemByID(2, this.idNoivo);

                }
                this.data = vetor.get(7) != null ? (LocalDate) vetor.get(7) : null;
                this.dataCriacao = vetor.get(8) != null ? (LocalDate) vetor.get(8) : null;
                this.dataModificacao = vetor.get(9) != null ? (LocalDate) vetor.get(9) : null;

                return alterado;
            } catch (Exception e) {
                System.err.println("ERRO AO DEFINIR VALORES");
                System.out.println(e.getMessage());
                return false;
            }

        }
    }

    public boolean criar(DAO dao, List<Object> vetor) {
        boolean alterado = false;
        if (dao != null) {
            this.dao = dao;

            Pessoa noiva = dao.getNoivos(1);
            Pessoa noivo = dao.getNoivos(0);

            int idIgreja = Util.stringToInt((String) vetor.get(1));
            Igreja igreja = idIgreja != 0 ? (Igreja) dao.getItemByID(7, idIgreja) : null;

            int idCartorio = Util.stringToInt((String) vetor.get(2));
            Cartorio cartorio = idCartorio != 0 ? (Cartorio) dao.getItemByID(8, idCartorio) : null;

            int idCerimonial = Util.stringToInt((String) vetor.get(3));
            Cerimonial cerimonial = idCerimonial != 0 ? (Cerimonial) dao.getItemByID(6, idCerimonial) : null;

            if (noiva != null) this.setNoiva(noiva);
            if (noivo != null) this.setNoivo(noivo);
            if (igreja != null) this.setIgreja(igreja);
            if (cartorio != null) this.setCartorio(cartorio);
            if (cerimonial != null) this.setCerimonial(cerimonial);

            if (vetor.get(0) != null && vetor.get(0) instanceof String) {
                String data = (String) vetor.getFirst();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                try {
                    this.data = LocalDate.parse(data, formato);
                    alterado = true;
                } catch (DateTimeParseException e) {
                    System.err.println("evento.java metodo criar()\n Erro ao definir data " + e.getMessage());
                }
            }

            // Preenche o nome caso esteja presente no vetor
            if (vetor.get(4) != null && vetor.get(4) instanceof String) {
                this.nome = (String) vetor.get(4);
            }

            // Atualiza os dados de criação e modificação se houver alteração
            if (alterado) {
                this.id = this.dao.getTotalClasse(5) + 1;
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
        if (this.nome != null && !this.nome.isEmpty()) {
            resultado.append("\n");
            resultado.append("\n\nID: ").append(this.id);
            resultado.append("  EVENTO: ").append(this.nome.toUpperCase());

            if (this.data != null) {
                resultado.append("\nData do Evento: ").append(this.data.format(formatter));
            }
            if (this.igreja != null) {
                resultado.append("\n\nIgreja: ").append(this.igreja.getNome());
                resultado.append("\nEndereço da Igreja: ").append(this.igreja != null ? this.igreja.getEndereco() : "N/A");
            }
            if (this.cartorio != null) {
                resultado.append("\n\nCartório: ").append(this.cartorio.getNome());
                resultado.append("\nEndereço do Cartório: ").append(this.cartorio != null ? this.cartorio.getEndereco() : "N/A");
            }
            if (this.cerimonial != null) {
                resultado.append("\n\nCerimonial: ").append(this.cerimonial.getNome());
            }
            if (this.noiva != null && (this.igreja != null && this.cartorio != null)) {
                if (this.noivo != null) {
                    resultado.append("\nNoiva: ").append(this.noiva.getNome());
                    resultado.append("\nNoivo: ").append(this.noivo.getNome());
                }

            }

            if (this.dataModificacao != null) {
                resultado.append("\nData da Última Modificação: ").append(this.dataModificacao.format(formatter));
            }
        }

        return resultado.toString();
    }

    public void update(List<Object> vetor) {

        boolean alterou = false;
        if (vetor.get(1) != null && vetor.get(1) instanceof String) {
            String dataStr = (String) vetor.get(1);
            if (!dataStr.isEmpty()) {
                try {
                    this.data = Util.stringToDate(dataStr);
                    alterou = true;
                } catch (DateTimeParseException e) {
                }
            }
        }
        if (vetor.get(5) != null && vetor.get(5) instanceof String) {
            String nome = (String) vetor.get(5);
            if (!nome.isEmpty()) {
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

    public String getEndereco() {
        if (this.getIgreja() != null) {
            return this.getIgreja().getEndereco();
        } else {
            if (this.getCartorio() != null) {
                return this.getCartorio().getEndereco();
            }
        }
        return null;
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


    public void setNoiva(Pessoa noiva) {
        if (noiva != null
                && noiva.getTipo().equalsIgnoreCase("NOIVA")) {
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
                && noivo.getTipo().equalsIgnoreCase("NOIVO")) {
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

    public void atualizarDataModificacao() {
        this.dataModificacao = LocalDate.now();
    }

    public String getNome() {
        return nome;
    }

    public Cartorio getCartorio() {
        return this.cartorio;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static int total;

    public Pessoa getNoiva() {
        return this.noiva;
    }

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

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public LocalDate getDataModificacao() {
        return this.dataModificacao;
    }
}
