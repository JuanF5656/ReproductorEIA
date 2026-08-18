package modelo;

import estructuras.ListaLigadaCircularDoble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModoAleatorio implements ModoReproduccion {

    private ListaLigadaCircularDoble<Cancion> lista;

    public ModoAleatorio() {
        lista = new ListaLigadaCircularDoble<>();
    }

    public void cargarCanciones(List<Cancion> canciones) {

        // Crear una copia para no modificar la biblioteca original
        List<Cancion> cancionesMezcladas =
                new ArrayList<>(canciones);

        // Mezclar aleatoriamente
        Collections.shuffle(cancionesMezcladas);

        // Introducirlas en la lista circular
        for (Cancion cancion : cancionesMezcladas) {
            lista.add(cancion);
        }
    }

    @Override
    public Cancion siguiente() {

        if (lista.isEmpty()) {
            return null;
        }

        return lista.siguiente();
    }

    @Override
    public Cancion anterior() {

        if (lista.isEmpty()) {
            return null;
        }

        return lista.anterior();
    }

    public void agregar(Cancion cancion) {
        lista.add(cancion);
    }

    public Cancion actual() {
        return lista.obtenerActual();
    }

    public void eliminarActual() {
        lista.eliminarActual();
    }
}