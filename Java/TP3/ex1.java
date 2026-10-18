import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ex1 {
	public static void main(String[] args) {
		Marchandise m1 = new Marchandise("M001", "Ordinateur");
		Marchandise m2 = new Marchandise("M002");
		System.out.println(m1);
		System.out.println(m2);

		List<Utilisateur> utilisateurs = new ArrayList<>();
		utilisateurs.add(new Utilisateur(1, "hiba", "hiba", 6000,
				"admin", "1234"));
        utilisateurs.add(new Utilisateur(2, "sara", "sara", 5000, "sara", "abcd"));
		Scanner scanner = new Scanner(System.in);
		System.out.print("Login : ");
		String login = scanner.nextLine();
		System.out.print("Password : ");
		String password = scanner.nextLine();

		Utilisateur trouve = null;
		for (Utilisateur utilisateur : utilisateurs) {
			if (utilisateur.getLogin().equals(login)
					&& utilisateur.getPassword().equals(password)) {
				trouve = utilisateur;
				break;
			}
		}
		if (trouve != null) {
			System.out.println("Authentification réussie");
			trouve.afficheUtilisateur();
		} else {
			System.out.println("Echec d'Authentification");
		}
		scanner.close();
	}
}

class Marchandise {
	private String reference;
	private String designation;

	public Marchandise(String reference) {
		this(reference, null);
	}

	public Marchandise(String reference, String designation) {
		this.reference = reference;
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "Marchandise{reference='" + reference + "', designation='"
				+ designation + "'}";
	}
}

class Personne {
	private int id;
	private String nom;
	private String prenom;
	private double salaire;

	public Personne() { }

	public Personne(int id, String nom, String prenom, double salaire) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.salaire = salaire;
	}

	public Personne(Personne autre) {
		this(autre.id, autre.nom, autre.prenom, autre.salaire);
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNom() { return nom == null ? null : nom.toUpperCase(); }
	public void setNom(String nom) { this.nom = nom; }
	public String getPrenom() { return prenom == null ? null : prenom.toUpperCase(); }
	public void setPrenom(String prenom) { this.prenom = prenom; }
	public double getSalaire() { return salaire; }
	public void setSalaire(double salaire) { this.salaire = salaire; }

	public void affichePersonne() {
		System.out.println("ID : " + id + ", Nom : " + getNom()
				+ ", Prénom : " + getPrenom() + ", Salaire : " + salaire);
	}
}

class Utilisateur extends Personne {
	private String login;
	private String password;

	public Utilisateur() { }

	public Utilisateur(int id, String nom, String prenom, double salaire,
					   String login, String password) {
		super(id, nom, prenom, salaire);
		this.login = login;
		this.password = password;
	}

	public Utilisateur(Utilisateur autre) {
		this(autre.getId(), autre.getNom(), autre.getPrenom(), autre.getSalaire(),
				autre.login, autre.password);
	}

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public void afficheUtilisateur() {
		affichePersonne();
		System.out.println("Login : " + login + ", Password : " + password);
	}
}
