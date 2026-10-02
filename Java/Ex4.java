import java.util.Scanner;

public class Ex4 {
	public static boolean estPair(int nombre) {
		return nombre % 2 == 0;
	}
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Entrez un nombre entier : ");
		int nombre = scanner.nextInt();
		if (estPair(nombre)) {
			System.out.println("Le nombre est pair.");
		} else {
			System.out.println("Le nombre est impair.");
		}
		scanner.close();
	}
}
