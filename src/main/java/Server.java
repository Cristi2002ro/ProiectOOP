import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    private static final BookSearch bookSearch = new BookSearch();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Serverul ruleaza pe portul 8080");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client conectat.");

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    String input;
                    while ((input = bufferedReader.readLine()) != null) {
                        if (input.equalsIgnoreCase("EXIT")) {
                            System.out.println("Client deconectat");
                            break;
                        }

                        System.out.println("Am primit comanda: " + input);
                        procesareComanda(input, bufferedWriter);
                    }
                } catch (IOException e) {
                    System.err.println("Eroare la citirea de la client: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la pornirea serverului: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void procesareComanda(String command, BufferedWriter writer) throws IOException {
        String[] commandParts = command.split(":", 2);

        String comanda = commandParts[0];
        String argumentComanda = (commandParts.length > 1) ? commandParts[1] : "";

        switch (comanda) {
            case "GET_ALL_BOOKS" -> {
                List<Book> books = bookSearch.getBooks();
                trimitereRaspuns(books, writer);
            }
            case "LINEAR_SEARCH_BY_TITLE" -> {
                List<Book> books = bookSearch.cautareLiniara(argumentComanda, BookSearch.TipCautare.TITLU);
                trimitereRaspuns(books, writer);
            }
            case "LINEAR_SEARCH_BY_AUTHOR" -> {
                List<Book> books = bookSearch.cautareLiniara(argumentComanda, BookSearch.TipCautare.AUTOR);
                trimitereRaspuns(books, writer);
            }
            case "LINEAR_SEARCH_BY_TITLE_OR_AUTHOR" -> {
                List<Book> books = bookSearch.cautareLiniara(argumentComanda, BookSearch.TipCautare.TITLU_SAU_AUTOR);
                trimitereRaspuns(books, writer);
            }
            case "BINARY_SEARCH_BY_TITLE" -> {
                Book book = bookSearch.cautareBinara(argumentComanda, BookSearch.TipCautare.TITLU);
                trimitereRaspuns(List.of(book), writer);
            }
            case "BINARY_SEARCH_BY_AUTHOR" -> {
                Book book = bookSearch.cautareBinara(argumentComanda, BookSearch.TipCautare.AUTOR);
                trimitereRaspuns(List.of(book), writer);
            }
            default -> {
                writer.write("Comanda necunoscuta");
                writer.newLine();
                writer.flush();
            }
        }
    }

    private static void trimitereRaspuns(List<Book> books, BufferedWriter writer) throws IOException {
        if (books.isEmpty()) {
            writer.write("Nu s-au gasit carti sau lista de carti este goala.");
            writer.newLine();
        } else {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        }
        writer.newLine();
        writer.flush();
    }
}
