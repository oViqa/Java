public class Ex2 {
    public static double convertirEnFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Entrez la température en Celsius : ");
        double celsius = scanner.nextDouble();
        double fahrenheit = convertirEnFahrenheit(celsius);
        System.out.println("Température en Fahrenheit : " + fahrenheit);
        scanner.close();
    }
}