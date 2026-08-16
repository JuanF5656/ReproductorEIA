package interfaz;
import modelo.*;
import estructuras.*;
import javax.swing.*;
import java.awt.*;

public class DialogoCancion extends JDialog {

    private final JTextField campoNombre = new JTextField();
    private final JTextField campoArtista = new JTextField();
    private final JTextField campoAlbum = new JTextField();
    private final JTextField campoDuracion = new JTextField();
    private final JTextField campoGenero = new JTextField();
    private final JTextField campoAnno = new JTextField();
    private final JSlider sliderCalificacion = new JSlider(0, 100, 50);
    private final JLabel lblCalificacion = new JLabel();

    private Cancion resultado;

    public DialogoCancion(Window propietario, Cancion existente) {
        super(propietario, existente == null ? "Agregar canción" : "Editar canción",
                ModalityType.APPLICATION_MODAL);

        setSize(380, 420);
        setLocationRelativeTo(propietario);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 10));
        form.add(new JLabel("Nombre:"));
        form.add(campoNombre);
        form.add(new JLabel("Artista:"));
        form.add(campoArtista);
        form.add(new JLabel("Álbum:"));
        form.add(campoAlbum);
        form.add(new JLabel("Duración (segundos):"));
        form.add(campoDuracion);
        form.add(new JLabel("Género:"));
        form.add(campoGenero);
        form.add(new JLabel("Año de lanzamiento:"));
        form.add(campoAnno);

        actualizarLabelCalificacion();
        sliderCalificacion.addChangeListener(e -> actualizarLabelCalificacion());

        JPanel panelCalificacion = new JPanel(new BorderLayout(8, 0));
        panelCalificacion.add(lblCalificacion, BorderLayout.WEST);
        panelCalificacion.add(sliderCalificacion, BorderLayout.CENTER);
        panelCalificacion.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(form, BorderLayout.NORTH);
        centro.add(panelCalificacion, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        if (existente != null) {
            campoNombre.setText(existente.getNombre());
            campoArtista.setText(existente.getArtista());
            campoAlbum.setText(existente.getAlbum());
            campoDuracion.setText(String.valueOf(existente.getDuracion()));
            campoGenero.setText(existente.getGenero());
            campoAnno.setText(String.valueOf(existente.getAnno()));
            sliderCalificacion.setValue(existente.getCalificacion());
            actualizarLabelCalificacion();
        }

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void actualizarLabelCalificacion() {
        lblCalificacion.setText("Calificación: " + sliderCalificacion.getValue());
    }

    private void guardar() {
        try {
            String nombre = campoNombre.getText().trim();
            String artista = campoArtista.getText().trim();
            String album = campoAlbum.getText().trim();
            String genero = campoGenero.getText().trim();
            int duracion = Integer.parseInt(campoDuracion.getText().trim());
            int anno = Integer.parseInt(campoAnno.getText().trim());
            int calificacion = sliderCalificacion.getValue();

            if (nombre.isEmpty() || artista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y artista son obligatorios.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            resultado = new Cancion(nombre, artista, album, duracion, genero, anno, calificacion);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duración y año deben ser números enteros.",
                    "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Devuelve la canción creada/editada, o null si el usuario canceló. */
    public Cancion getResultado() {
        return resultado;
    }
}
