package modelo;

import estructuras.BST;

import java.util.Comparator;

public class ModoAlfabetico implements ModoReproduccion {

    private BST<Cancion> arbol;

    public ModoAlfabetico() {
        arbol = new BST<>(Comparator.comparing(Cancion::getNombre));
    }

    @Override
    public Cancion siguiente() {
        return arbol.avanzar();
    }

    @Override
    public Cancion anterior() {
        return arbol.retroceder();
    }

    public void agregar(Cancion cancion) {
        arbol.insertar(cancion);
    }

    public Cancion actual() {
        return arbol.actualElemento();
    }

    public void eliminarActual() {
        Cancion cancion = arbol.actualElemento();
        if (cancion != null) {
            arbol.eliminar(cancion);
        }
    }
}