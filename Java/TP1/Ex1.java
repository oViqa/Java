public class Ex1 {
	public static double aireRectangle(double largeur, double hauteur) {
		return largeur * hauteur;
	}
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		System.out.print("Entrez la largeur : ");
		double largeur = scanner.nextDouble();
		System.out.print("Entrez la hauteur : ");
		double hauteur = scanner.nextDouble();
		System.out.println("L'aire du rectangle est : " + aireRectangle(largeur, hauteur));
		scanner.close();
	}
}
