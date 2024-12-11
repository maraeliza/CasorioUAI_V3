/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import CONTROLLER.DAO;
import DADOS.NomeClasse;
import MODEL.ConvidadoFamilia;
import MODEL.ConvidadoIndividual;
import MODEL.Evento;
import MODEL.Usuario;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;

import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;

import com.itextpdf.text.DocumentException;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.BaseFont;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
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
    private static final String FONTENORMAL = "src/ASSETS/Forum-Regular.ttf";
    private static final String FONTETITULO = "src/ASSETS/PinyonScript-Regular.ttf";
    private BaseFont fonteNormal;

    private BaseFont fonteTitulo;

    public MenuRelatorio() {

        setLista();
        try { // Tenta carregar as fontes de texto e título personalizadas
            fonteNormal = BaseFont.createFont(FONTENORMAL, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fonteTitulo = BaseFont.createFont(FONTETITULO, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        } catch (DocumentException | IOException e) {
            System.out.println(e.getMessage());
        }
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
                this.imprimirRecados();
                break;
            }
            case 2:
                this.imprimirConviteIndividual();
                break;
            case 3:
                this.imprimirConviteFamilia();
                break;
            case 4:
                this.imprimirPagamentoNoivos();
                break;
            case 5:
                this.imprimirConvidadosIndividuais();
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

    private void imprimirRecados() {
        Menu_READ menuVer = new Menu_READ();
        menuVer.exibir(this.dao, 0);
        this.gerarPDF("relatorio_recados", this.dao.gerarList(NomeClasse.RECADOS));
    }

    private void imprimirConvidadosIndividuais() {
        Menu_READ menuVer = new Menu_READ();
        menuVer.exibir(this.dao, 9);
        this.gerarPDF("relatorio_convidados_individuais", this.dao.gerarList(NomeClasse.CONVIDADO_INDIVIDUAL));

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

            String gerandoConvite = this.dao.getIprimirConviteINdividual(idConvidado, idEvento);
            JOptionPane.showMessageDialog(null, gerandoConvite, "Convite", JOptionPane.INFORMATION_MESSAGE);

            Evento evento = (Evento) this.dao.getItemByID(5, idEvento);
            ConvidadoIndividual conv = (ConvidadoIndividual) this.dao.getItemByID(9, idConvidado);
            try {
                String nomeArquivo = "Convite_" + conv.getNome() + "_" + evento.getNome();
                this.gerarPDF(nomeArquivo.toLowerCase(), this.dao.gerarListConviteIndividual(idEvento, idConvidado));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
            Evento evento = (Evento) this.dao.getItemByID(5, idEvento);
            ConvidadoFamilia convFamilia = (ConvidadoFamilia) this.dao.getItemByID(10, idFamilia);
            String gerandoConvite = this.dao.gerarConviteFamilia(idEvento, idFamilia);
            JOptionPane.showMessageDialog(null, gerandoConvite, "Convite", JOptionPane.INFORMATION_MESSAGE);
            try {
                String nomeArquivo = "Convite_" + convFamilia.getNome() + "_" + evento.getNome();
                this.gerarPDF(nomeArquivo.toLowerCase(), this.dao.gerarListConviteFamilia(idEvento, idFamilia));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else {

            JOptionPane.showMessageDialog(null, "Id do convidado não inserido.", "Erro", JOptionPane.WARNING_MESSAGE);

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

    private void imprimirPagamentoNoivos() {
        String relatorio = this.dao.getPagamentosNoivos();
        JOptionPane.showMessageDialog(null, relatorio, "Relatorio de pagamento dos noivos", JOptionPane.INFORMATION_MESSAGE);
        this.gerarPDF("relatorio_pagamento_dos_noivos", this.dao.gerarListPagamentosNoivos());
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

    public void gerarPDF(String nomeArquivo, ArrayList<String> listaConteudo) {
        try {
            BaseColor corDourada = new BaseColor(162, 113, 55);
            Font fontNormal = new Font(fonteNormal, 12, Font.BOLD, corDourada);
            Font fontTitle = new Font(fonteTitulo, 26, Font.BOLD, corDourada);

            String local = System.getProperty("user.dir") + File.separator + "src" + File.separator + "RELATORIOS" + File.separator;
            Document documento = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(local + nomeArquivo + ".pdf"));

            BackgroundImage bg = new BackgroundImage("src/ASSETS/bg1.png");
            writer.setPageEvent(bg);

            documento.open();

            Paragraph título = new Paragraph(listaConteudo.getFirst(), fontTitle);
            título.setAlignment(Element.ALIGN_CENTER);
            título.setSpacingBefore(70f);
            título.setSpacingAfter(70f);
            documento.add(título);

            for (int i = 1; i < listaConteudo.size(); i++) { // Começa do segundo item
                String linha = listaConteudo.get(i);
                Paragraph conteudo = new Paragraph(linha, fontNormal);
                conteudo.setAlignment(Element.ALIGN_LEFT);
                conteudo.setSpacingBefore(10f);
                conteudo.setIndentationLeft(20f);
                conteudo.setIndentationRight(20f);
                documento.add(conteudo);

            }

            LocalDateTime dataAtual = LocalDateTime.now();
            FooterEvent footerEvent = new FooterEvent("Relatorio gerado em: " + dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            writer.setPageEvent(footerEvent);

            documento.close();

            String msg = String.format(
                    "Relatório gerado com sucesso ✔!%n%n"
                    + "📂 Você pode acessá-lo na pasta RELATORIOS com o nome:%n"
                    + "📃 ➡ %s.pdf%n%n" + "\n\nEndereço completo do arquivo: \n📂 " + local + nomeArquivo + ".pdf"
                    + "\n\n❤ Obrigado por utilizar nosso sistema ❤!",
                    nomeArquivo
            );
            Util.mostrarMSG(msg);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
            Util.mostrarErro("Não foi possível gerar PDF!");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class BackgroundImage extends PdfPageEventHelper {

        private Image background;

        public BackgroundImage(String imagePath) throws IOException, DocumentException {
            background = Image.getInstance(imagePath);
            background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());  // Ajusta para o tamanho da página
            background.setAbsolutePosition(0, 0);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfContentByte canvas = writer.getDirectContentUnder();
                canvas.addImage(background); // Adiciona a imagem de fundo
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    class FooterEvent extends PdfPageEventHelper {

        private String textoFooter;

        public FooterEvent(String textoFooter) {
            this.textoFooter = textoFooter;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            float xPosition = (document.right() - document.left()) / 2 + document.left();
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                    new Paragraph(textoFooter), xPosition, document.bottom(), 0);
        }
    }
}
