public class NodoDLL<E> {
    NodoDLL<E> siguiente;
    NodoDLL<E> anterior;
    E valor;

    public NodoDLL() {
    }

    public NodoDLL(E valor) {
        this.valor = valor;
        this.siguiente = null;
        this.anterior = null;
    }
}
