public class Main {
    public static void main(String[] args) {
        BookSearch bookSearch = new BookSearch();
        MeniuText meniuText = new MeniuText(bookSearch);
        meniuText.afisareMeniu();
    }
}
