import java.util.List;
import java.util.Scanner;

public class MeniuText {

    private Scanner scanner;
    private BookSearch bookSearch;

    public MeniuText(BookSearch bookSearch) {
        scanner = new Scanner(System.in);
        this.bookSearch = bookSearch;
    }

    public void afisareMeniu() {
        while (true) {
            System.out.println("Meniu");
            System.out.println("1. Afisare cărți");
            System.out.println("2. Cautare carte după titlu (CAUTARE LINIARĂ)");
            System.out.println("3. Cautare carte după autor (CAUTARE LINIARĂ)");
            System.out.println("4. Cautare carte după titlu sau autor (CAUTARE LINIARĂ)");
            System.out.println("5. Cautare carte după titlu (CAUTARE BINARĂ)");
            System.out.println("6. Cautare carte după autor (CAUTARE BINARĂ])");
            System.out.println("7. Iesire");
            System.out.println("------------------------------");

            System.out.print("Alege o opțiune: ");
            int  optiune;
            try {
                optiune = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opțiune invalidă");
                continue;
            }

            switch (optiune) {
                case 1 -> afisareListaCarti();
                case 2 -> cautareDupaTitluLiniara();
                case 3 -> cautareDupaAutorLiniara();
                case 4 -> cautareDupaTitluOrAutorLiniara();
                case 5 -> cautareDupaTitluBinara();
                case 6 -> cautareDupaAutorBinara();
                case 7 -> {
                    System.out.println("Iesire...");
                    return;
                }

                default -> System.out.println("Opțiune invalidă");
            }
        }
    }

    private void afisareListaCarti() {
        List<Book> books = bookSearch.getBooks();
        afisareListaCarti(books);
    }

    private void cautareDupaTitluLiniara() {
        System.out.print("Introdu titlul cărții: ");
        String title = scanner.nextLine();
        List<Book> foundBooks = bookSearch.cautareLiniara(title, BookSearch.TipCautare.TITLU);
        afisareListaCarti(foundBooks);
    }

    private void cautareDupaAutorLiniara() {
        System.out.print("Introdu autorul cărții: ");
        String author = scanner.nextLine();
        List<Book> foundBooks = bookSearch.cautareLiniara(author, BookSearch.TipCautare.AUTOR);
        afisareListaCarti(foundBooks);
    }

    private void cautareDupaTitluOrAutorLiniara() {
        System.out.print("Introdu titlul sau autorul cărții: ");

        String titleOrAuthor = scanner.nextLine();
        List<Book> foundBooks = bookSearch.cautareLiniara(titleOrAuthor, BookSearch.TipCautare.TITLU_SAU_AUTOR);

        afisareListaCarti(foundBooks);
    }

    private void cautareDupaTitluBinara() {
        System.out.print("Introdu titlul cărții: ");

        String title = scanner.nextLine();
        Book foundBook = bookSearch.cautareBinara(title, BookSearch.TipCautare.TITLU);

        afisareListaCarti(List.of(foundBook));
    }

    private void cautareDupaAutorBinara() {
        System.out.print("Introdu autorul cărții: ");

        String author = scanner.nextLine();
        Book foundBook = bookSearch.cautareBinara(author, BookSearch.TipCautare.AUTOR);

        afisareListaCarti(List.of(foundBook));
    }

    public void afisareListaCarti(List<Book> books) {

        if (books.size() == 0) {
            System.out.println("Nu s-au găsit cărți.");
            return;
        }

        int maxTitleLength = "Titlu".length();
        int maxAuthorLength = "Autor".length();
        int maxCopiesLength = "Exemplare".length();

        // Determină lățimea maximă pentru fiecare coloană
        for (Book book : books) {
            if (book.getTitle().length() > maxTitleLength) {
                maxTitleLength = book.getTitle().length();
            }
            if (book.getAuthor().length() > maxAuthorLength) {
                maxAuthorLength = book.getAuthor().length();
            }
            if (String.valueOf(book.getCopies()).length() > maxCopiesLength) {
                maxCopiesLength = String.valueOf(book.getCopies()).length();
            }
        }
        System.out.println("\nCarti gasite: " + books.size());

        // Antetul tabelului
        String headerFormat = "%-" + maxTitleLength + "s | %-" + maxAuthorLength + "s | %-" + maxCopiesLength + "s%n";
        System.out.printf(headerFormat, "Titlu", "Autor", "Exemplare");
        for (int i = 0; i < maxTitleLength + maxAuthorLength + maxCopiesLength + 6; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Listarea fiecărei cărți
        String rowFormat = "%-" + maxTitleLength + "s | %-" + maxAuthorLength + "s | %" + maxCopiesLength + "d%n";
        for (Book book : books) {
            System.out.printf(rowFormat, book.getTitle(), book.getAuthor(), book.getCopies());
        }
        System.out.println();
    }
}