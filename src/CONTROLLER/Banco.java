package CONTROLLER;

import MODEL.Cerimonial;
import MODEL.Evento;

import java.sql.Statement;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import MODEL.InterfaceBanco;
import MODEL.InterfaceClasse;

public class Banco {

    private Connection conexao;
    private Properties properties;
    private String con;
    private String user;
    private String senha;
    private String banco;
    private DAO dao;

    public Banco(String user, String senha, String banco, DAO dao) {
        this.dao = dao;
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", senha);
        properties.setProperty("useSSL", "false");
        properties.setProperty("useTimezone", "true");
        properties.setProperty("serverTimezone", "UTC");
        properties.setProperty("allowPublicKeyRetrieval", "true");

        this.con = "jdbc:mysql://localhost/" + banco;
        this.properties = properties;
        this.user = user;
        this.senha = senha;
        this.banco = banco;

    }

    public String montarDeleteByIDSQL(String nomeTabela, int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(nomeTabela).append(" where ID = ").append(id);
        return sql.toString();
    }

    public String montarDeleteByIDSQL(InterfaceBanco item) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(item.getNomeTabela()).append(" where ID = ").append(item.getId());
        return sql.toString();
    }

    public String montarSelectByIDSQL(String nomeTabela, int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(nomeTabela).append(" where ID = ").append(id);

        return sql.toString();
    }

    public String montarSelectByIDSQL(InterfaceBanco item) {
        if (item != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("select * from ").append(item.getNomeTabela()).append(" where ID = ").append(item.getId());
            return sql.toString();
        } else {
            System.err.println("Nao foi possivel realizar a consulta para o item, pois é nulo");
            return null;
        }

    }

    public String montarInsertSQL(InterfaceBanco item) {
        StringBuilder sql = new StringBuilder();
        List<String> campos = item.getCamposSQL();

        sql.append("insert into ").append(item.getNomeTabela()).append(" (");
        for (int i = 0; i < campos.size(); i++) {
            sql.append(campos.get(i));
            if (i < campos.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") values(");
        for (int i = 0; i < campos.size(); i++) {
            sql.append("?");
            if (i < campos.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    public String montarUpdateSQL(InterfaceBanco item) {
        StringBuilder sql = new StringBuilder();
        List<String> campos = item.getCamposSQL();
        sql.append("UPDATE ").append(item.getNomeTabela()).append(" SET ");
        for (int i = 0; i < campos.size(); i++) {
            sql.append(campos.get(i)).append(" = ?");
            if (i < campos.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ID = ").append(item.getId());
        return sql.toString();
    }

    public void updateItemBanco(InterfaceBanco item) {

        String sql = this.montarUpdateSQL(item);
        try (Connection conexao = DriverManager.getConnection(this.con, this.properties); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            int i = 1;
            for (Object valor : item.getValoresSQL()) {
                stmt.setObject(i, valor);
                i++;
            }
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
            } else {
                System.out.println("Nenhuma linha foi afetada.");
            }
        } catch (Exception e) {
            System.err.println(sql);
            System.err.println(e.getMessage());
        }

    }

    public void delItemBancoByID(String nomeTabela, int id) throws SQLException {
        this.executeSQL(this.montarDeleteByIDSQL(nomeTabela, id));
    }

    public void addItemBanco(InterfaceBanco item) {
        if (item != null) {
            try {
                this.executeComandSQL(item.getValoresSQL(), this.montarInsertSQL(item));
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public boolean findByItem(InterfaceBanco item) {
        if (item != null) {
            try {
                String sql = this.montarSelectByIDSQL(item);

                try (Connection conexao = DriverManager.getConnection(this.con, this.properties); PreparedStatement stmt2 = conexao.prepareStatement(sql);) {
                    boolean encontrado = false;
                    ResultSet rs = stmt2.executeQuery();
                    encontrado = rs.next();

                    rs.close();
                    stmt2.close();
                    conexao.close();
                    return encontrado;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public Object getItemByIDBanco(int idClasse, int idItem) {
        String nomeTabela = this.dao.getNomeTabelaByID(idClasse);

        if (nomeTabela.isEmpty()) {
            if (idClasse == 5) {
                nomeTabela = Evento.getNomeTabelaByClass();
            } else if (idClasse == 6) {
                nomeTabela = Cerimonial.getNomeTabelaByClass();
            }

        }
        if (nomeTabela.isEmpty()) {
            System.err.println("NAO FOI POSSIVEL ACESSAR O NOME DA TABELA PARA A CLASSE " + idClasse);
            return null;
        } else {
            String sqlSelect = this.montarSelectByIDSQL(nomeTabela, idItem);

            try (Connection conexao = DriverManager.getConnection(this.con, this.properties); PreparedStatement stmt2 = conexao.prepareStatement(sqlSelect); ResultSet rs = stmt2.executeQuery()) {

                Class<?> classe = this.dao.getListaClasses().get(idClasse);
                if (rs.next()) {

                    try {
                        InterfaceClasse objeto = (InterfaceClasse) classe.getDeclaredConstructor().newInstance();
                        List<Object> infos = new ArrayList<>();

                        for (Object elem : ((InterfaceBanco) objeto).getCamposSQL()) {
                            String campo = elem.toString();
                            Object valor = rs.getObject(campo);
                            if (valor != null) {
                                if (valor instanceof Integer) {
                                    infos.add((Integer) valor);
                                } else if (valor instanceof Long) {
                                    infos.add(((Long) valor).intValue());
                                } else if (valor instanceof BigDecimal) {
                                    infos.add(((BigDecimal) valor).doubleValue());
                                } else if (valor instanceof Double) {
                                    infos.add((Double) valor);
                                } else if (valor instanceof Boolean) {
                                    infos.add((Boolean) valor);
                                } else if (valor instanceof Date) {

                                    LocalDate data = ((Date) valor).toLocalDate();
                                    //ADICIONANDO UM DIA A MAIS PARA A DATA FICAR DE ACORDO COM O BANCO
                                    infos.add(data.plusDays(1));

                                } else if (valor instanceof String) {
                                    infos.add((String) valor);
                                } else {
                                    infos.add(valor);
                                }
                            } else {
                                infos.add(null);
                            }

                        }
                        boolean criado = ((InterfaceBanco) objeto).criarObjetoDoBanco(this.dao, infos);
                        if (criado) {
                            return objeto;
                        } else {
                            System.err.println("Nao foi possivel criar o objeto! ");
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InstantiationException |
                             NoSuchMethodException | SecurityException | InvocationTargetException e) {

                        System.err.println("Erro ao criar objeto: " + e.getMessage());

                    }
                }
                rs.close();
                return null;
            } catch (SQLException e) {
                // Logando qualquer erro de SQL
                System.err.println("COMANDO SQL EXECUTADO : " + sqlSelect);
                System.err.println("Banco.java getItemByID:\n Erro ao executar consulta SQL: " + e.getMessage());
                return null;
            }
        }

    }

    public void executeSQL(String sql) throws SQLException {
        try (Connection conexao = DriverManager.getConnection(this.con, this.properties);) {
            Statement stmt = conexao.createStatement();
            int linhasAfetadas = stmt.executeUpdate(sql);
            if (linhasAfetadas <= 0) {
                System.err.println("banco.java executeSQL \n" + "Não foi possível executar o comando " + sql + " !");
            }
        } catch (SQLException e) {
            System.err.println("banco.java executeSQL \n Erro ao executar SQL: " + e.getMessage());
            throw e;
        }
    }

    public void executeComandSQL(List<Object> valores, String sql) throws SQLException {
        try (Connection conexao = DriverManager.getConnection(this.con, this.properties); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            int i = 1;
            for (Object valor : valores) {

                if (valor == null) {
                    stmt.setNull(i, java.sql.Types.NULL);
                } else {
                    switch (valor.getClass().getSimpleName()) {
                        case "Double" -> stmt.setDouble(i, (Double) valor);
                        case "LocalDate" -> {
                            Date sqlDate = Date.valueOf((LocalDate) valor);
                            stmt.setString(i, sqlDate.toString());
                        }
                        case "Boolean" -> stmt.setBoolean(i, (Boolean) valor);
                        case "String" -> stmt.setString(i, (String) valor);
                        case "Integer" -> stmt.setInt(i, (Integer) valor);
                        default -> throw new SQLException("Tipo não suportado: " + valor.getClass().getSimpleName());
                    }
                }
                i++;
            }
            int affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(" executeComandSQL \n Erro ao executar SQL: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Argumento inválido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }

    public Date stringToDate(String data) {
        LocalDate localDate = LocalDate.parse(data);
        return Date.valueOf(localDate);
    }

    public List<Object> getAllElementsByClass(String nomeTabela, int idClasse) {

        String sql = "SELECT ID FROM " + nomeTabela + " ORDER BY ID";
        try (Connection conexao = DriverManager.getConnection(this.con, this.properties); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery();) {

            List<Object> lista = new ArrayList<>();

            while (rs.next()) {
                try {
                    int idObjeto = rs.getInt("ID");
                    lista.add(this.getItemByIDBanco(idClasse, idObjeto));
                } catch (SQLException e) {

                    System.err.println("Banco.java getAllElementsByClass \n Erro ao acessar id: " + e.getMessage());
                    return new ArrayList<>();
                }
            }

            return lista;
        } catch (Exception e) {
            System.err.println(sql);
            System.err.println("Banco.java getAllElementsByClass \n Erro ao executar SQL: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Connection getConexao() {
        return conexao;
    }

    public void setConexao(Connection conexao) {
        this.conexao = conexao;
    }
}
