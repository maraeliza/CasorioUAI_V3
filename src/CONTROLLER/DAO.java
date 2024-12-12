package CONTROLLER;

import java.time.format.DateTimeFormatter;
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
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

import DADOS.NomeClasse;

public class DAO {

    private List<Object> despesasAgendadas;
    private List<Object> parcelaAgendadas;
    private List<ArrayList<Object>> todosOsVetores;
    private List<Class<?>> listaClasses;
    private List<String> listaNomesClasses;
    private LocalDate dataHoje;
    private Usuario userLogado;
    private Banco banco;

    public DAO(String user, String senha, String banco) {
        try {
            this.banco = new Banco(user, senha, banco, this);

            this.listaNomesClasses = Arrays.stream(NomeClasse.values()).map(Enum::name).collect(Collectors.toList());

            this.listaClasses = new ArrayList<>(Arrays.asList(
                    Recado.class, Presente.class, Pessoa.class, Usuario.class, Fornecedor.class,
                    Evento.class, Cerimonial.class, Igreja.class, Cartorio.class, ConvidadoIndividual.class,
                    ConvidadoFamilia.class, Pagamento.class, Despesa.class, Parcela.class
            ));

            this.todosOsVetores = new ArrayList<>();
            for (int i = 0; i < 14; i++) {
                this.todosOsVetores.add(new ArrayList<>());
            }
            for (NomeClasse nomeClasse : NomeClasse.values()) {
                if (!nomeClasse.equals(NomeClasse.EVENTO)
                        && !nomeClasse.equals(NomeClasse.CONVIDADO_INDIVIDUAL)
                        && !nomeClasse.equals(NomeClasse.PARCELAS)
                        && !nomeClasse.equals(NomeClasse.PAGAMENTOS)
                ) {
                    this.syncVetorBanco(nomeClasse);
                }
            }

            this.syncVetorBanco(NomeClasse.EVENTO);
            this.syncVetorBanco(NomeClasse.CONVIDADO_INDIVIDUAL);
            this.syncVetorBanco(NomeClasse.PAGAMENTOS);

            this.dataHoje = LocalDate.now();
        } catch (Exception e) {
            System.err.println("Erro no construtor do DAO " + e.getMessage());
        }
    }

    public void getAgendados() {
        int c = 0;
        /*------------------------    DESPESAS AGENDADAS ---------------------------------- */
        List<Object> vDespesa = this.getVectorByClassName(NomeClasse.DESPESAS);
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
        List<Object> vParcela = this.getVectorByClassName(NomeClasse.PARCELAS);
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
        String nomeTabela = this.getNomeTabelaByID(idClasse);
        int total = this.banco.getTotalBanco(nomeTabela);
        return total;
    }

    public String getTexto(int idClasse, List<InterfaceClasse> vetor) {
        StringBuilder texto = new StringBuilder(this.listaNomesClasses.get(idClasse) + " ENCONTRADOS!");
        int c = 0;
        for (InterfaceClasse interfaceClasse : vetor) {
            if (interfaceClasse != null) {
                texto.append(interfaceClasse.ler());
                c++;
            }
        }

        texto.append(this.addTotal(c));
        return texto.toString();
    }

    public ArrayList<String> gerarList(NomeClasse nomeClasse) {
        List<Object> vetor = this.getVectorByClassName(nomeClasse);
        ArrayList<String> texto = new ArrayList<>();
        String nomeRelatorio = nomeClasse.toString().toLowerCase().replace("_", " ");
        texto.add("Relatório de " + nomeRelatorio);
        texto.add(nomeRelatorio.toUpperCase());
        int c = 0;
        for (Object item : vetor) {
            if (item instanceof InterfaceClasse objeto) {
                texto.add(objeto.ler());
                c++;
            }
        }

        texto.add(this.addTotal(c));
        return texto;
    }

    public String addTotal(int c) {
        StringBuilder texto = new StringBuilder();
        if (c > 1) {
            texto.append("\n\nTotal: ").append(c).append(" itens\n");
        } else if (c == 1) {
            texto.append("\n\nTotal: ").append(c).append(" item\n");
        } else {
            texto = new StringBuilder("\nNenhum item encontrado!\n");
        }
        return texto.toString();
    }

    public String getTextoParcelas() {

        StringBuilder texto = new StringBuilder("PARCELAS CADASTRADAS");
        int c = 0;

        for (Object elem : this.todosOsVetores.get(13)) {
            Parcela p = (Parcela) elem;
            texto.append(p.lerParcelaAgendada());
            c++;
        }
        texto.append(this.addTotal(c));
        return texto.toString();
    }

    public String getTexto(int idClasse) {
        this.syncVetorBanco(NomeClasse.values()[idClasse]);

        StringBuilder texto = new StringBuilder(this.listaNomesClasses.get(idClasse) + " JÁ CADASTRADOS");
        if (this.getTotalClasse(idClasse) > 1) {
            texto.append("\nTotal: ").append(this.getTotalClasse(idClasse)).append(" itens\n");
        } else if (this.getTotalClasse(idClasse) == 1) {
            texto.append("\nTotal: ").append(this.getTotalClasse(idClasse)).append(" item\n");
        }
        if (this.getTotalClasse(idClasse) > 0 && this.getTotalClasse(idClasse) <= 7) {
            List<Object> vetor = this.getVectorByClassName(NomeClasse.values()[idClasse]);
            for (Object o : vetor) {
                if (o != null) {
                    if (o instanceof InterfaceClasse) {
                        texto.append(((InterfaceClasse) o).ler());
                    }

                }
            }
        } else if (this.getTotalClasse(idClasse) > 7) {
            texto.append(this.getNomes(idClasse));
        } else {
            texto.append("\n\nNENHUM ITEM ENCONTRADO!\n");
        }

        return texto.toString();
    }

    public String getPagamentosNoivos() {
        double valorPago = 0.0;
        double valorNoiva = 0.0;
        double valorNoivo = 0.0;
        int totalPgs = 0;
        StringBuilder texto = new StringBuilder("💲 PAGAMENTOS FEITOS PELOS NOIVOS 💲 ");
        for (int i = 0; i < this.todosOsVetores.get(11).size(); i++) {
            Pagamento pg = (Pagamento) this.todosOsVetores.get(11).get(i);
            if (pg != null && pg.getPessoa() != null) {
                if (pg.getPessoa().getTipo().equalsIgnoreCase("NOIVO")
                        || pg.getPessoa().getTipo().equalsIgnoreCase("NOIVA")) {
                    texto.append("\nValor pago: ").append(pg.getValor()).append(" data do pagamento: ").append(pg.getData());
                    texto.append("\n Pagante: ").append(pg.getPessoa().getNome());
                    valorPago += pg.getValor();
                    totalPgs++;
                }
                if (pg.getPessoa().getTipo().equalsIgnoreCase("NOIVA")) {

                    valorNoiva += pg.getValor();
                }
                if (pg.getPessoa().getTipo().equalsIgnoreCase("NOIVO")) {

                    valorNoivo += pg.getValor();
                }
            }

        }
        if (totalPgs > 1) {
            texto.append("\n\nTotal: ").append(totalPgs).append(" pagamentos");
        } else {
            texto.append("\n\nTotal: ").append(totalPgs).append(" pagamento");
        }
        texto.append("\nVALOR TOTAL GASTO PELOS NOIVOS R$").append(String.format("%.2f", valorPago));
        texto.append("\nGASTOS DA NOIVA:  R$").append(String.format("%.2f", valorNoiva));
        texto.append("\nGASTOS DO NOIVO:  R$").append(String.format("%.2f", valorNoivo));
        return texto.toString();
    }

    public void syncVetorBanco(NomeClasse nomeClasse) {
        int idClasse = nomeClasse.ordinal();
        try {
            Class<?> classe = this.getClasseByID(idClasse);
            if (InterfaceBanco.class.isAssignableFrom(classe)) {
                try {

                    List<Object> vetorBanco = this.banco.getAllElementsByClass(this.getNomeTabelaByID(idClasse), idClasse);

                    if (!vetorBanco.isEmpty()) {
                        this.todosOsVetores.get(idClasse).clear();
                        for (Object elem : vetorBanco) {
                            this.todosOsVetores.get(idClasse).add(elem);
                            if (idClasse == 12) {
                                if (((Despesa) elem).isParcelado()) {
                                    ((Despesa) elem).criarParcelas();
                                }
                            }
                        }

                    } else {
                        System.out.println("Nenhum item da classe " + nomeClasse + " foi encontrado no banco");
                    }
                } catch (Exception e) {
                    System.err.println("DAO.java syncVetorBanco \nErro inesperado: " + e.getMessage());

                }
            }
        } catch (Exception e) {
            System.err.println("DAO.java syncVetorBanco \n" + e.getMessage());
        }

    }

    public List<Object> getVectorByClassName(NomeClasse nomeClasse) {
        return this.todosOsVetores.get(nomeClasse.ordinal());
    }

    public Class<?> getClasseByID(int idClasse) {
        return this.listaClasses.get(idClasse);
    }

    public String getNomeTabelaByID(int idClasse) {
        try {
            Class<?> classe = this.listaClasses.get(idClasse);
            if (InterfaceBanco.class.isAssignableFrom(classe)) {
                Method metodo = classe.getMethod("getNomeTabelaByClass");
                return (String) metodo.invoke(null);
            }

        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException
                 | InvocationTargetException e) {
            System.err.println("DAO.java getNomeTabelaByID \n" + e.getMessage());
            return "";
        }
        return "";
    }

    public boolean cadastrar(int idClasse, ArrayList<Object> infos) {
        try {
            boolean criado = false;
            Class<?> classe = this.listaClasses.get(idClasse);
            InterfaceClasse objeto = (InterfaceClasse) classe.getDeclaredConstructor().newInstance();

            criado = objeto.criar(this, infos);
            if (criado) {
                this.addVetor(idClasse, objeto);

                if (objeto instanceof InterfaceBanco objB) {
                    if (!this.banco.findByItem(objB)) {
                        this.banco.addItemBanco(objB);
                    }
                }

                if (idClasse == 12) {
                    System.out.println("----------------------CADASTRANDO PARCELAS DE DESPESA--------------------");
                    if (objeto instanceof Despesa) {
                        Despesa despesa = (Despesa) objeto;
                        if (despesa.isParcelado()) {
                            despesa.criarParcelas();
                            despesa.addVParcelaNoBanco();
                        }
                    }
                }
            }
            return criado;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException
                 | NoSuchMethodException
                 | SecurityException | InvocationTargetException e) {
            System.out.println("DAO.java cadastrar \n" + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("DAO.java cadastrar \n" + e.getMessage());
            return false;
        }
    }


    public void atualizar(int idClasse, List<Object> infos) {
        int id = Util.stringToInt((String) infos.getFirst());
        if (id != 0) {
            if (this.isInList(idClasse, id)) {
                InterfaceClasse objeto = this.getItemByID(idClasse, id);
                objeto.update(infos);
                if (objeto instanceof InterfaceBanco objB) {
                    try {
                        if (this.banco.findByItem(objB)) {
                            this.banco.updateItemBanco(objB);
                        }
                    } catch (Exception e) {
                        System.err.println("DAO.java atualizar \n" + e.getMessage());
                    }

                }
            } else {
                Util.mostrarErro("NÃO ENCONTRADO");
            }
        }

    }

    public void addVetor(int idClasse, Object ob) {
        this.getVectorByClassName(NomeClasse.values()[idClasse]).add(ob);
    }

    public void removeFromList(int idClasse, int posicao) {
        if (idClasse >= 0 && idClasse < this.todosOsVetores.size()) {
            List<Object> lista = this.getVectorByClassName(NomeClasse.values()[idClasse]);
            if (lista != null) {
                lista.remove(posicao);

            } else {
                System.out.println("Objeto na " + posicao + " da lista da classe " + idClasse + " não encontrado.");
            }
        } else {
            System.out.println("Índice da classe inválido.");
        }
    }

    public boolean isInList(int idClasse, int id) {
        for (Object elem : this.getVectorByClassName(NomeClasse.values()[idClasse])) {
            if (elem instanceof InterfaceClasse item) {
                if (item.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public InterfaceClasse getItemByID(int idClasse, int id) {
        for (Object elem : this.getVectorByClassName(NomeClasse.values()[idClasse])) {
            if (elem instanceof InterfaceClasse item) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public boolean delItemByID(int idClasse, int id) {
        System.out.println("\n\n\n---------------------------DELETANDO-----------------------------------");
        System.out.println("Iniciando exclusão de item. Classe ID: " + idClasse + ", Item ID: " + id);

        for (Object elem : this.getVectorByClassName(NomeClasse.values()[idClasse])) {
            System.out.println("Verificando elemento: " + elem);

            if (elem instanceof InterfaceClasse item) {
                System.out.println("Elemento é uma instância de InterfaceClasse. ID do item: " + item.getId());

                if (item.getId() == id) {
                    System.out.println("Item encontrado. Tentando deletar...");

                    boolean podeApagar = item.deletar();
                    System.out.println("Resultado da exclusão do item: " + podeApagar);

                    if (podeApagar) {
                        int posicao = this.getVectorByClassName(NomeClasse.values()[idClasse]).indexOf(item);
                        System.out.println("Posição do item na lista: " + posicao);

                        this.removeFromList(idClasse, posicao);
                        System.out.println("Item removido da lista.");

                        if (elem instanceof InterfaceBanco objB) {
                            System.out.println("Elemento é uma instância de InterfaceBanco.");

                            if (this.banco.findByItem(objB)) {
                                System.out.println("Item encontrado no banco. Preparando para exclusão...");

                                String nomeTabela = this.getNomeTabelaByID(idClasse);
                                System.out.println("Nome da tabela: " + nomeTabela);

                                try {
                                    if (idClasse == 12) {
                                        System.out.println("Classe identificada como 'Despesa'. Excluindo parcelas associadas...");

                                        for (Parcela p : ((Despesa) objB).getvParcelas()) {
                                            System.out.println("Processando parcela ID: " + p.getId());

                                            int posParcela = this.getVectorByClassName(NomeClasse.PARCELAS).indexOf(p);
                                            System.out.println("Posição da parcela na lista: " + posParcela);

                                            this.removeFromList(13, posParcela);
                                            this.banco.delItemBancoByID("tb_parcela", p.getId());
                                            System.out.println("Parcela removida do banco e da lista.");
                                        }
                                    }

                                    this.banco.delItemBancoByID(nomeTabela, id);
                                    System.out.println("Item removido do banco de dados. Tabela: " + nomeTabela + ", ID: " + id);

                                } catch (Exception e) {
                                    System.out.println("Erro ao tentar deletar item no banco de dados: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Objeto não está cadastrado no banco!");
                            }
                        }

                        return podeApagar;
                    }
                }
            }
        }

        System.out.println("Item não encontrado ou não pôde ser apagado.");

        return false;
    }

    public Pessoa getNoivos(int noiva) {
        Pessoa p = null;
        int n = 0;
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);
        for (Object vPessoa : vPessoas) {
            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if ((noiva == 1 && pessoa.getTipo().equalsIgnoreCase("NOIVA"))
                        || (noiva == 0 && pessoa.getTipo().equalsIgnoreCase("NOIVO"))) {
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
        StringBuilder texto = new StringBuilder();
        int n = 0;
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);
        for (Object vPessoa : vPessoas) {
            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if (pessoa.getTipo().equals("CERIMONIAL")
                        && !pessoa.isCerimonialVinculado()
                        && !pessoa.isUserVinculado()) {
                    texto.append("\nID: ").append(pessoa.getId()).append("\nNome: ").append(pessoa.getNome());
                    texto.append("     tipo: ").append(pessoa.getTipo());
                    texto.append("\n");
                    n++;
                }
            }
        }
        if (n == 0) {
            texto = new StringBuilder("\nNenhum cerimonial encontrado!");
        }
        return texto.toString();
    }

    public String getNoivo(int noiva) {
        StringBuilder texto = new StringBuilder();
        int n = 0;
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);
        for (Object vPessoa : vPessoas) {

            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if ((noiva == 1 && pessoa.getTipo().equals("NOIVA"))
                        || (noiva == 0 && pessoa.getTipo().equals("NOIVO"))) {
                    texto.append("\nID: ").append(pessoa.getId()).append("\nNome: ").append(pessoa.getNome());
                    texto.append("\n");
                    n++;
                }
            }
        }
        if (n == 0) {
            texto = new StringBuilder("\nNenhum(a) noivo(a) encontrado!");
        }
        return texto.toString();
    }

    public String getTextoNoivos() {
        StringBuilder texto = new StringBuilder("\n                    ");
        int n = 0;
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);
        for (Object vPessoa : vPessoas) {
            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if (pessoa.getTipo().equals("NOIVO")
                        || pessoa.getTipo().equals("NOIVA")) {

                    texto.append(pessoa.getNome());
                    if (n == 0) {
                        texto.append(" ❤ ");
                    }
                    n++;
                }
            }
        }
        return texto.toString();
    }

    public String getDespesasParceladasPendentes() {
        StringBuilder texto = new StringBuilder("\n                    ");

        List<Object> vObj = this.getVectorByClassName(NomeClasse.DESPESAS);
        int c = 0;
        for (Object o : vObj) {
            if (o != null) {
                Despesa despesa = (Despesa) o;
                if (!despesa.isPago() && despesa.isParcelado()) {
                    texto.append("\nID: ").append(despesa.getId()).append("\nNome: ").append(despesa.getNome());
                    texto.append("\n");
                    c++;
                }
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNenhuma despesa encontrada!\n\n");
        }
        return texto.toString();
    }

    public String getDespesasPendentes() {
        StringBuilder texto = new StringBuilder("\n                    ");

        List<Object> vObj = this.getVectorByClassName(NomeClasse.DESPESAS);
        int c = 0;
        for (Object o : vObj) {
            if (o != null) {
                Despesa despesa = (Despesa) o;
                if (!despesa.isPago()) {
                    texto.append("\nID: ").append(despesa.getId()).append("\nNome: ").append(despesa.getNome());
                    texto.append("\nVALOR TOTAL ").append(despesa.getValorTotal());
                    texto.append("\nNÚMERO DE PARCELAS ").append(despesa.getnParcelas());
                    c++;
                }
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNenhuma despesa encontrada!\n\n");
        }
        return texto.toString();
    }

    public String getDespesasPendentesAgendada() {
        StringBuilder texto = new StringBuilder("\n DESPESAS COM PAGAMENTO AGENDADO \n");

        List<Object> vObj = this.getVectorByClassName(NomeClasse.DESPESAS);
        int c = 0;
        for (Object o : vObj) {
            if (o != null) {
                Despesa despesa = (Despesa) o;
                if (!despesa.isPago() && despesa.isAgendado()) {
                    texto.append(despesa.getId()).append("\n\nNOME: ").append(despesa.getNome());
                    texto.append("\nVALOR TOTAL: ").append(despesa.getValorTotal());

                    if (despesa.isParcelado()) {
                        texto.append("\nNÚMERO DE PARCELAS: ").append(despesa.getnParcelas()).append("\n");
                    }
                    if (despesa.getFornecedor() != null) {
                        texto.append("\nFORNECEDOR: ").append(despesa.getFornecedor().getNome()).append("\n");
                    }
                    String date = despesa.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    texto.append("\nDATA DO PAGAMENTO AGENDADO: ").append(date).append("\n");
                    if (despesa.getDataPrimeiroVencimento() != null) {
                        date = despesa.getDataPrimeiroVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        texto.append("\nDATA DO PRIMEIRO VENCIMENTO: ").append(date).append("\n");
                    }
                    c++;

                } else {
                    if (!despesa.isPago() && !despesa.isAgendado() && despesa.isParcelado() && !despesa.getvParcelas().isEmpty()) {
                        for (int p = 0; p < despesa.getnParcelas(); p++) {
                            Parcela parcela = despesa.getvParcelas().get(p);
                            if (parcela != null && !parcela.isPago() && parcela.isAgendado()) {
                                texto.append(parcela.lerParcelaAgendada());
                            }

                        }

                    }

                }
            }

        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNenhuma despesa com pagamento agendado foi encontrada!\n\n");
        }
        return texto.toString();
    }

    public ArrayList<String> gerarListDespesasPendentesAgendada() {
        ArrayList<String> textoList = new ArrayList<>();
        textoList.add("\n Pagamentos Agendados \n");

        List<Object> vObj = this.getVectorByClassName(NomeClasse.DESPESAS);
        int c = 0;

        for (Object o : vObj) {
            if (o != null) {
                Despesa despesa = (Despesa) o;
                if (!despesa.isPago() && despesa.isAgendado()) {
                    textoList.add("\n\nNOME: " + despesa.getNome());
                    textoList.add("VALOR: " + despesa.getValorTotal());
                    if (despesa.isParcelado()) {
                        textoList.add("NÚMERO DE PARCELAS: " + despesa.getnParcelas());
                    }
                    if (despesa.getFornecedor() != null) {
                        textoList.add("FORNECEDOR: " + despesa.getFornecedor().getNome().toUpperCase());
                    }
                    String date = despesa.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    textoList.add("DATA DO PAGAMENTO AGENDADO: " + date);
                    if (despesa.getDataPrimeiroVencimento() != null) {
                        date = despesa.getDataPrimeiroVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        textoList.add("DATA DO PRIMEIRO VENCIMENTO: " + date);
                    }

                    c++;
                } else if (!despesa.isPago() && !despesa.isAgendado() && despesa.isParcelado() && !despesa.getvParcelas().isEmpty()) {
                    for (int p = 0; p < despesa.getnParcelas(); p++) {
                        Parcela parcela = despesa.getvParcelas().get(p);
                        if (parcela != null && !parcela.isPago() && parcela.isAgendado()) {
                            textoList.add(parcela.lerParcelaAgendada());
                        }
                    }
                }
            }
        }

        if (c == 0) {
            textoList.clear(); // Limpa caso nenhuma despesa seja encontrada
            textoList.add("\n\nNenhuma despesa com pagamento agendado foi encontrada!\n\n");
        }

        return textoList;
    }

    public String getParcelasPendentes(int idDespesa) {
        StringBuilder texto = new StringBuilder("\n");

        Despesa despesa = (Despesa) this.getItemByID(12, idDespesa);

        List<Parcela> vDespesa = despesa.getvParcelas();
        int c = 0;
        if (vDespesa != null) {

            for (Parcela parcela : vDespesa) {
                if (parcela != null && !parcela.isPago()) {
                    texto.append(parcela.ler());
                    c++;
                }
            }

        }
        if (c == 0) {
            texto = new StringBuilder("\n\nNenhuma parcela pendente de pagamento encontrada!\n\n");
        }

        return texto.toString();
    }

    public String getNomes(int idClasse) {
        StringBuilder texto = new StringBuilder("\n                    ");

        List<Object> vObj = this.getVectorByClassName(NomeClasse.values()[idClasse]);
        int c = 0;
        for (Object o : vObj) {
            if (o != null) {
                InterfaceClasse obj = (InterfaceClasse) o;
                texto.append("\nID: ").append(obj.getId()).append("      NOME: ").append(obj.getNome().toUpperCase());
                texto.append("\n");
                c++;
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNenhum cadastrado encontrado!\n\n");
        }
        return texto.toString();
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
        StringBuilder texto = new StringBuilder("\n                    ");
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);

        int c = 0;
        for (Object vPessoa : vPessoas) {
            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if (!pessoa.isUserVinculado()
                        && !pessoa.getTipo().equalsIgnoreCase("CONVIDADO")
                        && !pessoa.getTipo().equalsIgnoreCase("CERIMONIAL")) {
                    texto.append("\nID: ").append(pessoa.getId()).append("\nNome: ").append(pessoa.getNome()).append("\nTipo: ").append(pessoa.getTipo());
                    c++;
                    texto.append("\n");
                }
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNENHUMA PESSOA CADASTRADA SEM USUÁRIO VINCULADO!\n\n");
        }
        return texto.toString();
    }

    public String getNomesPessoasSemUsers() {

        StringBuilder texto = new StringBuilder("\n                    ");
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);

        int c = 0;
        for (Object vPessoa : vPessoas) {
            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if (!pessoa.isUserVinculado()
                        && !pessoa.isCerimonialVinculado() && !pessoa.isConvidadoVinculado()) {
                    texto.append("\nID: ").append(pessoa.getId()).append("\nNome: ").append(pessoa.getNome()).append("\nTipo: ").append(pessoa.getTipo());
                    c++;
                    texto.append("\n");
                }
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNENHUMA PESSOA CADASTRADA SEM USUÁRIO VINCULADO!\n\n");
        }
        return texto.toString();
    }

    public String getNomesPessoasSemConvidado() {
        StringBuilder texto = new StringBuilder("\n                    ");
        List<Object> vPessoas = this.getVectorByClassName(NomeClasse.PESSOA);

        int c = 0;
        for (Object vPessoa : vPessoas) {
            if (vPessoa != null) {
                Pessoa pessoa = (Pessoa) vPessoa;
                if (!pessoa.isUserVinculado()
                        && !pessoa.isConvidadoVinculado()
                        && pessoa.getTipo().equalsIgnoreCase("CONVIDADO")) {
                    texto.append("\nID: ").append(pessoa.getId()).append("\nNome: ").append(pessoa.getNome()).append("\nTipo: ").append(pessoa.getTipo());
                    c++;
                    texto.append("\n");
                }
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNENHUMA PESSOA CADASTRADA SEM USUÁRIO VINCULADO!\n\n");
        }
        return texto.toString();
    }

    public Usuario getUserByIdPessoa(int id) {
        List<Object> vUsers = this.getVectorByClassName(NomeClasse.USUARIOS);

        for (Object vUser : vUsers) {
            Usuario user = (Usuario) vUser;
            if (user.getIdPessoa() == id) {
                return user;
            }

        }
        return null;
    }

    public String getPresentesEscolhidos(Usuario user) {
        StringBuilder texto = new StringBuilder("\n                    ");
        List<Object> vPresente = this.getVectorByClassName(NomeClasse.PRESENTES);

        int c = 0;
        for (Object o : vPresente) {
            if (o != null) {
                Presente presente = (Presente) o;
                if (presente.getIdPessoa() == user.getIdPessoa() && presente.getEscolhido()) {
                    texto.append("\nID: ").append(presente.getId()).append("\nNome: ").append(presente.getNome()).append("\nLink: ").append(presente.getLink());
                    if (presente.isComprado()) {
                        texto.append("\nComprado: SIM");

                    } else {
                        texto.append("\nComprado: NÃO");
                    }
                    c++;
                    texto.append("\n");
                }
            }
        }

        if (c == 0) {
            texto = new StringBuilder("\n\nNenhum presente escolhido por você!\n\n");
        }
        return texto.toString();
    }

    public void logar(String user, String senha) {
        Usuario usuario = this.getUserByLogin(user);

        if (usuario != null) {
            if (usuario.getSenha().equals(senha)) {
                this.setUserLogado(usuario);
                if (usuario.getPessoa().getTipo().equalsIgnoreCase("CONVIDADO")) {
                    String texto = "\nCONFIRMAR PRESENÇA\n\nVOCÊ GOSTARIA DE CONFIRMAR SUA PRESENÇA NO CASAMENTO?\nDIGITE SIM OU NÃO PARA CONFIRMAR";
                    String resposta = JOptionPane.showInputDialog(null, texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);

                    ConvidadoIndividual conv = this.findConvidado(this.userLogado.getId());
                    if (conv != null) {
                        if (resposta.equalsIgnoreCase("SIM")) {
                            conv.setConfirmacao(true);
                            this.getBanco().updateItemBanco(conv);
                            Util.mostrarMSG("PRESENÇA CONFIRMADA!");
                        } else {
                            conv.setConfirmacao(false);
                            this.getBanco().updateItemBanco(conv);
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
        List<Object> vObj = this.getVectorByClassName(NomeClasse.CONVIDADO_INDIVIDUAL);

        for (Object o : vObj) {

            if (o != null) {
                ConvidadoIndividual conv = (ConvidadoIndividual) o;
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
        this.syncVetorBanco(NomeClasse.USUARIOS);
        List<Object> vUsers = this.getVectorByClassName(NomeClasse.USUARIOS);
        for (Object vUser : vUsers) {

            if (vUser != null) {
                Usuario user = (Usuario) vUser;

                if (user.getLogin().equals(userNome)) {
                    return user;
                }
            }
        }
        return null;
    }

    public List<InterfaceClasse> getEventosByData(LocalDate data) {
        List<InterfaceClasse> vEventoConsulta = new ArrayList<>();

        List<Object> vEvento = this.getVectorByClassName(NomeClasse.CERIMONIAL);

        for (Object o : vEvento) {
            Evento evento = (Evento) o;
            if (evento.getData().equals(data)) {
                vEventoConsulta.add(evento);
            }
        }
        return vEventoConsulta;
    }

    public List<InterfaceClasse> getParcelasByDataVencimento(LocalDate dataVencimento) {
        List<InterfaceClasse> vParcelaConsulta = new ArrayList<>();
        List<Object> vParcela = this.getVectorByClassName(NomeClasse.PARCELAS);
        for (Object o : vParcela) {
            if (o != null) {
                Parcela parcela = (Parcela) o;
                if (parcela.getDataVencimento().equals(dataVencimento)
                        && !parcela.isPago()) {
                    vParcelaConsulta.add(parcela);
                }
            }
        }
        return vParcelaConsulta;
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

    public ArrayList<String> gerarListConviteIndividual(int idConvidado, int idEvento) {
        Evento evento = (Evento) this.getItemByID(5, idEvento);
        ConvidadoIndividual conv = (ConvidadoIndividual) this.getItemByID(9, idConvidado);
        ArrayList<String> textoList = new ArrayList<>();

        if (conv != null) {
            if (evento != null) {
                textoList.add("Convite Para o Casamento de " + this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome());
                textoList.add("\nCom grande prazer, gostaríamos de convidar você, " + conv.getNome() + ", para compartilhar conosco este momento tão especial em nossas vidas.");
                textoList.add("É com imensa alegria que contamos com a sua presença para celebrar a união de " + this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome() + ".");
                textoList.add("\nEvento: " + evento.getNome());
                textoList.add("Data: " + evento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                if (evento.getEndereco() != null) {
                    textoList.add("Local: " + evento.getEndereco());
                }
                textoList.add("\nConfirme a sua presença");
                textoList.add("Para isso, basta logar com as credenciais a seguir:");
                textoList.add("Login: " + conv.getUser().getLogin());
                textoList.add("Senha: " + conv.getUser().getSenha());
                textoList.add("\nSerá uma grande honra tê-lo conosco nesse momento tão significativo de nossas vidas.");
                textoList.add("Com carinho,");
                textoList.add(this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome());
            } else {
                textoList.add("Nenhum evento com id " + idEvento + " foi encontrado!");
            }
        } else {
            textoList.add("Nenhum convidado com id " + idConvidado + " foi encontrado!");
        }

        return textoList;
    }

    public ArrayList<String> getNomesConfirmados() {
        ArrayList<String> textoList = new ArrayList<>();

        List<Object> vObj = this.getVectorByClassName(NomeClasse.CONVIDADO_INDIVIDUAL);
        int c = 0;
        double pontos = 0.0;
        double qtdPontos = 0.0;
        String pts = " ";
        textoList.add("Convidados Confirmados");
        for (Object elem : vObj) {
            ConvidadoIndividual conv = (ConvidadoIndividual) elem;
            if (conv != null && conv.isConfirmacao()) {
                textoList.add("\nNome: " + conv.getNome());
                textoList.add("Idade: " + conv.getPessoa().getIdade());
                textoList.add("Tipo: " + conv.getPessoa().getTipo().toLowerCase());
                textoList.add("Pontos: " + pts);
                textoList.add(""); // Linha em branco para separar cada convidado
                if (conv.getPessoa().getIdade() <= 8) {
                    pontos = 0.0;
                } else if (conv.getPessoa().getIdade() >= 9 && conv.getPessoa().getIdade() <= 13) {
                    pontos = 0.5;
                } else if (conv.getPessoa().getIdade() >= 14) {
                    if (conv.getPessoa().getTipo().equalsIgnoreCase("FORNECEDOR")) {
                        pontos = 0.5;
                    } else {
                        pontos = 1.0;
                    }
                }
                qtdPontos = qtdPontos + pontos;
                pts = Double.toString(pontos);
                c++;
            } else {
                pontos = 0;
            }
        }
        if (c == 0) {
            textoList.add("\nNenhum Convidado Confirmado!\n");
        }
        String qtdPts = Double.toString(qtdPontos);
        textoList.add("Total de pontos: " + qtdPts);

        return textoList;
    }

    public ArrayList<String> gerarListPagamentosNoivos() {
        double valorPago = 0.0;
        double valorNoiva = 0.0;
        double valorNoivo = 0.0;
        int totalPgs = 0;
        ArrayList<String> textoList = new ArrayList<>();

        textoList.add("Relatório de Pagamentos do Casal");

        for (int i = 0; i < this.todosOsVetores.get(11).size(); i++) {
            Pagamento pg = (Pagamento) this.todosOsVetores.get(11).get(i);
            if (pg != null && pg.getPessoa() != null) {
                if (pg.getPessoa().getTipo().equalsIgnoreCase("NOIVO") || pg.getPessoa().getTipo().equalsIgnoreCase("NOIVA")) {
                    textoList.add("\nValor pago: R$ " +String.format("%.2f", pg.getValor()) );
                    textoList.add("Descrição: " + pg.getDescricao());
                    String date =  pg.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    textoList.add("Data do pagamento: " + date);
                    textoList.add("Pagante: " + pg.getPessoa().getNome());
                    valorPago += pg.getValor();
                    totalPgs++;
                }
                if (pg.getPessoa().getTipo().equalsIgnoreCase("NOIVA")) {
                    valorNoiva += pg.getValor();
                }
                if (pg.getPessoa().getTipo().equalsIgnoreCase("NOIVO")) {
                    valorNoivo += pg.getValor();
                }
            }
        }

        String totalPgsStr = totalPgs > 1 ? "pagamentos" : "pagamento";
        textoList.add("\n\nTOTAL: " + totalPgs + " " + totalPgsStr.toUpperCase());
        textoList.add("VALOR TOTAL  R$ " + String.format("%.2f", valorPago));
        textoList.add("PAGAMENTOS FEITOS PELA NOIVA:   R$ " + String.format("%.2f", valorNoiva));
        textoList.add("PAGAMENTOS FEITOS PELO NOIVO:  R$ " + String.format("%.2f", valorNoivo));

        return textoList;
    }

    public ArrayList<String> gerarListConviteFamilia(int idEvento, int idConvidadoFamilia) {
        Evento evento = (Evento) this.getItemByID(5, idEvento);
        ConvidadoFamilia convFamilia = (ConvidadoFamilia) this.getItemByID(10, idConvidadoFamilia); // Classe ConvidadoFamilia
        ArrayList<String> textoList = new ArrayList<>();

        if (convFamilia != null) {
            if (evento != null) {
                textoList.add("Convite Para o Casamento de " + this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome());
                textoList.add("Com imensa alegria, gostaríamos de convidar a família " + convFamilia.getNome() + " para compartilhar conosco o início de uma nova jornada! É com carinho e emoção que os convidamos para o nosso casamento.");
                textoList.add("Este é um momento especial e significante em nossas vidas, e ter a sua presença tornará este dia ainda mais memorável e feliz.");
                textoList.add("Por favor, reserve a data e venha celebrar conosco a união de " + this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome() + ".");

                textoList.add("\nEvento: " + evento.getNome());
                textoList.add("Data: " + evento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                if (evento.getEndereco() != null) {
                    textoList.add("Local: " + evento.getEndereco());
                }
                textoList.add("\nPor favor, confirme a presença de sua família");
                textoList.add("Para isso, peça para o titular da sua família logar com o acesso a seguir:");
                textoList.add("Acesso: " + convFamilia.getAcesso());
                textoList.add("\n\nSerá uma grande alegria contar com a sua presença nesse momento tão especial de nossas vidas.");
                textoList.add("Atenciosamente,");
                textoList.add(this.getNoivos(0).getNome() + " e " + this.getNoivos(1).getNome());
            } else {
                textoList.add("Nenhum evento com id " + idEvento + " foi encontrado!");
            }
        } else {
            textoList.add("Nenhuma família com id " + idConvidadoFamilia + " foi encontrada!");
        }

        return textoList;
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

                if (evento.getEndereco() != null) {
                    texto += "Local: " + evento.getEndereco();
                }

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
        StringBuilder texto = new StringBuilder("\n                    ");

        List<Object> vObj = this.getVectorByClassName(NomeClasse.values()[idClasse]);
        int c = 0;

        for (Object elem : vObj) {
            ConvidadoIndividual conv = (ConvidadoIndividual) elem;
            if (conv != null && conv.isConfirmacao()) {
                texto.append("\nID: ").append(conv.getId()).append("\nNome: ").append(conv.getNome());
                texto.append("\n");
                c++;
            }
        }
        if (c == 0) {
            texto = new StringBuilder("\n\nNenhum Convidado Confirmado!\n\n");
        }

        return texto.toString();

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

    public void addInfosIniciais() throws SQLException {

    }
}
