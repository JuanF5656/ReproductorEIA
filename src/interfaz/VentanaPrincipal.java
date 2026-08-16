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
    private ListaLigadaCircularDoble<Cancion> listaCircular;
    private ColaSimple<Cancion> cola;
    private BST<Cancion> arbol;

    public VentanaPrincipal() {
        super("Reproductor EIA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

    /**
     * Reconstruye, a partir del contenido actual de "biblioteca", la estructura
     * de datos correspondiente al modo activo, y muestra la primera canción de esa estructura.
     */
    private void reconstruirEstructuraModo() {

        switch (modoActual) {

            case 0 -> {
                listaCircular = new ListaLigadaCircularDoble<>();
                for (Cancion c : biblioteca) {
                    listaCircular.add(c);
                }
                boolean hay = !listaCircular.isEmpty();
                panelReproductor.mostrarCancion(hay ? biblioteca.get(0) : null);
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
                if (listaCircular == null || listaCircular.isEmpty()) return;
                // TODO: agregar un método siguiente() en ListaLigadaCircularDoble que mueva
                // el puntero "actual" y lo devuelva (la lista es circular: nunca hay null).
            }

            case 1 -> {
                if (cola == null || cola.isEmpty()) return;
                cola.dequeue(); // la canción reproducida sale de la cola (FIFO)
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
                if (listaCircular == null || listaCircular.isEmpty()) return;
                // TODO: agregar un método anterior() en ListaLigadaCircularDoble (mismo caso que arriba).
            }

            case 1 -> {
                // No aplica: la cola simple no permite regresar a canciones anteriores.
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
        biblioteca.add(new Cancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354, "Rock", 1975, 95));
        biblioteca.add(new Cancion("Yesterday", "The Beatles", "Help!", 125, "Pop", 1965, 90));
        biblioteca.add(new Cancion("Billie Jean", "Michael Jackson", "Thriller", 294, "Pop", 1982, 92));
        biblioteca.add(new Cancion("Creep", "Radiohead", "Pablo Honey", 238, "Alternative", 1992, 88));
        refrescarBiblioteca();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}