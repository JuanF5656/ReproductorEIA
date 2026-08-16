package interfaz;
import modelo.*;
import estructuras.*;

public interface BibliotecaListener {

    void onAgregar(Cancion nueva);

    void onEditar(Cancion original, Cancion editada);

    void onEliminar(Cancion cancion);

    void onBuscar(String texto);

    // indiceModo: 0 = Aleatorio, 1 = Por orden de llegada, 2 = Alfabético
    void onModoCambiado(int indiceModo);

    void onSeleccionCancion(Cancion cancion);
}