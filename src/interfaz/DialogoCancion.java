package interfaz;

import modelo.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class DialogoCancion extends JDialog {

    private final JTextField campoNombre = new JTextField();
    private final JTextField campoArtista = new JTextField();
    private final JTextField campoAlbum = new JTextField();
    private final JTextField campoDuracion = new JTextField();
    private final JTextField campoGenero = new JTextField();
    private final JTextField campoAnno = new JTextField();
    private final JSlider sliderCalificacion = new JSlider(0, 100, 50);
    private final JLabel lblCalificacion = new JLabel();
    private final JLabel lblPreview;

    private String rutaPortadaSeleccionada;
    private Cancion resultado;

    public DialogoCancion(Window propietario, Cancion existente) {
        super(propietario, existente == null ? "Agregar canción" : "Editar canción",
                ModalityType.APPLICATION_MODAL);

        setSize(430, 500);
        setLocationRelativeTo(propietario);
        setLayout(new BorderLayout(14, 14));
        getContentPane().setBackground(Tema.FONDO);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // ---- Portada: vista previa + botón para elegir imagen ----
        lblPreview = new JLabel("\u266A", SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(110, 110));
        lblPreview.setMaximumSize(new Dimension(110, 110));
        lblPreview.setOpaque(true);
        lblPreview.setBackground(new Color(50, 50, 65));
        lblPreview.setForeground(Tema.TEXTO_SECUNDARIO);
        lblPreview.setFont(lblPreview.getFont().deriveFont(30f));
        lblPreview.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        lblPreview.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnElegirImagen = Tema.botonSecundario("Elegir portada...");
        btnElegirImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnElegirImagen.addActionListener(e -> elegirImagen());

        JPanel panelPortada = new JPanel();
        panelPortada.setOpaque(false);
        panelPortada.setLayout(new BoxLayout(panelPortada, BoxLayout.Y_AXIS));
        panelPortada.add(lblPreview);
        panelPortada.add(Box.createVerticalStrut(8));
        panelPortada.add(btnElegirImagen);
        panelPortada.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        // ---- Formulario ----
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 10));
        form.setOpaque(false);
        agregarCampo(form, "Nombre:", campoNombre);
        agregarCampo(form, "Artista:", campoArtista);
        agregarCampo(form, "Álbum:", campoAlbum);
        agregarCampo(form, "Duración (segundos):", campoDuracion);
        agregarCampo(form, "Género:", campoGenero);
        agregarCampo(form, "Año de lanzamiento:", campoAnno);

        actualizarLabelCalificacion();
        sliderCalificacion.addChangeListener(e -> actualizarLabelCalificacion());
        estilizarSlider(sliderCalificacion);
        lblCalificacion.setForeground(Tema.TEXTO);
        lblCalificacion.setFont(Tema.FUENTE_NORMAL);

        JPanel panelCalificacion = new JPanel(new BorderLayout(8, 0));
        panelCalificacion.setOpaque(false);
        panelCalificacion.add(lblCalificacion, BorderLayout.WEST);
        panelCalificacion.add(sliderCalificacion, BorderLayout.CENTER);
        panelCalificacion.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JButton btnGuardar = Tema.botonPrimario("Guardar");
        JButton btnCancelar = Tema.botonSecundario("Cancelar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setOpaque(false);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(panelPortada, BorderLayout.NORTH);
        centro.add(form, BorderLayout.CENTER);
        centro.add(panelCalificacion, BorderLayout.SOUTH);

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

            rutaPortadaSeleccionada = existente.getRutaPortada();
            actualizarPreview();
        }

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void agregarCampo(JPanel form, String etiqueta, JTextField campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Tema.TEXTO);
        lbl.setFont(Tema.FUENTE_NORMAL);

        campo.setBackground(Tema.FONDO_PANEL);
        campo.setForeground(Tema.TEXTO);
        campo.setCaretColor(Tema.TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        form.add(lbl);
        form.add(campo);
    }

    private void estilizarSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setForeground(Tema.TEXTO);
    }

    private void elegirImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Selecciona la portada de la canción");
        selector.setFileFilter(new FileNameExtensionFilter(
                "Imágenes (jpg, jpeg, png, gif)", "jpg", "jpeg", "png", "gif"));

        int seleccion = selector.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            rutaPortadaSeleccionada = archivo.getAbsolutePath();
            actualizarPreview();
        }
    }

    private void actualizarPreview() {
        ImageIcon icono = Tema.cargarImagen(rutaPortadaSeleccionada, 110, 110);
        if (icono != null) {
            lblPreview.setIcon(icono);
            lblPreview.setText(null);
        } else {
            lblPreview.setIcon(null);
            lblPreview.setText("\u266A");
        }
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

            resultado = new Cancion(nombre, artista, album, duracion, genero, anno,
                    calificacion, rutaPortadaSeleccionada);
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