import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Impressora {
    private static final int AVANCO_CORTE = 5;
    private static final int TAMANHO_QRCODE = 4;
    private static final int NIVEL_CORRECAO_QRCODE = 2;
    private static final int TIPO_BARRAS = 8;
    private static final int ALTURA_BARRAS = 100;
    private static final int LARGURA_BARRAS = 2;
    private static final int HRI_BARRAS = 2;
    private static final String DADOS_BARRAS = "{A012345678912";
    private static final int PINO_GAVETA = 0;
    private static final int TEMPO_INICIAL_GAVETA = 5;
    private static final int TEMPO_FINAL_GAVETA = 10;
    private static final String ARQUIVO_XML_SAT = "XMLSAT.xml";
    private static final String ARQUIVO_XML_CANCELAMENTO = "CANC_SAT.xml";

    private static final String ASSINATURA_QRCODE_CANCELAMENTO =
            "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0" +
            "E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQ" +
            "sE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+Z" +
            "fO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

    private int tipo;
    private String modelo;
    private String conexao;
    private int parametro;
    private boolean conexaoAberta;

    public void configurarConexao(Scanner sc) {
        tipo = lerInteiro(sc, "USB == 1: ", 1, 4);
        System.out.print("i9/i8: ");
        modelo = sc.nextLine().trim();
        System.out.print("USB/COM2/IP: ");
        conexao = sc.nextLine().trim();
        parametro = lerInteiro(sc, "USB == 0, serial == velocidade, rede == porta: ", 0, 999999);
        System.out.println("Salvo");
    }

    public void abrirConexao() {
        try {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
            if (retorno == 0) {
                conexaoAberta = true;
                System.out.println("Conectado.");
            } else {
                System.out.println("Falha ao conectar. Código: " + retorno);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro na DLL: " + e.getMessage());
        }
    }

    public void fecharConexao() {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
        if (retorno == 0) {
            conexaoAberta = false;
            System.out.println("Desconectado.");
        } else {
            System.out.println("Falha ao desconectar. Código: " + retorno);
        }
    }

    public void imprimirTexto(Scanner sc) {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        System.out.print("Texto: ");
        String texto = sc.nextLine();
        int posicao = 0;
        int estilo = 0;
        int tamanho = 0;
        int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(texto, posicao, estilo, tamanho);
        if (retorno >= 0) {
            System.out.println("Texto OK. Bytes: " + retorno);
            cortarPapel();
        } else {
            System.out.println("Erro no texto. Código: " + retorno);
        }
    }

    public void imprimirQRCode(Scanner sc) {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        System.out.print("Conteúdo do QR Code: ");
        String dados = sc.nextLine();
        int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(
                dados, TAMANHO_QRCODE, NIVEL_CORRECAO_QRCODE);
        if (retorno == 0) {
            System.out.println("QR Code OK.");
            cortarPapel();
        } else {
            System.out.println("Erro no QR Code. Código: " + retorno);
        }
    }

    public void imprimirCodigoBarras() {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(
                TIPO_BARRAS, DADOS_BARRAS, ALTURA_BARRAS, LARGURA_BARRAS, HRI_BARRAS);
        if (retorno == 0) {
            System.out.println("Código de barras OK.");
            cortarPapel();
        } else {
            System.out.println("Erro no código de barras. Código: " + retorno);
        }
    }

    public void imprimirXmlSat() {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        try {
            String conteudo = Files.readString(Paths.get(ARQUIVO_XML_SAT));
            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(conteudo, 0);
            if (retorno == 0) {
                System.out.println("Cupom SAT OK.");
                cortarPapel();
            } else {
                System.out.println("Erro no SAT. Código: " + retorno);
            }
        } catch (IOException e) {
            System.out.println("XML não encontrado: " + ARQUIVO_XML_SAT);
        }
    }

    public void imprimirXmlCancelamento() {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        try {
            String conteudo = Files.readString(Paths.get(ARQUIVO_XML_CANCELAMENTO));
            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(conteudo, ASSINATURA_QRCODE_CANCELAMENTO, 0);
            if (retorno == 0) {
                System.out.println("Cancelamento OK.");
                cortarPapel();
            } else {
                System.out.println("Erro no cancelamento. Código: " + retorno);
            }
        } catch (IOException e) {
            System.out.println("XML não encontrado: " + ARQUIVO_XML_CANCELAMENTO);
        }
    }

    private void cortarPapel() {
        int retorno = ImpressoraDLL.INSTANCE.Corte(AVANCO_CORTE);
        if (retorno == 0) {
            System.out.println("Papel cortado.");
        } else {
            System.out.println("Erro ao cortar o papel. Código: " + retorno);
        }
    }

    public void abrirGaveta() {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(
                PINO_GAVETA, TEMPO_INICIAL_GAVETA, TEMPO_FINAL_GAVETA);
        if (retorno == 0) {
            System.out.println("Gaveta OK.");
        } else {
            System.out.println("Erro na gaveta. Código: " + retorno);
        }
    }

    public void sinalSonoro(Scanner sc) {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        int quantidade = lerInteiro(sc, "Bips (1-63): ", 1, 63);
        int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(quantidade, 50, 5);
        if (retorno == 0) {
            System.out.println("Sinal OK.");
        } else {
            System.out.println("Erro no sinal. Código: " + retorno);
        }
    }

    public void avancarPapel(Scanner sc) {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        int linhas = lerInteiro(sc, "Linhas (1-100): ", 1, 100);
        int retorno = ImpressoraDLL.INSTANCE.AvancaPapel(linhas);
        if (retorno == 0) {
            System.out.println("Papel avançado.");
        } else {
            System.out.println("Erro ao avançar. Código: " + retorno);
        }
    }

    public void verStatus(Scanner sc) {
        if (!conexaoAberta) {
            avisoConexaoFechada();
            return;
        }
        int param = lerOpcao(sc, "Status (1 gaveta, 2 tampa, 3 papel, 5 geral): ", 1, 2, 3, 5);
        int retorno = ImpressoraDLL.INSTANCE.StatusImpressora(param);
        if (param == 3) {
            switch (retorno) {
                case 5 -> System.out.println("Papel OK.");
                case 6 -> System.out.println("Papel acabando.");
                case 7 -> System.out.println("Sem papel.");
                default -> System.out.println("Código: " + retorno);
            }
        } else {
            System.out.println("Código: " + retorno);
        }
    }

    private void avisoConexaoFechada() {
        System.out.println("Sem conexão. Conecte a impressora.");
    }

    private int lerInteiro(Scanner sc, String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine();
            try {
                int valor = Integer.parseInt(entrada.trim());
                if (valor < min || valor > max) {
                    System.out.println("Use um valor de " + min + " a " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    private int lerOpcao(Scanner sc, String mensagem, int... validos) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine();
            try {
                int valor = Integer.parseInt(entrada.trim());
                for (int v : validos) {
                    if (v == valor) {
                        return valor;
                    }
                }
                System.out.println("Opção inválida.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }
}
