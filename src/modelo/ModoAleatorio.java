package modelo;

import estructuras.ListaLigadaCircularDoble;

public class ModoAleatorio implements ModoReproduccion {

    private ListaLigadaCircularDoble<Cancion> lista;

    public ModoAleatorio() {
        lista = new ListaLigadaCircularDoble<>();
    }

    @Override
    public Cancion siguiente() {
        return lista.siguiente();
    }

    @Override
    public Cancion anterior() {
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