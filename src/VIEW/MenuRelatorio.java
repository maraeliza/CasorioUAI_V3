/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import MODEL.Usuario;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;

/**
 *
 * @author Mara
 */
public class MenuRelatorio {

    private String texto;
    private String op;
    private boolean logou;
    private String opcoes[];
    private Usuario userLogado;
    private String listaNomeClasses[] = new String[20];

    private int nOps;
    private DAO dao;

    public MenuRelatorio() {

        setLista();

    }

    private void setLista() {
        String listaClasses[] = new String[20];
        listaClasses[0] = "RECADOS";
        listaClasses[1] = "IMPRIMIR CONVITE INDIVIDUAL";
        listaClasses[2] = "IMPRIMIR CONVITE FAMÍLIA";
        listaClasses[3] = "PAGAMENTOS DOS NOIVOS";
        listaClasses[4] = "CONVIDADOS";
        listaClasses[5] = "CONVIDADOS CONFIRMADOS";
        listaClasses[6] = "PAGAMENTOS AGENDADOS";
        this.listaNomeClasses = listaClasses;
    }

    private String[] getLista() {
        return this.listaNomeClasses;
    }

    private void definirTexto() {
        this.texto = "";
        this.texto += "\n\nRELATÓRIOS";
        this.texto += "\n\nEscolha a opção a seguir ";

        this.texto += this.definirOpcoes();

        this.texto += "\n\nDigite aqui o número da sua opção: ";

    }

    private String definirOpcoes() {
        String Opcoes = "";
        this.nOps = 1;

        for (int n = 0; n < this.listaNomeClasses.length; n++) {
            if (this.listaNomeClasses[n] != null) {
                Opcoes += "\n" + Util.intToString(this.nOps) + ". " + this.listaNomeClasses[n];
                this.nOps++;
            }

        }

        Opcoes += "\n" + Util.intToString(this.nOps) + ". SAIR";
        return Opcoes;
    }

    public void exibir(DAO dao, Usuario user, boolean logou) {
        this.userLogado = user;
        this.logou = logou;
        this.dao = dao;
        int o = -1;
        do {
            this.definirTexto();

            this.op = JOptionPane.showInputDialog(null, this.texto, "UaiCasórioPro", JOptionPane.QUESTION_MESSAGE);

            if (this.op != null) {
                o = Util.stringToInt(this.op);
                this.lidarEscolha(o);
            } else {
                TelaInicial menu = new TelaInicial();
                op = menu.exibir(this.dao);
            }
        } while (o != 0 && this.op != null);

    }

    private void lidarEscolha(int o) {

        switch (o) {
            case 1: {
                Menu_READ menuVer = new Menu_READ();
                menuVer.exibir(this.dao, 0);
                break;
            }
            case 4: {
                Menu_READ menuVer = new Menu_READ();
                menuVer.exibir(this.dao, 11, true);
                break;
            }
            case 2:
                this.imprimirConviteIndividual();
                break;
            case 3:
                this.imprimirConviteFamilia();
                break;
            case 5:
                Menu_READ menuVer = new Menu_READ();
                menuVer.exibir(this.dao, 9);
                break;
            case 6:
                this.mostrarConvidadosConfirmados();
                break;
            case 7:
                this.dao.mostrarPagamentosAgendados();
                break;
            default:
                break;
        }

        if (o >= this.nOps) {
            MenuInicial menu = new MenuInicial();
            menu.exibir(this.dao, this.logou, this.userLogado);
        }

    }

    private void imprimirConviteIndividual() {

        String texto = "\nIMPRESSÃO DE CONVITES INDIVIDUAIS";
        texto += "\n                    ";
        texto += "\nLISTA DE CONVIDADOS: ";
        texto += "\n " + this.dao.getNomes(9);
        texto += "\n\nINSIRA 0 PARA VOLTAR:";
        texto += "\nINSIRA O ID DO CONVIDADO:";

        String idNomeConvidado = JOptionPane.showInputDialog(null, texto, "Imprimir Convite Individual", JOptionPane.QUESTION_MESSAGE);

        if (idNomeConvidado != null && !idNomeConvidado.trim().isEmpty()) {
           

            texto = "\nIMPRESSÃO DE CONVITES INDIVIDUAIS";
            texto += "\n                    ";
            texto += "\nLISTA DE EVENTOS: ";
            texto += "\n " + this.dao.getNomes(5);
            texto += "\nINSIRA O ID DO EVENTO PARA GERAR O CONVITE:";

            String idEventoInserido = JOptionPane.showInputDialog(null, texto, "Imprimir Convite Individual", JOptionPane.QUESTION_MESSAGE);
            int idConvidado = Util.stringToInt(idNomeConvidado);
            int idEvento = Util.stringToInt(idEventoInserido);

            String gerandoConvite = this.dao.getIprimirConviteINdividual(idConvidado,idEvento);
            JOptionPane.showMessageDialog(null, gerandoConvite, "Convite", JOptionPane.INFORMATION_MESSAGE);
        } else {

            JOptionPane.showMessageDialog(null, "Id do convidado não inserido.", "Erro", JOptionPane.WARNING_MESSAGE);

        }
    }

    private void imprimirConviteFamilia() {

        String texto = "\nIMPRESSÃO DE CONVITES";
        texto += "\n                    ";
        texto += "\nLISTA DE FAMÍLIAS: ";
        texto += "\n " + this.dao.getNomes(10);
        texto += "\n\nINSIRA 0 PARA VOLTAR:";
        texto += "\nINSIRA O ID DA FAMÍLIA:";

        String idNomeConvidado = JOptionPane.showInputDialog(null, texto, "Imprimir Convite Familiar", JOptionPane.QUESTION_MESSAGE);

        if (idNomeConvidado != null && !idNomeConvidado.trim().isEmpty()) {
            int idFamilia = Util.stringToInt(idNomeConvidado);

            texto = "\nIMPRESSÃO DE CONVITES";
            texto += "\n                    ";
            texto += "\nLISTA DE EVENTOS: ";
            texto += "\n " + this.dao.getNomes(5);
            texto += "\nINSIRA O ID DO EVENTO PARA GERAR O CONVITE:";

            String idEventoInserido = JOptionPane.showInputDialog(null, texto, "Imprimir Convite Familiar", JOptionPane.QUESTION_MESSAGE);

            int idEvento = Util.stringToInt(idEventoInserido);
            String gerandoConvite = this.dao.gerarConviteFamilia(idEvento, idFamilia);
            JOptionPane.showMessageDialog(null, gerandoConvite, "Convite", JOptionPane.INFORMATION_MESSAGE);
            this.gerarPDF("convite", gerandoConvite);
        } else {

            JOptionPane.showMessageDialog(null, "Id do convidado não inserido.", "Erro", JOptionPane.WARNING_MESSAGE);

        }
    }
    public void gerarPDF(String nomeArquivo, String texto) {
        try {
            String local = System.getProperty("user.dir") + File.separator + "src" + File.separator + "RELATORIOS" + File.separator;
            Document documento = new Document(PageSize.A2);
            PdfWriter.getInstance(documento, new FileOutputStream(local + nomeArquivo + ".pdf"));
            documento.open();
            Paragraph conteudo = new Paragraph(texto);
            documento.add(conteudo);
            documento.close();
            System.out.println(nomeArquivo + ".pdf adicionado com sucesso");
        } catch (DocumentException | FileNotFoundException e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
        }
    }

    private void mostrarConvidados() {
        // Tenta obter os nomes dos convidados
        String nomeConvidados = this.dao.getNomes(9);

        // Verifica se a lista está vazia ou nula
        if (nomeConvidados == null || nomeConvidados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum convidado encontrado.", "Lista de Convidados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Exibe os nomes dos convidados em um JOptionPane
            String mensagem = "Lista de Convidados:\n" + nomeConvidados + "\n\nClique em OK para voltar.";
            JOptionPane.showMessageDialog(null, mensagem, "Lista de Convidados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarConvidadosConfirmados() {

        String NomeConvidadosConfirmados = this.dao.getNomesConfirmados(9);

        // Verifica se há convidados confirmados 
        // Esse if é reddundante porque o metodo ja retorna senão houver Convidados Confirmados
        if (NomeConvidadosConfirmados == null || NomeConvidadosConfirmados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum convidado confirmou presença.", "Convidados Confirmados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Exibe os nomes dos convidados confirmados
            String mensagem = "Lista de Convidados Confirmados:\n" + NomeConvidadosConfirmados + "\n\nClique em OK para voltar.";
            JOptionPane.showMessageDialog(null, mensagem, "Convidados Confirmados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
