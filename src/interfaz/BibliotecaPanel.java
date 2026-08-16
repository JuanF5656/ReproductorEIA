package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import modelo.*;
import estructuras.*;

public class BibliotecaPanel extends JPanel {

    private static final String[] COLUMNAS = {"Nombre", "Artista", "Álbum", "Género", "Año", "Calificación"};

    private static final String[] MODOS = {
            "Aleatorio (Lista circular doble)",
            "Por orden de llegada (Cola)",
            "Alfabético (Árbol binario)"
    };

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;
    private final JTextField campoBusqueda;
    private final JComboBox<String> selectorModo;
    private final JButton btnAgregar;
    private final JButton btnEditar;
    private final JButton btnEliminar;
    private final JButton btnBuscar;

    private List<Cancion> canciones; // referencia a la lista actualmente mostrada (puede ser un filtro de búsqueda)
    private BibliotecaListener listener;

    public BibliotecaPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Biblioteca de canciones"));

        campoBusqueda = new JTextField();
        btnBuscar = new JButton("Buscar");
        JPanel panelBusqueda = new JPanel(new BorderLayout(4, 0));
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        selectorModo = new JComboBox<>(MODOS);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        panelSuperior.add(selectorModo, BorderLayout.EAST);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        configurarEventos();
    }

    private void configurarEventos() {

        btnAgregar.addActionListener(e -> abrirDialogoCancion(null));

        btnEditar.addActionListener(e -> {
            Cancion seleccionada = obtenerCancionSeleccionada();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una canción para editar.");
                return;
            }
            abrirDialogoCancion(seleccionada);
        });

        btnEliminar.addActionListener(e -> {
            Cancion seleccionada = obtenerCancionSeleccionada();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una canción para eliminar.");
                return;
            }
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar \"" + seleccionada.getNombre() + "\"?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION && listener != null) {
                listener.onEliminar(seleccionada);
            }
        });

        btnBuscar.addActionListener(e -> {
            if (listener != null) {
                listener.onBuscar(campoBusqueda.getText().trim());
            }
        });

        campoBusqueda.addActionListener(e -> btnBuscar.doClick());

        selectorModo.addActionListener(e -> {
            if (listener != null) {
                listener.onModoCambiado(selectorModo.getSelectedIndex());
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listener != null) {
                Cancion seleccionada = obtenerCancionSeleccionada();
                if (seleccionada != null) {
                    listener.onSeleccionCancion(seleccionada);
                }
            }
        });
    }

    private void abrirDialogoCancion(Cancion existente) {

        Window ventana = SwingUtilities.getWindowAncestor(this);
        DialogoCancion dialogo = new DialogoCancion(ventana, existente);
        dialogo.setVisible(true);

        Cancion resultado = dialogo.getResultado();
        if (resultado == null || listener == null) {
            return;
        }

        if (existente == null) {
            listener.onAgregar(resultado);
        } else {
            listener.onEditar(existente, resultado);
        }
    }

    public Cancion obtenerCancionSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || canciones == null || fila >= canciones.size()) {
            return null;
        }
        return canciones.get(fila);
    }

    /** Refresca la tabla con la lista dada (biblioteca completa o resultado de una búsqueda). */
    public void mostrarCanciones(List<Cancion> lista) {
        this.canciones = lista;
        modeloTabla.setRowCount(0);
        for (Cancion c : lista) {
            modeloTabla.addRow(new Object[]{
                    c.getNombre(), c.getArtista(), c.getAlbum(),
                    c.getGenero(), c.getAnno(), c.getCalificacion()
            });
        }
    }

    public void setListener(BibliotecaListener listener) {
        this.listener = listener;
    }
}