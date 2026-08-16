package estructuras;

public class NodoArbol<E> {

    E valor;
    NodoArbol<E> izquierdo;
    NodoArbol<E> derecho;
    NodoArbol<E> padre;

    public NodoArbol(E valor) {
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
        this.padre = null;
    }
}