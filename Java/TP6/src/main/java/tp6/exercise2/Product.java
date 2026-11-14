package tp6.exercise2;

public class Product {
    private final int code;
    private final String nom;
    private final double prixAchat;
    private final double prixVente;

    public Product(
            int code,
            String nom,
            double prixAchat,
            double prixVente) {

        this.code = code;
        this.nom = nom;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
    }
     public int getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public double getPrixVente() {
        return prixVente;
    }
}