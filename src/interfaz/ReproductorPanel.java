package interfaz;
import modelo.*;
import estructuras.*;
import javax.swing.*;
import java.awt.*;

public class ReproductorPanel extends JPanel {

    private final JLabel lblPortada;
    private final JLabel lblNombre;
    private final JLabel lblArtistaAlbum;
    private final JLabel lblGeneroAnno;
    private final JProgressBar barraProgreso;
    private final JButton btnAnterior;
    private final JButton btnReproducir;
    private final JButton btnSiguiente;
    private final JSlider sliderCalificacion;
    private final JLabel lblCalificacionValor;

    private final Timer timerProgreso;
    private int segundoActual;
    private int duracionTotal;
    private boolean reproduciendo;

    private Cancion cancionActual;
    private ReproductorListener listener;

    public ReproductorPanel() {
        setLayout(new BorderLayout(12, 8));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        setPreferredSize(new Dimension(0, 170));

        lblPortada = new JLabel("♪", SwingConstants.CENTER);
        lblPortada.setPreferredSize(new Dimension(80, 80));
        lblPortada.setOpaque(true);
        lblPortada.setBackground(new Color(60, 60, 60));
        lblPortada.setForeground(Color.WHITE);
        lblPortada.setFont(lblPortada.getFont().deriveFont(28f));
        lblPortada.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        lblNombre = new JLabel("Sin reproducción");
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 16f));
        lblArtistaAlbum = new JLabel(" ");
        lblGeneroAnno = new JLabel(" ");
        lblGeneroAnno.setForeground(Color.GRAY);

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.add(lblNombre);
        panelInfo.add(lblArtistaAlbum);
        panelInfo.add(lblGeneroAnno);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(lblPortada, BorderLayout.WEST);
        panelIzquierdo.add(panelInfo, BorderLayout.CENTER);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setString("0:00 / 0:00");

        btnAnterior = new JButton("⏮ Anterior");
        btnReproducir = new JButton("▶ Reproducir");
        btnSiguiente = new JButton("Siguiente ⏭");

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        panelControles.setOpaque(false);
        panelControles.add(btnAnterior);
        panelControles.add(btnReproducir);
        panelControles.add(btnSiguiente);

        sliderCalificacion = new JSlider(0, 100, 0);
        lblCalificacionValor = new JLabel("Calificación: -");
        JPanel panelCalificacion = new JPanel(new BorderLayout(8, 0));
        panelCalificacion.setOpaque(false);
        panelCalificacion.add(lblCalificacionValor, BorderLayout.WEST);
        panelCalificacion.add(sliderCalificacion, BorderLayout.CENTER);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.add(barraProgreso);
        panelCentro.add(panelControles);
        panelCentro.add(panelCalificacion);

        add(panelIzquierdo, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);

        // Simula el avance de la canción sin reproducir audio real (1 tick = 1 segundo)
        timerProgreso = new Timer(1000, e -> avanzarProgreso());

        configurarEventos();
        actualizarBotones();
    }

    private void configurarEventos() {

        btnReproducir.addActionListener(e -> {
            if (cancionActual == null) return;
            if (reproduciendo) {
                pausar();
            } else {
                reproducir();
            }
        });

        btnSiguiente.addActionListener(e -> {
            if (listener != null) listener.onSiguiente();
        });

        btnAnterior.addActionListener(e -> {
            if (listener != null) listener.onAnterior();
        });

        sliderCalificacion.addChangeListener(e -> {
            lblCalificacionValor.setText("Calificación: " + sliderCalificacion.getValue());
            if (!sliderCalificacion.getValueIsAdjusting() && cancionActual != null && listener != null) {
                listener.onCalificar(cancionActual, sliderCalificacion.getValue());
            }
        });
    }

    /** Se llama desde el controlador cada vez que cambia la canción que se está reproduciendo. */
    public void mostrarCancion(Cancion cancion) {

        detener();
        this.cancionActual = cancion;

        if (cancion == null) {
            lblNombre.setText("Sin reproducción");
            lblArtistaAlbum.setText(" ");
            lblGeneroAnno.setText(" ");
            duracionTotal = 0;
            segundoActual = 0;
            barraProgreso.setValue(0);
            barraProgreso.setString("0:00 / 0:00");
            sliderCalificacion.setValue(0);
            actualizarBotones();
            return;
        }

        lblNombre.setText(cancion.getNombre());
        lblArtistaAlbum.setText(cancion.getArtista() + " — " + cancion.getAlbum());
        lblGeneroAnno.setText(cancion.getGenero() + " · " + cancion.getAnno());

        duracionTotal = Math.max(cancion.getDuracion(), 1);
        segundoActual = 0;
        barraProgreso.setMaximum(duracionTotal);
        barraProgreso.setValue(0);
        barraProgreso.setString("0:00 / " + formatoTiempo(duracionTotal));

        sliderCalificacion.setValue(cancion.getCalificacion());

        actualizarBotones();
    }

    public void habilitarAnterior(boolean habilitado) {
        btnAnterior.setEnabled(habilitado);
    }

    public void habilitarSiguiente(boolean habilitado) {
        btnSiguiente.setEnabled(habilitado);
    }

    private void reproducir() {
        reproduciendo = true;
        btnReproducir.setText("⏸ Pausar");
        timerProgreso.start();
    }

    private void pausar() {
        reproduciendo = false;
        btnReproducir.setText("▶ Reproducir");
        timerProgreso.stop();
    }

    private void detener() {
        reproduciendo = false;
        timerProgreso.stop();
        btnReproducir.setText("▶ Reproducir");
    }

    private void avanzarProgreso() {

        segundoActual++;

        if (segundoActual >= duracionTotal) {
            segundoActual = duracionTotal;
            barraProgreso.setValue(segundoActual);
            barraProgreso.setString(formatoTiempo(segundoActual) + " / " + formatoTiempo(duracionTotal));
            pausar();
            if (listener != null) {
                listener.onSiguiente(); // al terminar la canción, pasa automáticamente a la siguiente
            }
            return;
        }

        barraProgreso.setValue(segundoActual);
        barraProgreso.setString(formatoTiempo(segundoActual) + " / " + formatoTiempo(duracionTotal));
    }

    private void actualizarBotones() {
        boolean hayCancion = cancionActual != null;
        btnReproducir.setEnabled(hayCancion);
        sliderCalificacion.setEnabled(hayCancion);
    }

    private String formatoTiempo(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%d:%02d", min, seg);
    }

    public void setListener(ReproductorListener listener) {
        this.listener = listener;
    }
}