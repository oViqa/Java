public class AnimalHerbivor extends Animal {
    public AnimalHerbivor(String nom) {
        super(nom);
    }

    @Override
    public void Manger() {
        System.out.printf("« %s mange l'herbe »%n", getNom());
    }
}
