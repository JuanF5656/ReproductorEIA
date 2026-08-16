import modelo.Cancion;

public class Main {
    public static void main(String[] args) {
        Cancion cancion = new Cancion(
                "Bohemian Rhapsody",
                "Queen",
                "A Night at the Opera",
                354,
                "Rock",
                1975,
                95
        );

        System.out.println(cancion.getNombre());
        System.out.println(cancion.getArtista());
    }
}