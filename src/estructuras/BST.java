package estructuras;
import java.util.Comparator;

public class BST <E> {

    private NodoArbol<E> raiz;
    private NodoArbol<E> actual;
    private int tamano;
    private final Comparator<E> comparador;

    public BST(Comparator<E> comparador) {
        this.comparador = comparador;
        this.raiz = null;
        this.actual = null;
        this.tamano = 0;
    }

    public boolean isEmpty() {
        return raiz == null;
    }

    public int size() {
        return tamano;
    }

    // Insertar
    public void insertar(E valor) {

        NodoArbol<E> nuevo = new NodoArbol<>(valor);

        if (isEmpty()) {
            raiz = nuevo;
            actual = nuevo;
            tamano++;
            return;
        }

        NodoArbol<E> nodoActual = raiz;
        NodoArbol<E> padre = null;

        while (nodoActual != null) {
            padre = nodoActual;
            if (comparador.compare(valor, nodoActual.valor) < 0) {
                nodoActual = nodoActual.izquierdo;
            } else {
                nodoActual = nodoActual.derecho;
            }
        }

        nuevo.padre = padre;

        if (comparador.compare(valor, padre.valor) < 0) {
            padre.izquierdo = nuevo;
        } else {
            padre.derecho = nuevo;
        }

        tamano++;
    }


    public E buscar(E valor) {
        NodoArbol<E> nodo = buscarNodo(valor);
        return nodo == null ? null : nodo.valor;
    }

    public boolean contiene(E valor) {
        return buscarNodo(valor) != null;
    }

    private NodoArbol<E> buscarNodo(E valor) {

        NodoArbol<E> nodo = raiz;

        while (nodo != null) {
            int cmp = comparador.compare(valor, nodo.valor);

            if (cmp == 0) {
                return nodo;
            }

            nodo = cmp < 0 ? nodo.izquierdo : nodo.derecho;
        }

        return null;
    }

 // eliminar

    public boolean eliminar(E valor) {

        NodoArbol<E> nodo = buscarNodo(valor);

        if (nodo == null) {
            return false;
        }

        // Si el nodo que se elimina es el que se está reproduciendo,
        // movemos el puntero "actual" antes de borrarlo para no perder la posición.
        if (nodo == actual) {
            NodoArbol<E> siguiente = sucesor(nodo);
            actual = (siguiente != null) ? siguiente : predecesor(nodo);
        }

        eliminarNodo(nodo);
        tamano--;
        return true;
    }

    private void eliminarNodo(NodoArbol<E> nodo) {

        // Caso 1: nodo hoja (sin hijos)
        if (nodo.izquierdo == null && nodo.derecho == null) {
            reemplazarEnPadre(nodo, null);

            // Caso 2: un solo hijo
        } else if (nodo.izquierdo == null) {
            reemplazarEnPadre(nodo, nodo.derecho);
        } else if (nodo.derecho == null) {
            reemplazarEnPadre(nodo, nodo.izquierdo);


        } else {
            NodoArbol<E> sucesor = minimo(nodo.derecho);
            nodo.valor = sucesor.valor;
            eliminarNodo(sucesor);
        }
    }

    private void reemplazarEnPadre(NodoArbol<E> nodo, NodoArbol<E> hijo) {

        if (hijo != null) {
            hijo.padre = nodo.padre;
        }

        if (nodo.padre == null) {
            raiz = hijo;
        } else if (nodo == nodo.padre.izquierdo) {
            nodo.padre.izquierdo = hijo;
        } else {
            nodo.padre.derecho = hijo;
        }
    }


    // Navegación simulando el recorrido inorden Avanzar o Retroceder



    public E iniciar() {
        if (isEmpty()) {
            return null;
        }
        actual = minimo(raiz);
        return actual.valor;
    }

    public E actualElemento() {
        return actual == null ? null : actual.valor;
    }

    public boolean puedeAvanzar() {
        return actual != null && sucesor(actual) != null;
    }

    public boolean puedeRetroceder() {
        return actual != null && predecesor(actual) != null;
    }

    public E avanzar() {

        if (actual == null) {
            return iniciar();
        }

        NodoArbol<E> siguiente = sucesor(actual);

        if (siguiente != null) {
            actual = siguiente;
        }

        return actual.valor;
    }

    public E retroceder() {

        if (actual == null) {
            return null;
        }

        NodoArbol<E> anterior = predecesor(actual);

        if (anterior != null) {
            actual = anterior;
        }

        return actual.valor;
    }

    // Sucesor inorden de un nodo: si tiene subárbol derecho, es el mínimo
    // de ese subárbol; si no, es el primer ancestro del que "nodo" cuelga
    // por la izquierda.
    private NodoArbol<E> sucesor(NodoArbol<E> nodo) {

        if (nodo.derecho != null) {
            return minimo(nodo.derecho);
        }

        NodoArbol<E> padre = nodo.padre;

        while (padre != null && nodo == padre.derecho) {
            nodo = padre;
            padre = padre.padre;
        }

        return padre;
    }

    // Predecesor inorden: simétrico al sucesor.
    private NodoArbol<E> predecesor(NodoArbol<E> nodo) {

        if (nodo.izquierdo != null) {
            return maximo(nodo.izquierdo);
        }

        NodoArbol<E> padre = nodo.padre;

        while (padre != null && nodo == padre.izquierdo) {
            nodo = padre;
            padre = padre.padre;
        }

        return padre;
    }

    private NodoArbol<E> minimo(NodoArbol<E> nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    private NodoArbol<E> maximo(NodoArbol<E> nodo) {
        while (nodo.derecho != null) {
            nodo = nodo.derecho;
        }
        return nodo;
    }
}
