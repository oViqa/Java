public class ex2 {
    public static void main(String[] args) {
        Etudiant e = new Etudiant("AB123456", "Alami", 20);
        e.AfficheEtudiant();
        System.out.println();

        EtudiantFaculte ef = new EtudiantFaculte("CD789012", "Bennani", 21);
        ef.AfficheEtudiant();
        System.out.println();

        EtudiantENSA ee = new EtudiantENSA("EF345678", "Idrissi", 22);
        ee.AfficheEtudiant();
        System.out.println();

        // Test des setters
        ee.setNom("Idrissi Karim");
        ee.setAge(23);
        ee.AfficheEtudiant();
    }
}

class Etudiant {
    private String CIN;
    private String Nom;
    private int Age;

    public Etudiant() { }

    public Etudiant(String CIN, String Nom, int Age) {
        this.CIN = CIN;
        this.Nom = Nom;
        this.Age = Age;
    }

    // Lecture / écriture
    public String getCIN() { return CIN; }
    public void setCIN(String CIN) { this.CIN = CIN; }

    public String getNom() { return Nom; }
    public void setNom(String Nom) { this.Nom = Nom; }

    public int getAge() { return Age; }
    public void setAge(int Age) { this.Age = Age; }

    public void AfficheEtudiant() {
        System.out.println("CIN : " + CIN + "   Nom : " + Nom + "   Age : " + Age);
    }
}

class EtudiantFaculte extends Etudiant {
    public EtudiantFaculte(String CIN, String Nom, int Age) {
        super(CIN, Nom, Age);
    }

    @Override
    public void AfficheEtudiant() {
        System.out.println("Etudiant à la faculté :");
        System.out.println("CIN : " + getCIN() + "   Nom : " + getNom() + "   Age : " + getAge());
    }
}

class EtudiantENSA extends Etudiant {
    public EtudiantENSA(String CIN, String Nom, int Age) {
        super(CIN, Nom, Age);
    }

    @Override
    public void AfficheEtudiant() {
        System.out.println("Etudiant à l'ENSA :");
        System.out.println("CIN : " + getCIN() + "   Nom : " + getNom() + "   Age : " + getAge());
    }
}