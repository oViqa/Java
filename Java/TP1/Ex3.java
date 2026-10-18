public class Ex3 {
	public static int calculerSomme(int a, int b, int c) {
		return a + b + c;
	}
	public static double calculerMoyenne(int a, int b, int c) {
		return calculerSomme(a, b, c) / 3.0;
	}
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		System.out.print("Entrez le premier entier : ");
		int a = scanner.nextInt();
		System.out.print("Entrez le deuxième entier : ");
		int b = scanner.nextInt();
		System.out.print("Entrez le troisième entier : ");
		int c = scanner.nextInt();
		System.out.println("Somme : " + calculerSomme(a, b, c));
		System.out.println("Moyenne : " + calculerMoyenne(a, b, c));
		scanner.close();
	}
}