/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package CONTROLLER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import MODEL.Cartorio;
import MODEL.Cerimonial;
import MODEL.InterfaceClasse;
import MODEL.ConvidadoFamilia;
import MODEL.ConvidadoIndividual;
import MODEL.Despesa;
import MODEL.Evento;
import MODEL.Fornecedor;
import MODEL.Igreja;
import MODEL.InterfaceBanco;
import MODEL.Pagamento;
import MODEL.Parcela;
import MODEL.Pessoa;
import MODEL.Presente;
import MODEL.Recado;

import MODEL.Usuario;
import VIEW.MenuInicial;
import VIEW.TelaInicial;
import VIEW.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 *
 * @author Mara
 */
public class DAO {

    private List<Object> despesasAgendadas;
    private List<Object> parcelaAgendadas;
    private List<ArrayList<Object>> todosOsVetores;
    private List<Class<?>> listaClasses;
    private List<String> listaNomesClasses;
    private LocalDate dataHoje;
    private Usuario userLogado;
    private Banco banco;

    public DAO(String user, String senha, String banco) throws SQLException {
        this.banco = new Banco(user, senha, banco, this);

        this.listaNomesClasses = new ArrayList<>();
        this.listaNomesClasses.add("RECADOS");
        this.listaNomesClasses.add("PRESENTES");
        this.listaNomesClasses.add("PESSOA");
        this.listaNomesClasses.add("USUÁRIOS");
        this.listaNomesClasses.add("FORNECEDOR");
        this.listaNomesClasses.add("EVENTO");

        this.listaNomesClasses.add("CERIMONIAL");
        this.listaNomesClasses.add("IGREJA");
        this.listaNomesClasses.add("CARTÓRIO");
        this.listaNomesClasses.add("CONVIDADO INDIVIDUAL");
        this.listaNomesClasses.add("CONVIDADO FAMÍLIA");

        this.listaNomesClasses.add("PAGAMENTOS");
        this.listaNomesClasses.add("DESPESAS");
        this.listaNomesClasses.add("PARCELAS");

        this.listaClasses = new ArrayList<>();
        this.listaClasses.add(Recado.class);    // RECADOS          0
        this.listaClasses.add(Presente.class);  // PRESENTES        1
        this.listaClasses.add(Pessoa.class);    // PESSOA           2
        this.listaClasses.add(Usuario.class);   // USUÁRIOS         3
        this.listaClasses.add(Fornecedor.class);// FORNECEDOR       4
        this.listaClasses.add(Evento.class);    // EVENTO           5
        this.listaClasses.add(Cerimonial.class);// CERIMONIAL       6
        this.listaClasses.add(Igreja.class);    // IGREJA           7
        this.listaClasses.add(Cartorio.class);  // CARTÓRIO         8

        this.listaClasses.add(ConvidadoIndividual.class);   // CONVIDADO INDIVIDUAL 9
        this.listaClasses.add(ConvidadoFamilia.class);      // CONVIDADO FAMÍLIA    10

        this.listaClasses.add(Pagamento.class); // PAGAMENTOS         11
        this.listaClasses.add(Despesa.class); // DESPESAS           12
        this.listaClasses.add(Parcela.class);// PARCELAS           13

        this.todosOsVetores = new ArrayList<>();

        this.todosOsVetores.add(new ArrayList<>());//0
        this.todosOsVetores.add(new ArrayList<>());//1

        this.todosOsVetores.add(new ArrayList<>());//2
        this.todosOsVetores.add(new ArrayList<>());//3

        this.todosOsVetores.add(new ArrayList<>());//4
        this.todosOsVetores.add(new ArrayList<>());//5

        this.todosOsVetores.add(new ArrayList<>());//6
        this.todosOsVetores.add(new ArrayList<>());//7

        this.todosOsVetores.add(new ArrayList<>());//8
        this.todosOsVetores.add(new ArrayList<>());//9

        this.todosOsVetores.add(new ArrayList<>());//10
        this.todosOsVetores.add(new ArrayList<>());//11

        this.todosOsVetores.add(new ArrayList<>());//12
        this.todosOsVetores.add(new ArrayList<>());//13

        this.dataHoje = LocalDate.now();
    }

    public void criar() throws SQLException {
        this.addInfosIniciais();
    }

    public void addInfosIniciais() throws SQLException {
        ArrayList<Object> comentario = new ArrayList<>(Arrays.asList("Felicidades para o casal!"));
        this.cadastrar(0, comentario);

        ArrayList<Object> comentario1 = new ArrayList<>(Arrays.asList("Mal posso esperar pela festa!"));
        this.cadastrar(0, comentario1);

        ArrayList<Object> comentario2 = new ArrayList<>(Arrays.asList("Shippo demais! Meu casal favorito!!"));
        this.cadastrar(0, comentario2);

        ArrayList<Object> presente1 = new ArrayList<>(Arrays.asList("Fogão", "Eletrodomésticos", "https://www.casasbahia.com.br/fogao/b"));
        this.cadastrar(1, presente1);

        ArrayList<Object> presente2 = new ArrayList<>(Arrays.asList("Cama", "Móveis", "https://www.casasbahia.com.br/cama/b"));
        this.cadastrar(1, presente2);

        ArrayList<Object> presente3 = new ArrayList<>(Arrays.asList("Sofá", "Móveis", "https://www.casasbahia.com.br/sofa/b"));
        this.cadastrar(1, presente3);

        ArrayList<Object> dados0 = new ArrayList<>(Arrays.asList("Pagamento agendado", "00000000", "sys", "01/01/2000"));
        this.cadastrar(2, dados0);

        ArrayList<Object> dados = new ArrayList<>(Arrays.asList("ADMINISTRADOR", "7777 5555", "ADMIN", "01/01/2001"));
        this.cadastrar(2, dados);

        ArrayList<Object> pessoa2Dados = new ArrayList<>(Arrays.asList("José", "3432 2556", "NOIVO", "01/01/2001"));
        this.cadastrar(2, pessoa2Dados);

        ArrayList<Object> pessoa3Dados = new ArrayList<>(Arrays.asList("Maria", "3431 1335", "NOIVA", "01/01/2001"));
        this.cadastrar(2, pessoa3Dados);

        ArrayList<Object> pessoa4Dados = new ArrayList<>(Arrays.asList("Ana", "3431 1335", "convidado", "01/01/2001"));
        this.cadastrar(2, pessoa4Dados);

        ArrayList<Object> pessoa5Dados = new ArrayList<>(Arrays.asList("Ricardo", "3431 1335", "cerimonial", "31/01/1989"));
        this.cadastrar(2, pessoa5Dados);

        ArrayList<Object> pessoa6Dados = new ArrayList<>(Arrays.asList("Fábio", "3431 1335", "cerimonial", "15/05/1989"));
        this.cadastrar(2, pessoa6Dados);

        ArrayList<Object> pessoa7Dados = new ArrayList<>(Arrays.asList("Marisvalda", "3431 1335", "convidado", "15/05/1989"));
        this.cadastrar(2, pessoa7Dados);

        ArrayList<Object> userDados1 = new ArrayList<>(Arrays.asList("2", "admin", "1234"));
        this.cadastrar(3, userDados1);

        ArrayList<Object> userDados3 = new ArrayList<>(Arrays.asList("3", "loginNoivo", "senha"));
        this.cadastrar(3, userDados3);

        ArrayList<Object> userDados4 = new ArrayList<>(Arrays.asList("4", "loginNoiva", "senha"));
        this.cadastrar(3, userDados4);

        ArrayList<Object> cerDados = new ArrayList<>(Arrays.asList("6"));
        this.cadastrar(6, cerDados);

        ArrayList<Object> fornecedorBuffet = new ArrayList<>(Arrays.asList("Buffet Delicioso", "12.345.678/0001-99", "(34) 1234-5678", "15000.0", "5", "em aberto"));
        this.cadastrar(4, fornecedorBuffet);

        ArrayList<Object> fornecedorDecoracao = new ArrayList<>(Arrays.asList("Flores e Cores Decoração", "98.765.432/0001-11", "(34) 9876-5432", "8000.0", "3", "pago"));
        this.cadastrar(4, fornecedorDecoracao);

        ArrayList<Object> fornecedorFotografia = new ArrayList<>(Arrays.asList("Momentos Eternos Fotografia", "11.223.344/0001-22", "(34) 1122-3344", "5000.0", "2", "em aberto"));
        this.cadastrar(4, fornecedorFotografia);

        ArrayList<Object> fornecedorMusica = new ArrayList<>(Arrays.asList("Som & Luz Banda", "22.334.556/0001-33", "(34) 2233-4455", "7000.0", "4", "pago"));
        this.cadastrar(4, fornecedorMusica);

        ArrayList<Object> fornecedorConvites = new ArrayList<>(Arrays.asList("Convites Perfeitos", "33.445.667/0001-44", "(34) 3344-5566", "2000.0", "1", "em aberto"));
        this.cadastrar(4, fornecedorConvites);

        ArrayList<Object> igrejaDados1 = new ArrayList<>(Arrays.asList("Igreja Matriz", "Rua das Flores, 123"));
        this.cadastrar(7, igrejaDados1);

        ArrayList<Object> igrejaDados2 = new ArrayList<>(Arrays.asList("Capela São José", "Avenida Central, 456"));
        this.cadastrar(7, igrejaDados2);

        ArrayList<Object> igrejaDados3 = new ArrayList<>(Arrays.asList("Igreja Nossa Senhora das Graças", "Praça das Palmeiras, 789"));
        this.cadastrar(7, igrejaDados3);

        ArrayList<Object> cartorioDados1 = new ArrayList<>(Arrays.asList("Cartório Central", "(34) 1234-5678", "Avenida Brasil, 100"));
        this.cadastrar(8, cartorioDados1);

        ArrayList<Object> cartorioDados2 = new ArrayList<>(Arrays.asList("Cartório do Povo", "(34) 8765-4321", "Rua da Independência, 200"));
        this.cadastrar(8, cartorioDados2);

        ArrayList<Object> cartorioDados3 = new ArrayList<>(Arrays.asList("Cartório e Registro São José", "(34) 5678-1234", "Praça da República, 300"));
        this.cadastrar(8, cartorioDados3);

        ArrayList<Object> eventoIgreja = new ArrayList<>(Arrays.asList("15/12/2024", "1", "0", "1", "❤ Casorio na Igreja ⛪❤"));
        this.cadastrar(5, eventoIgreja);

        ArrayList<Object> eventoCartorio = new ArrayList<>(Arrays.asList("10/12/2024", "0", "1", "1", "❤ Casorio no Civil ❤"));
        this.cadastrar(5, eventoCartorio);

        String date = Util.dateToString(this.dataHoje);
        ArrayList<Object> evento = new ArrayList<>(Arrays.asList(date, "0", "0", "0", "Apresentação do Casório UAI❤"));
        this.cadastrar(5, evento);

        ArrayList<Object> famDados = new ArrayList<>(Arrays.asList("Lopes"));
        this.cadastrar(10, famDados);

        ArrayList<Object> famDados1 = new ArrayList<>(Arrays.asList("Silva"));
        this.cadastrar(10, famDados1);

        ArrayList<Object> famDados2 = new ArrayList<>(Arrays.asList("Sales"));
        this.cadastrar(10, famDados2);

        ArrayList<Object> famDados3 = new ArrayList<>(Arrays.asList("Genesio"));
        this.cadastrar(10, famDados3);

        ArrayList<Object> famDados4 = new ArrayList<>(Arrays.asList("Sampaio"));
        this.cadastrar(10, famDados4);

        ArrayList<Object> conDados = new ArrayList<>(Arrays.asList("5", "1", "Filha"));
        this.cadastrar(9, conDados);

        ArrayList<Object> despesaDados = new ArrayList<>(Arrays.asList("1", "Comidas", "Bolo, janta, etc.", "1800.0", "2", "31/11/2024", ""));
        this.cadastrar(12, despesaDados);

        ArrayList<Object> despesaDados2 = new ArrayList<>(Arrays.asList("3", "Album", "Fotos, fotográfo, etc.", "2500.0", "2", "15/12/2024", ""));
        this.cadastrar(12, despesaDados2);
        ArrayList<Object> despesaDados3 = new ArrayList<>(Arrays.asList("2", "Decoração", "Flores, adornos, etc.", "300.0", "1", "10/11/2024", ""));
        this.cadastrar(12, despesaDados3);

        LocalDate data = Util.stringToDate("15/12/2024");
        Despesa despesa = (Despesa) this.getItemByID(12, 1);
        despesa.agendar(data, true);

 
    }

    public void getAgendados() {
        int c = 0;
        /*------------------------    DESPESAS AGENDADAS ---------------------------------- */
        List<Object> vDespesa = this.todosOsVetores.get(12);
        List<Object> vDespesaAgendadas = new ArrayList<>();

        for (Object elem : vDespesa) {
            Despesa desp = (Despesa) elem;
            if (desp != null) {
                if (desp.isAgendado() && !desp.isPago()) {
                    vDespesaAgendadas.add(desp);
                    c++;
                }
            }
        }

        this.setDespesasAgendadas(vDespesaAgendadas);
        /*------------------------    PARCELAS AGENDADAS ---------------------------------- */
        List<Object> vParcela = this.todosOsVetores.get(13);
        List<Object> vParcelaAgendadas = new ArrayList<>();

        for (Object elem : vParcela) {
            Parcela desp = (Parcela) elem;
            if (desp != null) {
                if (desp.isAgendado() && !desp.isPago()) {
                    vParcelaAgendadas.add(desp);
                    c++;
                }
            }
        }

        /* salva no vetor */
        this.setParcelaAgendadas(vParcelaAgendadas);
    }

    public void pagarAgendados() {
        this.getAgendados();
        for (Object elem : this.getDespesasAgendadas()) {
            Despesa despesa = (Despesa) elem;
            if (despesa != null && despesa.isAgendado()
                    && (despesa.getDataAgendamento().isBefore(this.dataHoje)
                    || despesa.getDataAgendamento().isEqual(this.dataHoje))) {
                despesa.pagar(true);
            }
        }
        for (Object elem : this.getParcelaAgendadas()) {
            Parcela parcela = (Parcela) elem;
            if (parcela != null && parcela.isAgendado()
                    && (parcela.getDataAgendamento().isBefore(this.dataHoje)
                    || parcela.getDataAgendamento().isEqual(this.dataHoje))) {
                parcela.pagar(true);
            }
        }

    }

    public int getTotalClasse(int idClasse) {
        return this.todosOsVetores.get(idClasse).size();
    }

    public String getTexto(int idClasse, List<InterfaceClasse> vetor) {

        String texto = this.listaNomesClasses.get(idClasse) + " ENCONTRADOS!";
        int c = 0;

        for (int i = 0; i < vetor.size(); i++) {
            if (vetor.get(i) != null) {
                if (vetor.get(i) instanceof InterfaceClasse) {
                    InterfaceClasse obj = vetor.get(i);
                    texto += obj.ler();
                    c++;
                }
            }
        }
        if (c > 1) {
            texto += "\n\nTotal: " + c + " itens\n";
        } else if (c == 1) {
            texto += "\n\nTotal: " + c + " item\n";
        } else {
            texto = "\nNenhum item encontrado!\n";
        }

        return texto;
    }

    public String getTextoParcelas() {

        String texto = "PARCELAS CADASTRADAS";
        int c = 0;

        for (Object elem : this.todosOsVetores.get(13)) {
            Parcela p = (Parcela) elem;
            texto += p.lerParcelaAgendada();
            c++;
        }
        if (c > 1) {
            texto += "\n\nTotal: " + c + " itens\n";
        } else if (c == 1) {
            texto += "\n\nTotal: " + c + " item\n";
        } else {
            texto = "\nNenhum item encontrado!\n";
        }

        return texto;
    }

    public String getTexto(int idClasse) {
        this.syncVetorBanco(idClasse);
        String texto = this.listaNomesClasses.get(idClasse) + " JÁ CADASTRADOS";
        if (this.getTotalClasse(idClasse) > 1) {
            texto += "\nTotal: " + this.getTotalClasse(idClasse) + " itens\n";
        } else if (this.getTotalClasse(idClasse) == 1) {
            texto += "\nTotal: " + this.getTotalClasse(idClasse) + " item\n";
        }
        if (this.getTotalClasse(idClasse) > 0 && this.getTotalClasse(idClasse) <= 7) {
            List<Object> vetor = this.getVetorById(idClasse);
            for (int i = 0; i < vetor.size(); i++) {
                if (vetor.get(i) != null) {
                    if (vetor.get(i) instanceof InterfaceClasse) {
                        texto += ((InterfaceClasse) vetor.get(i)).ler();
                    }

                }
            }
        } else if (this.getTotalClasse(idClasse) > 7) {
            texto += this.getNomes(idClasse);
        } else {
            texto += "\n\nNENHUM ITEM ENCONTRADO!\n";
        }

        return texto;
    }

    public List<Object> getVetorById(int idClasse) {
        return this.todosOsVetores.get(idClasse);
    }

    public String getPagamentosNoivos(int idClasse) {
        double valorPago = 0.0;
        double valorNoiva = 0.0;
        double valorNoivo = 0.0;
        int totalPgs = 0;
        String texto = "💲 PAGAMENTOS FEITOS PELOS NOIVOS 💲 ";
        for (int i = 0; i < this.todosOsVetores.get(11).size(); i++) {
            Pagamento pg = (Pagamento) this.todosOsVetores.get(11).get(i);
            if (pg != null && pg.getPessoa() != null) {
                if (pg.getPessoa().getTipo().toUpperCase().equals("NOIVO")
                        || pg.getPessoa().getTipo().toUpperCase().equals("NOIVA")) {
                    texto += "\nValor pago: " + pg.getValor() + " data do pagamento: " + pg.getData();
                    texto += "\n Pagante: " + pg.getPessoa().getNome();
                    valorPago += pg.getValor();
                    totalPgs++;
                }
                if (pg.getPessoa().getTipo().toUpperCase().equals("NOIVA")) {

                    valorNoiva += pg.getValor();
                }
                if (pg.getPessoa().getTipo().toUpperCase().equals("NOIVO")) {

                    valorNoivo += pg.getValor();
                }
            }

        }
        if (totalPgs > 1) {
            texto += "\n\nTotal: " + totalPgs + " pagamentos";
        } else {
            texto += "\n\nTotal: " + totalPgs + " pagamento";
        }
        texto += "\nVALOR TOTAL GASTO PELOS NOIVOS R$" + String.format("%.2f", valorPago);
        texto += "\nGASTOS DA NOIVA:  R$" + String.format("%.2f", valorNoiva);
        texto += "\nGASTOS DO NOIVO:  R$" + String.format("%.2f", valorNoivo);
        return texto;
    }
    public void syncVetorBanco(int idClasse){
        this.todosOsVetores.get(idClasse).clear();
        try{
            List<Object> vetorBanco = this.banco.getAllElementsByClass(this.getNomeTabelaByID(idClasse), idClasse);
            for (Object elem : vetorBanco) {
                this.todosOsVetores.get(idClasse).add(elem);
            }
        }catch (SQLException e) {
            System.err.println("syncVetorBanco 434 Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace(); // Log completo do erro
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Argumento inválido: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
       
    }

    public Class<?> getClasseByID(int idClasse) {

        return this.listaClasses.get(idClasse);
    }
    public String getNomeTabelaByID(int idClasse) {
        try {
            Class<?> classe = this.listaClasses.get(idClasse);
            if(InterfaceBanco.class.isAssignableFrom(classe)){
              Method metodo =  classe.getMethod("getNomeTabelaByClass"); 
              return (String) metodo.invoke(null);
            }

        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {

            return "";
        }
         return "";
    }

    public boolean cadastrar(int idClasse, ArrayList<Object> infos) throws SQLException {
        boolean criado = false;
        try {
            Class<?> classe = this.listaClasses.get(idClasse);
            InterfaceClasse objeto = (InterfaceClasse) classe.getDeclaredConstructor().newInstance();
            criado = objeto.criar(this, infos);
            if (criado) {
                this.addVetor(idClasse, objeto);
               
                if (idClasse == 12) {
                    Despesa despesa = (Despesa) objeto;
                    if (despesa.isParcelado()) {
                            despesa.criarParcelas();
                    }
                }
                if (objeto instanceof InterfaceBanco) {
                    InterfaceBanco objB = (InterfaceBanco) objeto;
                    if(!this.banco.findByItem(objB)){
                        this.banco.addItemBanco(objB);
                    }
                }
            }
            return criado;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            return false;
        }
    }

    public Parcela cadastrarParcela(int idClasse, List<Object> infos) {

        boolean criado = false;
        try {
            // Cria uma nova instância
            Parcela objeto = new Parcela();
            // Chama o método criar com as informações fornecidas
            criado = objeto.criar(this, infos);

            if (criado) {
                this.addVetor(13, objeto);
                return objeto;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void atualizar(int idClasse, List<Object> infos) {
        int id = Util.stringToInt((String) infos.get(0));
        if (id != 0) {
            if (this.find(idClasse, id)) {
                InterfaceClasse objeto = this.getItemByID(idClasse, id);
                objeto.update(infos);
                if (objeto instanceof InterfaceBanco) {
                    InterfaceBanco objB = (InterfaceBanco) objeto;
                    try{
                        if(this.banco.findByItem(objB)){
                            this.banco.updateItemBanco(objB);
                        }
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                   
                }
            } else {
                Util.mostrarErro("NÃO ENCONTRADO");
            }
        }

    }

    public void addVetor(int idClasse, Object ob) {
        this.todosOsVetores.get(idClasse).add(ob);
    }

    public void remove(int idClasse, int posicao) {
        if (idClasse >= 0 && idClasse < this.todosOsVetores.size()) {
            List<Object> lista = this.todosOsVetores.get(idClasse);
            if (lista != null) {
                lista.remove(posicao);

            } else {
                System.out.println("Objeto na " + posicao + " da lista da classe " + idClasse + " não encontrado.");
            }
        } else {
            System.out.println("Índice da classe inválido.");
        }
    }

    public boolean find(int idClasse, int id) {
        for (Object elem : this.todosOsVetores.get(idClasse)) {

            if (elem != null && elem instanceof InterfaceClasse) {
                InterfaceClasse item = (InterfaceClasse) elem;
                if (item.getId() == id) {
                    return true;
                }
            }

        }

        return false;
    }

    public InterfaceClasse getItemByID(int idClasse, int id) {

        for (Object elem : this.todosOsVetores.get(idClasse)) {
           
            if (elem != null && elem instanceof InterfaceClasse) {
                InterfaceClasse item = (InterfaceClasse) elem;
      
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public boolean delItemByID(int idClasse, int id)  throws SQLException {
        for (Object elem : this.todosOsVetores.get(idClasse)) {
            if (elem != null && elem instanceof InterfaceClasse) {
                InterfaceClasse item = (InterfaceClasse) elem;
                if (item.getId() == id) {
                    boolean podeApagar = item.deletar();
                    if (podeApagar) {
                        int posicao = this.todosOsVetores.get(idClasse).indexOf(item);
                        this.remove(idClasse, posicao);
                        if (elem instanceof InterfaceBanco) {
                            InterfaceBanco objB = (InterfaceBanco) elem;
                            if(this.banco.findByItem(objB)){
                                String nomeTabela = this.getNomeTabelaByID(idClasse);
                                this.banco.delItemBancoByID(nomeTabela, id);
                            }else{
                                System.out.println("Objeto não está cadastrado no banco!");
                            }
                        }
                    }

                    return podeApagar;
                }
            }
        }
        return false;
    }

    public Pessoa getNoivos(int noiva) {
        Pessoa p = null;
        int n = 0;
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
        for (int i = 0; i < vPessoas.size(); i++) {
            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if ((noiva == 1 && pessoa.getTipo().equals("NOIVA"))
                        || (noiva == 0 && pessoa.getTipo().equals("NOIVO"))) {
                    p = pessoa;
                    n++;
                }
            }
        }
        if (n == 0) {
            return null;
        }
        return p;
    }

    public String getCerimoniaisIdNomeDisponiveis() {
        String texto = "";
        int n = 0;
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
        for (int i = 0; i < vPessoas.size(); i++) {
            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if (pessoa.getTipo().equals("CERIMONIAL")
                        && !pessoa.isCerimonialVinculado()
                        && !pessoa.isUserVinculado()) {
                    texto += "\nID: " + pessoa.getId() + "\nNome: " + pessoa.getNome();
                    texto += "     tipo: " + pessoa.getTipo();
                    texto += "\n";
                    n++;
                }
            }
        }
        if (n == 0) {
            texto = "\nNenhum cerimonial encontrado!";
        }
        return texto;
    }

    public String getNoivo(int noiva) {
        String texto = "";
        int n = 0;
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
        for (int i = 0; i < vPessoas.size(); i++) {

            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if ((noiva == 1 && pessoa.getTipo().equals("NOIVA"))
                        || (noiva == 0 && pessoa.getTipo().equals("NOIVO"))) {
                    texto += "\nID: " + pessoa.getId() + "\nNome: " + pessoa.getNome();
                    texto += "\n";
                    n++;
                }
            }
        }
        if (n == 0) {
            texto = "\nNenhum(a) noivo(a) encontrado!";
        }
        return texto;
    }

    public String getTextoNoivos() {
        String texto = "\n                    ";
        int n = 0;
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
        for (int i = 0; i < vPessoas.size(); i++) {
            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if (pessoa.getTipo().equals("NOIVO")
                        || pessoa.getTipo().equals("NOIVA")) {

                    texto += pessoa.getNome();
                    if (n == 0) {
                        texto += " ❤ ";
                    }
                    n++;
                }
            }
        }
        return texto;
    }

    public String getDespesasParceladasPendentes() {
        String texto = "\n                    ";

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(12);
        int c = 0;
        for (int i = 0; i < vObj.size(); i++) {
            if (vObj.get(i) != null) {
                Despesa despesa = (Despesa) vObj.get(i);
                if (!despesa.isPago() && despesa.isParcelado()) {
                    texto += "\nID: " + despesa.getId() + "\nNome: " + despesa.getNome();
                    texto += "\n";
                    c++;
                }
            }
        }

        if (c == 0) {
            texto = "\n\nNenhuma despesa encontrada!\n\n";
        }
        return texto;
    }

    public String getDespesasPendentes() {
        String texto = "\n                    ";

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(12);
        int c = 0;
        for (int i = 0; i < vObj.size(); i++) {
            if (vObj.get(i) != null) {
                Despesa despesa = (Despesa) vObj.get(i);
                if (!despesa.isPago()) {
                    texto += "\nID: " + despesa.getId() + "\nNome: " + despesa.getNome();
                    texto += "\n";
                    c++;
                }
            }
        }

        if (c == 0) {
            texto = "\n\nNenhuma despesa encontrada!\n\n";
        }
        return texto;
    }

    public String getDespesasPendentesAgendada() {
        String texto = "\n DESPESAS COM PAGAMENTO AGENDADO \n";

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(12);
        int c = 0;
        for (int i = 0; i < vObj.size(); i++) {
            if (vObj.get(i) != null) {
                Despesa despesa = (Despesa) vObj.get(i);
                if (!despesa.isPago() && despesa.isAgendado()) {
                    texto += "\nID: " + despesa.getId() + "           NOME: " + despesa.getNome();
                    texto += "\nVALOR: " + despesa.getValorTotal();
                    texto += "\nDATA DO PAGAMENTO AGENDADO: " + despesa.getDataAgendamento() + "\n";
                    c++;

                } else {
                    if (!despesa.isPago() && !despesa.isAgendado() && despesa.isParcelado()) {
                        for (int p = 0; p < despesa.getnParcelas(); p++) {
                            Parcela parcela = despesa.getvParcelas().get(p);
                            if (parcela != null && !parcela.isPago() && parcela.isAgendado()) {
                                texto += parcela.lerParcelaAgendada();
                            }

                        }

                    }

                }
            }

        }

        if (c == 0) {
            texto = "\n\nNenhuma despesa encontrada!\n\n";
        }
        return texto;
    }

    public String getParcelasPendentes(int idDespesa) {
        String texto = "\n";

        Despesa despesa = (Despesa) this.getItemByID(12, idDespesa);

        List<Parcela> vDespesa = despesa.getvParcelas();
        int c = 0;
        if (vDespesa != null) {

            for (int i = 0; i < vDespesa.size(); i++) {
                if (vDespesa.get(i) != null && !vDespesa.get(i).isPago()) {
                    texto += vDespesa.get(i).ler();
                    c++;
                }
            }

        }
        if (c == 0) {
            texto = "\n\nNenhuma parcela pendente de pagamento encontrada!\n\n";
        }

        return texto;
    }

    public String getNomes(int idClasse) {
        String texto = "\n                    ";

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(idClasse);
        int c = 0;
        for (int i = 0; i < vObj.size(); i++) {
            if (vObj.get(i) != null) {
                InterfaceClasse obj = (InterfaceClasse) vObj.get(i);
                texto += "\nID: " + obj.getId() + "      NOME: " + obj.getNome().toUpperCase();
                texto += "\n";
                c++;
            }
        }

        if (c == 0) {
            texto = "\n\nNenhum cadastrado encontrado!\n\n";
        }
        return texto;
    }

    public void mostrarPagamentosAgendados() {
        // Tenta obter os nomes dos convidados
        String texto = this.getDespesasPendentesAgendada();

        // Verifica se a lista está vazia ou nula
        if (texto == null || texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum pagamento agendado encontrado.", "Lista de Pagamentos Agendados", JOptionPane.INFORMATION_MESSAGE);
        } else {

            JOptionPane.showMessageDialog(null, texto, "Lista de Convidados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public String getNomesPessoasParaCriarUsers() {
        String texto = "\n                    ";
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);

        int c = 0;
        for (int i = 0; i < vPessoas.size(); i++) {
            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if (!pessoa.isUserVinculado()
                        && !pessoa.getTipo().toUpperCase().equals("CONVIDADO")
                        && !pessoa.getTipo().toUpperCase().equals("CERIMONIAL")) {
                    texto += "\nID: " + pessoa.getId() + "\nNome: " + pessoa.getNome() + "\nTipo: " + pessoa.getTipo();
                    c++;
                    texto += "\n";
                }
            }
        }

        if (c == 0) {
            texto = "\n\nNENHUMA PESSOA CADASTRADA SEM USUÁRIO VINCULADO!\n\n";
        }
        return texto;
    }

    public String getNomesPessoasSemUsers() {

        String texto = "\n                    ";
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);

        int c = 0;
        for (int i = 0; i < vPessoas.size(); i++) {
            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if (!pessoa.isUserVinculado()
                        && !pessoa.isCerimonialVinculado() && !pessoa.isConvidadoVinculado()) {
                    texto += "\nID: " + pessoa.getId() + "\nNome: " + pessoa.getNome() + "\nTipo: " + pessoa.getTipo();
                    c++;
                    texto += "\n";
                }
            }
        }

        if (c == 0) {
            texto = "\n\nNENHUMA PESSOA CADASTRADA SEM USUÁRIO VINCULADO!\n\n";
        }
        return texto;
    }

    public String getNomesPessoasSemConvidado() {
        String texto = "\n                    ";
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);

        int c = 0;
        for (int i = 0; i < vPessoas.size(); i++) {
            if (vPessoas.get(i) != null) {
                Pessoa pessoa = (Pessoa) vPessoas.get(i);
                if (!pessoa.isUserVinculado()
                        && !pessoa.isConvidadoVinculado()
                        && pessoa.getTipo().toUpperCase().equals("CONVIDADO")) {
                    texto += "\nID: " + pessoa.getId() + "\nNome: " + pessoa.getNome() + "\nTipo: " + pessoa.getTipo();
                    c++;
                    texto += "\n";
                }
            }
        }

        if (c == 0) {
            texto = "\n\nNENHUMA PESSOA CADASTRADA SEM USUÁRIO VINCULADO!\n\n";
        }
        return texto;
    }

    public Usuario getUserByIdPessoa(int id) {
        List<Object> vUsers = (List<Object>) this.todosOsVetores.get(3);

        for (int u = 0; u < vUsers.size(); u++) {
            Usuario user = (Usuario) vUsers.get(u);
            if (user.getIdPessoa() == id) {
                return user;
            }

        }
        return null;
    }

    public String getPresentesEscolhidos(Usuario user) {
        String texto = "\n                    ";
        List<Object> vPresente = (List<Object>) this.todosOsVetores.get(1);

        int c = 0;
        for (int i = 0; i < vPresente.size(); i++) {
            if (vPresente.get(i) != null) {
                Presente presente = (Presente) vPresente.get(i);
                if (presente.getIdPessoa() == user.getIdPessoa()) {
                    texto += "\nID: " + presente.getId() + "\nNome: " + presente.getNome() + "\nLink: " + presente.getLink();
                    if (presente.isComprado()) {
                        texto += "\nComprado: SIM";

                    } else {
                        texto += "\nComprado: NÃO";
                    }
                    c++;
                    texto += "\n";
                }
            }
        }

        if (c == 0) {
            texto = "\n\nNenhum presente escolhido por você!\n\n";
        }
        return texto;
    }

    public void logar(String user, String senha) {
        Usuario usuario = this.getUserByLogin(user);
        if (usuario != null) {
            if (usuario.getSenha().equals(senha)) {
                this.setUserLogado(usuario);
                if (usuario.getPessoa().getTipo().toUpperCase().equals("CONVIDADO")) {
                    String texto = "\nCONFIRMAR PRESENÇA\n\nVOCÊ GOSTARIA DE CONFIRMAR SUA PRESENÇA NO CASAMENTO?\nDIGITE SIM OU NÃO PARA CONFIRMAR";
                    String resposta = JOptionPane.showInputDialog(null, texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);

                    ConvidadoIndividual conv = this.findConvidado(this.userLogado.getId());
                    if (conv != null) {
                        if (resposta.toUpperCase().equals("SIM")) {
                            conv.setConfirmacao(true);
                            Util.mostrarMSG("PRESENÇA CONFIRMADA!");
                        } else {
                            conv.setConfirmacao(false);
                            Util.mostrarMSG("Obrigado pela resposta ❤! \nAté mais!");
                        }

                        TelaInicial menu = new TelaInicial();
                        menu.exibir(this);
                    }
                } else {

                    MenuInicial menu = new MenuInicial();
                    menu.exibir(this, true, this.getUserLogado());
                }

            } else {

                Util.mostrarErro("Credenciais incorretas!");
                this.deslogar();
            }
        } else {

            Util.mostrarErro("Credenciais incorretas!");
            this.deslogar();
        }
    }

    public ConvidadoIndividual findConvidado(int idUser) {
        List<Object> vObj = (List<Object>) this.todosOsVetores.get(9);

        for (int i = 0; i < vObj.size(); i++) {

            if (vObj.get(i) != null) {
                ConvidadoIndividual conv = (ConvidadoIndividual) vObj.get(i);
                if (conv.getIdUser() == idUser) {
                    return conv;
                }

            }
        }
        return null;
    }

    public void deslogar() {
        this.userLogado = null;
        TelaInicial menu = new TelaInicial();
        menu.exibir(this);
    }

    public Usuario getUserByLogin(String userNome) {
        List<Object> vUsers = (List<Object>) this.todosOsVetores.get(3);

        for (int i = 0; i < vUsers.size(); i++) {

            if (vUsers.get(i) != null) {
                Usuario user = (Usuario) vUsers.get(i);

                if (user.getLogin().equals(userNome)) {
                    return user;
                }
            }
        }
        return null;
    }

    public List<InterfaceClasse> getEventosByData(LocalDate data) {
        List<InterfaceClasse> vEventoConsulta = new ArrayList<>();

        List<Object> vEvento = this.todosOsVetores.get(6);;

        for (int i = 0; i < vEvento.size(); i++) {
            Evento evento = (Evento) vEvento.get(i);
            if (evento.getData().equals(data)) {
                vEventoConsulta.add(evento);
            }
        }
        return vEventoConsulta;
    }

    public List<Pagamento> getPagamentoByData(LocalDate dataPagamento) {
        ArrayList<Pagamento> vPagamentoConsulta = new ArrayList<>();

        List<Object> vPagamento = this.todosOsVetores.get(11);

        for (int i = 0; i < vPagamento.size(); i++) {
            if (vPagamento.get(i) != null) {
                Pagamento pagamento = (Pagamento) vPagamento.get(i);
                if (pagamento.getData().equals(dataPagamento)) {
                    vPagamentoConsulta.add(pagamento);
                }
            }
        }
        return vPagamentoConsulta;
    }

    public List<InterfaceClasse> getParcelasByDataVencimento(LocalDate dataVencimento) {
        List<InterfaceClasse> vParcelaConsulta = new ArrayList<>();
        List<Object> vParcela = this.todosOsVetores.get(13);
        for (int i = 0; i < vParcela.size(); i++) {
            if (vParcela.get(i) != null) {
                Parcela parcela = (Parcela) vParcela.get(i);
                if (parcela.getDataVencimento().equals(dataVencimento)
                        && !parcela.isPago()) {
                    vParcelaConsulta.add(parcela);
                }
            }
        }
        return vParcelaConsulta;
    }

    public List<Despesa> getDespesasByDataAgendamento(int idClasse, LocalDate dataAgendamento) {
        List<Despesa> vDespesaConsulta = new ArrayList<>();
        List<Object> vDespesa = this.todosOsVetores.get(12);

        for (Object elem : vDespesa) {
            Despesa despesa = (Despesa) elem;
            if (despesa != null && despesa.getDataAgendamento().equals(dataAgendamento)) {
                vDespesaConsulta.add(despesa);
            }
        }
        return vDespesaConsulta;
    }

    public String getIprimirConviteINdividual(int idConvidado, int idEvento) {
        Evento evento = (Evento) this.getItemByID(5, idEvento);
        ConvidadoIndividual conv = (ConvidadoIndividual) this.getItemByID(9, idConvidado);
        String texto = "\n                    ";
        if (conv != null) {
            if (evento != null) {
                texto += "\n Convite Para o Casamento de " + this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome() + "\n";

                texto += "\nCom muito prazer, gostaríamos de convidar você " + conv.getNome() + " para o nosso casamento!\n";
                texto += "Evento: " + evento.getNome() + " \n";
                texto += "Data: " + evento.getData() + " \n";
                texto += "Local: " + evento.getEndereco() + " \n";
                texto += "\nPor favor, confirme sua presença\n";
                texto += "\nPara isso, basta logar com as credenciais a seguir: \n";
                texto += "Login: " + conv.getUser().getLogin() + " \n";
                texto += "Senha: " + conv.getUser().getSenha() + " \n";
            } else {
                texto = "\n\nNenhum evento com id " + idEvento + " foi encontrado!\n\n";
            }
        } else {
            texto = "\n\nNenhum convidado com id " + idConvidado + " foi encontrado!\n\n";
        }

        return texto;

    }

    public String gerarConviteFamilia(int idEvento, int idConvidadoFamilia) {
        Evento evento = (Evento) this.getItemByID(5, idEvento);
        ConvidadoFamilia convFamilia = (ConvidadoFamilia) this.getItemByID(10, idConvidadoFamilia); // Classe ConvidadoFamilia
        String texto = "\n                    ";

        if (convFamilia != null) {
            if (evento != null) {
                texto += "\n Convite Para o Casamento de " + this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome() + "\n";
                texto += "\nCom muito prazer, gostaríamos de convidar a família " + convFamilia.getNome() + " para o nosso casamento!\n";
                texto += "Evento: " + evento.getNome() + " \n";
                texto += "Data: " + evento.getData() + " \n";
                texto += "Local: " + evento.getEndereco() + " \n";
                texto += "\nPor favor, confirme a presença de sua família\n";
                texto += "\nPara isso, peça para o titular da sua família logar com o acesso a seguir: \n";
                texto += "Acesso: " + convFamilia.getAcesso() + " \n"; // Acesso específico da família

            } else {
                texto = "\nNenhum evento com id " + idEvento + " foi encontrado!\n\n";
            }
        } else {
            texto = "\nNenhuma família com id " + idConvidadoFamilia + " foi encontrada!\n\n";
        }

        return texto;
    }

    public String getNomesConfirmados(int idClasse) {
        String texto = "\n                    ";

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(idClasse);
        int c = 0;

        for (Object elem : vObj) {
            ConvidadoIndividual conv = (ConvidadoIndividual) elem;
            if (conv != null && conv.isConfirmacao() == true) {
                texto += "\nID: " + conv.getId() + "\nNome: " + conv.getNome();
                texto += "\n";
                c++;
            }
        }
        if (c == 0) {
            texto = "\n\nNenhum Convidado Confirmado!\n\n";
        }

        return texto;

    }

    public String getNameClasseById(int idClasse) {
        return this.listaNomesClasses.get(idClasse);
    }

    public LocalDate getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(LocalDate dataHoje) {
        this.dataHoje = dataHoje;
    }

    public Usuario getUserLogado() {
        return userLogado;
    }

    public void setUserLogado(Usuario userLogado) {
        this.userLogado = userLogado;
    }

    public List<Object> getDespesasAgendadas() {
        return despesasAgendadas;
    }

    public void setDespesasAgendadas(List<Object> despesasAgendadas) {
        this.despesasAgendadas = despesasAgendadas;
    }

    public List<Object> getParcelaAgendadas() {
        return parcelaAgendadas;
    }

    public void setParcelaAgendadas(List<Object> parcelaAgendadas) {
        this.parcelaAgendadas = parcelaAgendadas;
    }

    public List<ArrayList<Object>> getTodosOsVetores() {
        return todosOsVetores;
    }

    public List<Class<?>> getListaClasses() {
        return listaClasses;
    }

    public void setListaClasses(List<Class<?>> listaClasses) {
        this.listaClasses = listaClasses;
    }

    public List<String> getListaNomesClasses() {
        return listaNomesClasses;
    }

    public void setListaNomesClasses(List<String> listaNomesClasses) {
        this.listaNomesClasses = listaNomesClasses;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
    
    
}