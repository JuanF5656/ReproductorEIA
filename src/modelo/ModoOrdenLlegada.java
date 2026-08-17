package modelo;

import estructuras.ColaSimple;

public class ModoOrdenLlegada implements ModoReproduccion {

    private ColaSimple<Cancion> cola;

    public ModoOrdenLlegada() {
        cola = new ColaSimple<>();
    }

    @Override
    public Cancion siguiente() {
        return cola.dequeue();
    }

    @Override
    public Cancion anterior() {
        return null;
    }

    public void agregar(Cancion cancion) {
        cola.enqueue(cancion);
    }

    public Cancion actual() {
        return cola.peek();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }
}
