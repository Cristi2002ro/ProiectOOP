import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost", 8080);
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            while (true) {
                afisareMeniu();
                System.out.print("Alege o optiune: ");
                int optiuneaAleasa = 0;
                try {
                    optiuneaAleasa = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Optiune invalida. Te rog sa alegi o cifra.");
                }

                switch (optiuneaAleasa) {
                    case 1, 2, 3, 4, 5, 6 -> {
                        String comanda = procesareComanda(optiuneaAleasa, scanner);
                        System.out.println("Comanda trimisa: " + comanda);
                        trimitereComanda(comanda, bufferedWriter);
                        procesareRaspuns(bufferedReader);
                    }
                    case 7-> {
                        trimitereComanda("EXIT", bufferedWriter);
                        return;
                    }
                    default -> System.out.println("Optiune invalida. Te rog sa alegi din nou.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void afisareMeniu() {
        System.out.println("Meniu Biblioteca");
        System.out.println("1. Listare carti");
        System.out.println("2. Cautare dupa titlu (metoda liniara)");
        System.out.println("3. Cautare dupa autor (metoda liniara)");
        System.out.println("4. Cautare dupa titlu sau autor (metoda liniara)");
        System.out.println("5. Cautare dupa titlu (metoda binara)");
        System.out.println("6. Cautare dupa autor (metoda binara)");
        System.out.println("7. Iesire");
        System.out.println("------------------------------");
    }


    private static String procesareComanda(int choice, Scanner scanner) {
        if (choice == 1) {
            return "GET_ALL_BOOKS";
        } else {
            System.out.print("Introdu titlul sau autorul cartii: ");
            String input = scanner.nextLine();
            return switch (choice) {
                case 2 -> "LINEAR_SEARCH_BY_TITLE:" + input;
                case 3 -> "LINEAR_SEARCH_BY_AUTHOR:" + input;
                case 4 -> "LINEAR_SEARCH_BY_TITLE_OR_AUTHOR:" + input;
                case 5 -> "BINARY_SEARCH_BY_TITLE:" + input;
                case 6 -> "BINARY_SEARCH_BY_AUTHOR:" + input;
                default -> "";
            };
        }
    }

    private static void trimitereComanda(String command, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(command);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private static void procesareRaspuns(BufferedReader bufferedReader) throws IOException {
        StringBuilder fullResponse = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            fullResponse.append(line).append("\n");
        }
        System.out.println("Raspuns de la server:\n" + fullResponse);
    }
}
