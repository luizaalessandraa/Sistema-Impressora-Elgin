import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Usuario usuarioValido = new Usuario("lulu", "1234");
        Impressora impressora = new Impressora();

        if (!fazerLogin(sc, usuarioValido)) {
            sc.close();
            return;
        }

        boolean sair = false;
        while (!sair) {
            mostrarMenu();
            int opcao = lerOpcaoMenu(sc);
            switch (opcao) {
                case 1 -> impressora.configurarConexao(sc);
                case 2 -> impressora.abrirConexao();
                case 3 -> impressora.fecharConexao();
                case 4 -> impressora.imprimirTexto(sc);
                case 5 -> impressora.imprimirQRCode(sc);
                case 6 -> impressora.imprimirCodigoBarras();
                case 7 -> impressora.imprimirXmlSat();
                case 8 -> impressora.imprimirXmlCancelamento();
                case 9 -> impressora.abrirGaveta();
                case 10 -> impressora.sinalSonoro(sc);
                case 11 -> impressora.avancarPapel(sc);
                case 12 -> impressora.verStatus(sc);
                case 0 -> sair = true;
                default -> System.out.println("Opção inválida.");
            }
        }

        System.out.println("Programa encerrado.");
        sc.close();
    }

    private static boolean fazerLogin(Scanner sc, Usuario usuarioValido) {
        int tentativas = 3;
        for (int i = 1; i <= tentativas; i++) {
            System.out.print("Login: ");
            String usuario = sc.nextLine();
            System.out.print("Senha: ");
            String senha = sc.nextLine();
            if (usuarioValido.validar(usuario, senha)) {
                System.out.println("Login OK.");
                return true;
            }
            int restantes = tentativas - i;
            if (restantes > 0) {
                System.out.println("Login inválido. Restam: " + restantes);
            }
        }
        System.out.println("Tentativas esgotadas. Encerrando.");
        return false;
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=== MENU ELGIN ===");
        System.out.println("1  - Configurar");
        System.out.println("2  - Conectar");
        System.out.println("3  - Desconectar");
        System.out.println("4  - Texto");
        System.out.println("5  - QR Code");
        System.out.println("6  - Código de barras");
        System.out.println("7  - XML SAT");
        System.out.println("8  - Cancelamento SAT");
        System.out.println("9  - Gaveta");
        System.out.println("10 - Sinal sonoro");
        System.out.println("11 - Avançar papel");
        System.out.println("12 - Status");
        System.out.println("0  - Sair");
        System.out.print("Opção: ");
    }

    private static int lerOpcaoMenu(Scanner sc) {
        while (true) {
            String entrada = sc.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número: ");
            }
        }
    }
}
