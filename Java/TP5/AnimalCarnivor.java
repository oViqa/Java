public class AnimalCarnivor extends Animal {
    public AnimalCarnivor(String nom) {
        super(nom);
    }

    @Override
    public void Manger() {
        System.out.printf("« %s mange la viande »%n", getNom());
    }
}
