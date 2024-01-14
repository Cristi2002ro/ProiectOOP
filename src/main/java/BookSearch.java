import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookSearch {

    private List<Book> books;
    private List<Book> booksSortedByTitle;
    private List<Book> booksSortedByAuthor;
    public enum TipCautare {
        TITLU,
        AUTOR,
        TITLU_SAU_AUTOR
    }

    public BookSearch() {
        this.books = new ArrayList<>();
        this.booksSortedByTitle = new ArrayList<>();
        this.booksSortedByAuthor = new ArrayList<>();
        //initializare cititor carti
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/carti.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String title = line.trim();
                String author = reader.readLine().trim();
                int copies = Integer.parseInt(reader.readLine().trim());

                Book book = new Book(title, author, copies);
                books.add(book);
                booksSortedByTitle.add(book);
                booksSortedByAuthor.add(book);
            }

            Collections.sort(booksSortedByTitle, Comparator.comparing(Book::getTitle));
            Collections.sort(booksSortedByAuthor, Comparator.comparing(Book::getAuthor));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> cautareLiniara(String searchText, TipCautare tipCautare) {
        List<Book> foundBooks = new ArrayList<>();

        searchText = searchText.toLowerCase();

        for (Book book : books) {

            String bookAttribute = switch (tipCautare) {
                case TITLU -> book.getTitle().toLowerCase();
                case AUTOR -> book.getAuthor().toLowerCase();
                case TITLU_SAU_AUTOR -> book.getTitle().toLowerCase() + " " + book.getAuthor().toLowerCase();
            };

            if (bookAttribute.contains(searchText)) {
                foundBooks.add(book);
            }
        }

        return foundBooks;
    }

    public Book cautareBinara(String searchText, TipCautare tipCautare) {

        List<Book> searchList;

        switch (tipCautare) {
            case TITLU -> searchList = booksSortedByTitle;
            case AUTOR -> searchList = booksSortedByAuthor;
            default -> {
                return null;
            }
        }

        int low = 0;
        int high = searchList.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Book midBook = searchList.get(mid);
            String midAttribute = (tipCautare == TipCautare.TITLU) ? midBook.getTitle() : midBook.getAuthor();
            int result = midAttribute.compareToIgnoreCase(searchText);

            if (result == 0) {
                return midBook;
            } else if (result < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return null;
    }

    public List<Book> getBooks() {
        return books;
    }


}
