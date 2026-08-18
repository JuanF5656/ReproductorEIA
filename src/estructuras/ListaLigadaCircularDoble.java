package estructuras;

public class ListaLigadaCircularDoble<E> {
    private NodoDLL<E> cabeza;
    private NodoDLL<E> actual;

    public ListaLigadaCircularDoble() {
        cabeza = null;
        actual = null;
    }

    public boolean isEmpty() {
        return cabeza == null;
    }

    public void add(E valor) {

        NodoDLL<E> newNode = new NodoDLL<>(valor);

        if (isEmpty()) {

            cabeza = newNode;
            actual = newNode;

            newNode.siguiente = newNode;
            newNode.anterior = newNode;

        } else {

            NodoDLL<E> last = cabeza.anterior;

            newNode.siguiente = cabeza;
            newNode.anterior = last;

            last.siguiente = newNode;
            cabeza.anterior = newNode;
        }
    }
    public void mostrar() {

        if (isEmpty()) {
            System.out.println("Lista vacía");
            return;
        }

        NodoDLL<E> nodo = cabeza;

        do {
            System.out.println(nodo.valor);
            nodo = nodo.siguiente;
        } while (nodo != cabeza);
    }

    public E siguiente() {

        if (isEmpty()) {
            return null;
        }

        actual = actual.siguiente;

        return actual.valor;
    }

    public E anterior() {

        if (isEmpty()) {
            return null;
        }

        actual = actual.anterior;

        return actual.valor;
    }

    public E obtenerActual() {

        if (isEmpty()) {
            return null;
        }

        return actual.valor;
    }

    public void eliminarActual() {

        if (isEmpty()) {
            return;
        }

        if (actual.siguiente == actual) {

            cabeza = null;
            actual = null;

            return;
        }

        NodoDLL<E> anterior = actual.anterior;
        NodoDLL<E> siguiente = actual.siguiente;

        anterior.siguiente = siguiente;
        siguiente.anterior = anterior;

        if (actual == cabeza) {
            cabeza = siguiente;
        }


        actual = siguiente;
    }

}
