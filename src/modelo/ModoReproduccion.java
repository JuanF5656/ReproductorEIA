package modelo;

public interface ModoReproduccion {
    void agregar(Cancion cancion);

    Cancion siguiente();

    boolean puedeRetroceder();

    Cancion anterior();

}
