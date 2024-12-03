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
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

import DADOS.NomeClasse;

/**
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

        this.listaNomesClasses = Arrays.stream(NomeClasse.values()).map(Enum::name).collect(Collectors.toList());

        this.listaClasses = new ArrayList<>(Arrays.asList(
                Recado.class, Presente.class, Pessoa.class, Usuario.class, Fornecedor.class,
                Evento.class, Cerimonial.class, Igreja.class, Cartorio.class, ConvidadoIndividual.class,
                ConvidadoFamilia.class, Pagamento.class, Despesa.class, Parcela.class
        ));

        this.todosOsVetores = new ArrayList<>();

        this.todosOsVetores = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            this.todosOsVetores.add(new ArrayList<>());
        }

        this.dataHoje = LocalDate.now();
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
        return this.getVectorByClassName(NomeClasse.values()[idClasse]).size();
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
        if (c > 1) {
            texto.append("\n\nTotal: ").append(c).append(" itens\n");
        } else if (c == 1) {
            texto.append("\n\nTotal: ").append(c).append(" item\n");
        } else {
            texto = new StringBuilder("\nNenhum item encontrado!\n");
        }

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

    public String getPagamentosNoivos(int idClasse) {
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
        Class<?> classe = this.getClasseByID(idClasse);
        if (InterfaceBanco.class.isAssignableFrom(classe)) {
            this.getVectorByClassName(nomeClasse).clear();
            try {
                List<Object> vetorBanco = this.banco.getAllElementsByClass(this.getNomeTabelaByID(idClasse), idClasse);
                for (Object elem : vetorBanco) {
                    this.getVectorByClassName(nomeClasse).add(elem);
                }
            } catch (SQLException e) {
                System.err.println("syncVetorBanco 434 Erro ao executar SQL: " + e.getMessage());

            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());

            }
        }


    }

    public List<Object> getVectorByClassName(NomeClasse listaNomeClasse) {
        this.syncVetorBanco(listaNomeClasse);
        return this.todosOsVetores.get(listaNomeClasse.ordinal());
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

        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException |
                 InvocationTargetException e) {

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
                if (objeto instanceof InterfaceBanco objB) {
                    if (!this.banco.findByItem(objB)) {
                        this.banco.addItemBanco(objB);
                    }
                }
            }
            return criado;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException |
                 SecurityException | InvocationTargetException e) {
            return false;
        }
    }

    public Parcela cadastrarParcela(int idClasse, List<Object> infos) {

        boolean criado = false;
        try {
            Parcela objeto = new Parcela();
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
                        System.out.println(e.getMessage());
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

    public void remove(int idClasse, int posicao) {
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

    public boolean delItemByID(int idClasse, int id) throws SQLException {
        for (Object elem : this.getVectorByClassName(NomeClasse.values()[idClasse])) {
            if (elem instanceof InterfaceClasse item) {
                if (item.getId() == id) {
                    boolean podeApagar = item.deletar();
                    if (podeApagar) {
                        int posicao = this.getVectorByClassName(NomeClasse.values()[idClasse]).indexOf(item);
                        this.remove(idClasse, posicao);
                        if (elem instanceof InterfaceBanco objB) {
                            if (this.banco.findByItem(objB)) {
                                String nomeTabela = this.getNomeTabelaByID(idClasse);
                                this.banco.delItemBancoByID(nomeTabela, id);
                            } else {
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
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
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
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
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
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);
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

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(12);
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

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(12);
        int c = 0;
        for (Object o : vObj) {
            if (o != null) {
                Despesa despesa = (Despesa) o;
                if (!despesa.isPago()) {
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

    public String getDespesasPendentesAgendada() {
        StringBuilder texto = new StringBuilder("\n DESPESAS COM PAGAMENTO AGENDADO \n");

        List<Object> vObj = (List<Object>) this.todosOsVetores.get(12);
        int c = 0;
        for (Object o : vObj) {
            if (o != null) {
                Despesa despesa = (Despesa) o;
                if (!despesa.isPago() && despesa.isAgendado()) {
                    texto.append("\nID: ").append(despesa.getId()).append("           NOME: ").append(despesa.getNome());
                    texto.append("\nVALOR: ").append(despesa.getValorTotal());
                    texto.append("\nDATA DO PAGAMENTO AGENDADO: ").append(despesa.getDataAgendamento()).append("\n");
                    c++;

                } else {
                    if (!despesa.isPago() && !despesa.isAgendado() && despesa.isParcelado()) {
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
            texto = new StringBuilder("\n\nNenhuma despesa encontrada!\n\n");
        }
        return texto.toString();
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

        List<Object> vObj = (List<Object>) this.getVectorByClassName(NomeClasse.values()[idClasse]);
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
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);

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
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);

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
        List<Object> vPessoas = (List<Object>) this.todosOsVetores.get(2);

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
        List<Object> vUsers = (List<Object>) this.todosOsVetores.get(3);

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
        List<Object> vPresente = (List<Object>) this.todosOsVetores.get(1);

        int c = 0;
        for (Object o : vPresente) {
            if (o != null) {
                Presente presente = (Presente) o;
                if (presente.getIdPessoa() == user.getIdPessoa()) {
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
        List<Object> vUsers = (List<Object>) this.todosOsVetores.get(3);

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

        List<Object> vEvento = this.todosOsVetores.get(6);
        ;

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
        List<Object> vParcela = this.todosOsVetores.get(13);
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
        StringBuilder texto = new StringBuilder("\n                    ");

        List<Object> vObj = (List<Object>) this.getVectorByClassName(NomeClasse.values()[idClasse]);
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
        ArrayList<Object> comentario = new ArrayList<>(List.of("Felicidades para o casal!"));
        this.cadastrar(0, comentario);

        ArrayList<Object> comentario1 = new ArrayList<>(List.of("Mal posso esperar pela festa!"));
        this.cadastrar(0, comentario1);

        ArrayList<Object> comentario2 = new ArrayList<>(List.of("Shippo demais! Meu casal favorito!!"));
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

        ArrayList<Object> cerDados = new ArrayList<>(List.of("6"));
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

        ArrayList<Object> famDados = new ArrayList<>(List.of("Lopes"));
        this.cadastrar(10, famDados);

        ArrayList<Object> famDados1 = new ArrayList<>(List.of("Silva"));
        this.cadastrar(10, famDados1);

        ArrayList<Object> famDados2 = new ArrayList<>(List.of("Sales"));
        this.cadastrar(10, famDados2);

        ArrayList<Object> famDados3 = new ArrayList<>(List.of("Genesio"));
        this.cadastrar(10, famDados3);

        ArrayList<Object> famDados4 = new ArrayList<>(List.of("Sampaio"));
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
}