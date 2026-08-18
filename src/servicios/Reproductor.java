package servicios;

import modelo.*;

public class Reproductor {

    public enum TipoModo { ALEATORIO, LLEGADA, ALFABETICO }

    private final Biblioteca biblioteca;

    private ModoReproduccion modoActual;
    private TipoModo tipoActual;
    private Cancion cancionActual;

    public Reproductor(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        cambiarModo(TipoModo.ALEATORIO);
    }

    public void cambiarModo(TipoModo tipo) {

        this.tipoActual = tipo;

        switch (tipo) {

            case ALEATORIO -> {
                ModoAleatorio modo = new ModoAleatorio();
                for (Cancion c : biblioteca.listar()) {
                    modo.agregar(c);
                }
                modoActual = modo;
                cancionActual = modo.actual();
            }

            case LLEGADA -> {
                ModoOrdenLlegada modo = new ModoOrdenLlegada();
                for (Cancion c : biblioteca.listar()) {
                    modo.agregar(c);
                }
                modoActual = modo;
                cancionActual = modo.actual();
            }

            case ALFABETICO -> {
                ModoAlfabetico modo = new ModoAlfabetico();
                for (Cancion c : biblioteca.listar()) {
                    modo.agregar(c);
                }
                modoActual = modo;
                cancionActual = modo.actual();
            }
        }
    }


    public void refrescar() {
        cambiarModo(tipoActual);
    }

    // ---- A partir de aquí,  es polimórfico: solo se habla con ModoReproduccion ----

    public Cancion siguiente() {
        if (modoActual == null) {
            return null;
        }
        cancionActual = modoActual.siguiente();
        return cancionActual;
    }

    public Cancion anterior() {
        if (modoActual == null) {
            return null;
        }
        cancionActual = modoActual.anterior();
        return cancionActual;
    }

    public Cancion obtenerCancionActual() {
        return cancionActual;
    }

    public TipoModo getTipoActual() {
        return tipoActual;
    }
}