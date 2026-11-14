public class Main {
    public static void main(String[] args) {
        Article article = new Article("Guide Java", 120);
        Magazine magazine = new Magazine("Science & Vie", 84);

        article.Imprimer();
        magazine.Imprimer();

        AnimalCarnivor carnivor = new AnimalCarnivor("Lion");
        AnimalHerbivor herbivor = new AnimalHerbivor("Vache");

        carnivor.Manger();
        herbivor.Manger();
    }
}