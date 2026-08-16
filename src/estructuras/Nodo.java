package estructuras;

public class Nodo<E> {

    Nodo<E> siguiente;
    Nodo<E> anterior;
    E valor;

    public Nodo() {
    }

    public Nodo(E valor) {
        this.valor = valor;
        this.siguiente = null;
        this.anterior = null;
    }
}
