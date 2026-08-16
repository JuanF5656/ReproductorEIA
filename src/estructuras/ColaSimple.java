package estructuras;

public class ColaSimple<E> {
    private Nodo<E> frente;
    private Nodo<E> atras;

    public ColaSimple() {
        frente = null;
        atras = null;
    }

    public boolean isEmpty() {
        return frente == null;
    }

    public void enqueue(E valor) {

        Nodo<E> newNode = new Nodo<>(valor);

        if (isEmpty()) {
            frente = newNode;
            atras = newNode;
        } else {
            atras.siguiente = newNode;
            atras = newNode;
        }
    }

    public E dequeue() {

        if (isEmpty()) {
            return null;
        }

        E valor = frente.valor;

        frente = frente.siguiente;

        if (frente == null) {
            atras = null;
        }

        return valor;
    }

    public E peek() {

        if (isEmpty()) {
            return null;
        }

        return frente.valor;
    }
}
