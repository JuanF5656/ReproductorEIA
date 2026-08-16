package interfaz;

import modelo.Cancion;


public interface ReproductorListener {

    void onSiguiente();

    void onAnterior();

    void onCalificar(Cancion cancion, int calificacion);
}