package interfaz;

import modelo.*;
import estructuras.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final List<Cancion> biblioteca = new ArrayList<>();

    private final BibliotecaPanel panelBiblioteca = new BibliotecaPanel();
    private final ReproductorPanel panelReproductor = new ReproductorPanel();

    // 0 = Aleatorio (lista circular), 1 = Por orden de llegada (cola), 2 = Alfabético (árbol)
    private int modoActual = 0;

    // Solo una de estas tres está "activa" según el modo seleccionado.
    private ModoAleatorio modoAleatorio;
    private ColaSimple<Cancion> cola;
    private BST<Cancion> arbol;

    public VentanaPrincipal() {
        super("Reproductor EIA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Tema.FONDO);

        add(panelBiblioteca, BorderLayout.CENTER);
        add(panelReproductor, BorderLayout.SOUTH);

        panelBiblioteca.setListener(new BibliotecaListener() {

            @Override
            public void onAgregar(Cancion nueva) {
                biblioteca.add(nueva);
                refrescarBiblioteca();
                reconstruirEstructuraModo();
            }

            @Override
            public void onEditar(Cancion original, Cancion editada) {
                int indice = biblioteca.indexOf(original);
                if (indice >= 0) {
                    biblioteca.set(indice, editada);
                }
                refrescarBiblioteca();
                reconstruirEstructuraModo();
            }

            @Override
            public void onEliminar(Cancion cancion) {
                biblioteca.remove(cancion);
                refrescarBiblioteca();
                reconstruirEstructuraModo();
            }

            @Override
            public void onBuscar(String texto) {
                if (texto.isEmpty()) {
                    panelBiblioteca.mostrarCanciones(biblioteca);
                    return;
                }
                String textoLower = texto.toLowerCase();
                List<Cancion> resultado = new ArrayList<>();
                for (Cancion c : biblioteca) {
                    if (c.getNombre().toLowerCase().contains(textoLower)
                            || c.getArtista().toLowerCase().contains(textoLower)) {
                        resultado.add(c);
                    }
                }
                panelBiblioteca.mostrarCanciones(resultado);
            }

            @Override
            public void onModoCambiado(int indiceModo) {
                modoActual = indiceModo;
                reconstruirEstructuraModo();
            }

            @Override
            public void onSeleccionCancion(Cancion cancion) {
                // Espacio libre por si luego quieren previsualizar datos sin reproducir.
            }
        });

        panelReproductor.setListener(new ReproductorListener() {

            @Override
            public void onSiguiente() {
                avanzarSegunModo();
            }

            @Override
            public void onAnterior() {
                retrocederSegunModo();
            }

            @Override
            public void onCalificar(Cancion cancion, int calificacion) {
                cancion.setCalificacion(calificacion);
                refrescarBiblioteca();
            }
        });

        cargarDatosDePrueba();
        reconstruirEstructuraModo();
    }

    private void refrescarBiblioteca() {
        panelBiblioteca.mostrarCanciones(biblioteca);
    }


    private void reconstruirEstructuraModo() {

        switch (modoActual) {

            case 0 -> {
                modoAleatorio = new ModoAleatorio();

                modoAleatorio.cargarCanciones(biblioteca);

                Cancion actual = modoAleatorio.actual();
                boolean hay = actual != null;

                panelReproductor.mostrarCancion(actual);
                panelReproductor.habilitarAnterior(hay);
                panelReproductor.habilitarSiguiente(hay);
            }

            case 1 -> {
                cola = new ColaSimple<>();
                for (Cancion c : biblioteca) {
                    cola.enqueue(c);
                }
                panelReproductor.mostrarCancion(cola.peek());
                panelReproductor.habilitarAnterior(false); // la cola (FIFO) no permite retroceder
                panelReproductor.habilitarSiguiente(!cola.isEmpty());
            }

            case 2 -> {
                arbol = new BST<>(Comparator.comparing(Cancion::getNombre));
                for (Cancion c : biblioteca) {
                    arbol.insertar(c);
                }
                Cancion primera = arbol.iniciar();
                panelReproductor.mostrarCancion(primera);
                panelReproductor.habilitarAnterior(arbol.puedeRetroceder());
                panelReproductor.habilitarSiguiente(arbol.puedeAvanzar());
            }
        }
    }

    private void avanzarSegunModo() {

        switch (modoActual) {

            case 0 -> {
                if (modoAleatorio == null || modoAleatorio.isEmpty()) return;

                Cancion siguiente = modoAleatorio.siguiente();

                panelReproductor.mostrarCancion(siguiente);
            }

            case 1 -> {
                if (cola == null || cola.isEmpty()) return;

                cola.dequeue();

                panelReproductor.mostrarCancion(cola.peek());
                panelReproductor.habilitarSiguiente(!cola.isEmpty());
            }

            case 2 -> {
                if (arbol == null) return;

                Cancion siguiente = arbol.avanzar();

                panelReproductor.mostrarCancion(siguiente);
                panelReproductor.habilitarAnterior(arbol.puedeRetroceder());
                panelReproductor.habilitarSiguiente(arbol.puedeAvanzar());
            }
        }
    }

    private void retrocederSegunModo() {

        switch (modoActual) {

            case 0 -> {
                if (modoAleatorio == null || modoAleatorio.isEmpty()) return;

                Cancion anterior = modoAleatorio.anterior();

                panelReproductor.mostrarCancion(anterior);
            }

            case 1 -> {
                // No aplica: la cola simple no permite regresar.
            }

            case 2 -> {
                if (arbol == null) return;

                Cancion anterior = arbol.retroceder();

                panelReproductor.mostrarCancion(anterior);
                panelReproductor.habilitarAnterior(arbol.puedeRetroceder());
                panelReproductor.habilitarSiguiente(arbol.puedeAvanzar());
            }
        }
    }

    private void cargarDatosDePrueba() {

        biblioteca.add(new Cancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354, "Rock", 1975, 95,"portadas/ANightAtTheOpera.png"));
        biblioteca.add(new Cancion("Yesterday", "The Beatles", "Help!", 125, "Pop", 1965, 90,"portadas/Help.png"));
        biblioteca.add(new Cancion("Billie Jean", "Michael Jackson", "Thriller", 294, "Pop", 1982, 92,"portadas/BillieJean.png"));
        biblioteca.add(new Cancion("Creep", "Radiohead", "Pablo Honey", 238, "Alternative", 1992, 88,"portadas/Radiohead.png"));

        biblioteca.add(new Cancion("Hotel California", "Eagles", "Hotel California", 391, "Rock", 1976, 94,"portadas/HotelCalifornia.png"));
        biblioteca.add(new Cancion("Imagine", "John Lennon", "Imagine", 187, "Pop", 1971, 93,"portadas/Imagine.png"));
        biblioteca.add(new Cancion("Smells Like Teen Spirit", "Nirvana", "Nevermind", 301, "Grunge", 1991, 91,"portadas/Grunge.png"));
        biblioteca.add(new Cancion("Sweet Child O' Mine", "Guns N' Roses", "Appetite for Destruction", 356, "Rock", 1987, 90,"portadas/GNR.png"));
        biblioteca.add(new Cancion("Like a Rolling Stone", "Bob Dylan", "Highway 61 Revisited", 369, "Rock", 1965, 89,"portadas/BobDylan.png"));
        biblioteca.add(new Cancion("Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", 482, "Rock", 1971, 96,"portadas/LedZeppelin.png"));
        biblioteca.add(new Cancion("Wonderwall", "Oasis", "(What's the Story) Morning Glory?", 259, "Britpop", 1995, 87,"portadas/Oasis.png"));
        biblioteca.add(new Cancion("Under Pressure", "Queen", "Hot Space", 248, "Rock", 1981, 91,"portadas/Queen.png"));
        biblioteca.add(new Cancion("Purple Haze", "Jimi Hendrix", "Are You Experienced", 170, "Rock", 1967, 88,"portadas/JimiHendrix.png"));
        biblioteca.add(new Cancion("Dream On", "Aerosmith", "Aerosmith", 267, "Rock", 1973, 89,"portadas/Aerosmith.png"));
        biblioteca.add(new Cancion("Losing My Religion", "R.E.M.", "Out of Time", 268, "Alternative", 1991, 86,"portadas/LosingMyReligion.png"));
        biblioteca.add(new Cancion("Take On Me", "a-ha", "Hunting High and Low", 225, "Pop", 1985, 90,"portadas/HuntingHighAndLow.png"));
        biblioteca.add(new Cancion("Africa", "Toto", "Toto IV", 295, "Rock", 1982, 89,"portadas/TotoIV.png"));
        biblioteca.add(new Cancion("Everybody Wants to Rule the World", "Tears for Fears", "Songs from the Big Chair", 251, "Pop", 1985, 88,"portadas/Tears.png"));
        biblioteca.add(new Cancion("The Sound of Silence", "Simon & Garfunkel", "Sounds of Silence", 222, "Folk", 1966, 87,"portadas/SoS.png"));
        biblioteca.add(new Cancion("Heroes", "David Bowie", "Heroes", 371, "Rock", 1977, 92,"portadas/Heroes.png"));
        biblioteca.add(new Cancion("Paint It, Black", "The Rolling Stones", "Aftermath", 202, "Rock", 1966, 91,"portadas/PaintIt.png"));
        biblioteca.add(new Cancion("Space Oddity", "David Bowie", "David Bowie", 318, "Rock", 1969, 90,"portadas/DavidBowie.png"));

        refrescarBiblioteca();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}