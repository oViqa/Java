public class Article implements Imprimable {
    private String nom;
    private int nombreDePages;

    public Article(String nom, int nombreDePages) {
        setNom(nom);
        setNombreDePages(nombreDePages);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'article ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public int getNombreDePages() {
        return nombreDePages;
    }

    public void setNombreDePages(int nombreDePages) {
        if (nombreDePages < 0) {
            throw new IllegalArgumentException("Le nombre de pages ne peut pas être négatif.");
        }
        this.nombreDePages = nombreDePages;
    }

    @Override
    public void Imprimer() {
        System.out.printf("« %s est un Article imprimable sur %d pages »%n",
                nom, nombreDePages);
    }
}
