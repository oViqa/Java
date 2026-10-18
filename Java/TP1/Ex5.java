public class Ex5 {
    public static int calculerFactorielle(int n) {
        if (n < 0) {
            System.out.println("Le nombre doit être non négatif.");
        }
        int resultat = 1;
        for (int i = 1; i <= n; i++) {
            resultat = resultat * i;
        }
        return resultat;
    }
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Entrez un entier non négatif : ");
        int n = scanner.nextInt();
        int factorielle = calculerFactorielle(n);
        System.out.println("La factorielle de " + n + " est : " + factorielle);
        scanner.close();
    }
}

