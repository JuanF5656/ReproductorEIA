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
}
