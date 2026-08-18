package servicios;

import modelo.Cancion;

import java.util.ArrayList;
import java.util.List;


public class Biblioteca {

    private final List<Cancion> canciones = new ArrayList<>();

    public void agregar(Cancion cancion) {
        canciones.add(cancion);
    }

    public boolean eliminar(Cancion cancion) {
        return canciones.remove(cancion);
    }

    public boolean editar(Cancion original, Cancion editada) {
        int indice = canciones.indexOf(original);
        if (indice < 0) {
            return false;
        }
        canciones.set(indice, editada);
        return true;
    }

    public List<Cancion> buscar(String texto) {

        if (texto == null || texto.isBlank()) {
            return listar();
        }

        String textoLower = texto.toLowerCase();
        List<Cancion> resultado = new ArrayList<>();

        for (Cancion c : canciones) {
            if (c.getNombre().toLowerCase().contains(textoLower)
                    || c.getArtista().toLowerCase().contains(textoLower)) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    public void calificar(Cancion cancion, int calificacion) {
        cancion.setCalificacion(calificacion);
    }

    public List<Cancion> listar() {
        return canciones;
    }

    public int size() {
        return canciones.size();
    }

    public boolean isEmpty() {
        return canciones.isEmpty();
    }
}