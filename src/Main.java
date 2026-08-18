import modelo.Cancion;
import servicios.Biblioteca;
import servicios.Reproductor;
import servicios.Reproductor.TipoModo;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("       PRUEBA DEL PROYECTO");
        System.out.println("=================================\n");

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.agregar(new Cancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354, "Rock", 1975, 95));
        biblioteca.agregar(new Cancion("Yesterday", "The Beatles", "Help!", 125, "Pop", 1965, 90));
        biblioteca.agregar(new Cancion("Billie Jean", "Michael Jackson", "Thriller", 294, "Pop", 1982, 92));
        biblioteca.agregar(new Cancion("Creep", "Radiohead", "Pablo Honey", 238, "Alternative", 1992, 88));

        System.out.println("Biblioteca:");
        for (Cancion c : biblioteca.listar()) {
            System.out.println(" - " + c.getNombre() + " (" + c.getArtista() + ")");
        }

        Reproductor reproductor = new Reproductor(biblioteca);

        System.out.println("\n--- MODO ALEATORIO ---");
        reproductor.cambiarModo(TipoModo.ALEATORIO);
        imprimir("Actual", reproductor.obtenerCancionActual());
        imprimir("Siguiente", reproductor.siguiente());
        imprimir("Siguiente", reproductor.siguiente());
        imprimir("Anterior", reproductor.anterior());

        System.out.println("\n--- MODO LLEGADA ---");
        reproductor.cambiarModo(TipoModo.LLEGADA);
        imprimir("Actual", reproductor.obtenerCancionActual());
        imprimir("Siguiente", reproductor.siguiente());
        imprimir("Siguiente", reproductor.siguiente());
        imprimir("Siguiente", reproductor.siguiente());

        System.out.println("\n--- MODO ALFABÉTICO ---");
        reproductor.cambiarModo(TipoModo.ALFABETICO);
        imprimir("Actual", reproductor.obtenerCancionActual());
        imprimir("Siguiente", reproductor.siguiente());
        imprimir("Siguiente", reproductor.siguiente());
        imprimir("Anterior", reproductor.anterior());

        System.out.println("\n--- CAMBIO DE MODOS ---");
        System.out.println("Aleatorio -> Llegada -> Alfabético (probado arriba con el mismo objeto Reproductor)");
    }

    private static void imprimir(String etiqueta, Cancion cancion) {
        String texto = (cancion == null) ? "(ninguna)" : cancion.getNombre();
        System.out.println(etiqueta + ": " + texto);
    }
}