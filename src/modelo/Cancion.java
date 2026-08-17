package modelo;
import java.util.Objects;

public class Cancion{

    private String nombre;
    private String artista;
    private String album;
    private int duracion; // en segundos
    private String genero;
    private int anno;
    private int calificacion; // 0 a 100
    private String rutaPortada; // ruta/URL de la imagen de portada (puede ser null)

    public Cancion(String nombre, String artista, String album, int duracion,
                   String genero, int anno, int calificacion) {
        this(nombre, artista, album, duracion, genero, anno, calificacion, null);
    }

    public Cancion(String nombre, String artista, String album, int duracion,
                   String genero, int anno, int calificacion, String rutaPortada) {

        if (calificacion < 0 || calificacion > 100) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 100");
        }
        if (duracion < 0) {
            throw new IllegalArgumentException("La duración no puede ser negativa");
        }

        this.nombre = nombre;
        this.artista = artista;
        this.album = album;
        this.duracion = duracion;
        this.genero = genero;
        this.anno = anno;
        this.calificacion = calificacion;
        this.rutaPortada = rutaPortada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        if (duracion < 0) {
            throw new IllegalArgumentException("La duración no puede ser negativa");
        }
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        if (calificacion < 0 || calificacion > 100) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 100");
        }
        this.calificacion = calificacion;
    }

    public String getRutaPortada() {
        return rutaPortada;
    }

    public void setRutaPortada(String rutaPortada) {
        this.rutaPortada = rutaPortada;
    }

    /** Duración formateada como m:ss, útil para mostrarla en la interfaz. */
    public String getDuracionFormateada() {
        int min = duracion / 60;
        int seg = duracion % 60;
        return String.format("%d:%02d", min, seg);
    }

    @Override
    public String toString() {
        return nombre + " - " + artista;
    }

    // Dos canciones se consideran la misma si coinciden nombre, artista y álbum.
    // (Las estructuras de reproducción del proyecto igual comparan por referencia;
    // esto sirve si en algún momento necesitas comparar por contenido, ej. al buscar.)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancion)) return false;
        Cancion otra = (Cancion) o;
        return Objects.equals(nombre, otra.nombre)
                && Objects.equals(artista, otra.artista)
                && Objects.equals(album, otra.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, artista, album);
    }
}