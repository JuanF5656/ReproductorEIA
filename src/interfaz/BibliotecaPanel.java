package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import modelo.*;
import estructuras.*;

public class BibliotecaPanel extends JPanel {

    private static final String[] COLUMNAS = {"Portada", "Nombre", "Artista", "Álbum", "Género", "Año", "Calificación"};

    private static final String[] MODOS = {
            "Aleatorio (Lista circular doble)",
            "Por orden de llegada (Cola)",
            "Alfabético (Árbol binario)"
    };

    private static final int ALTO_FILA = 54;
    private static final int LADO_MINIATURA = 42;

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
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel titulo = new JLabel("Biblioteca de canciones");
        titulo.setFont(Tema.FUENTE_TITULO);
        titulo.setForeground(Tema.TEXTO);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(ALTO_FILA);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(Tema.FONDO_TABLA);
        tabla.setForeground(Tema.TEXTO);
        tabla.setSelectionBackground(Tema.FONDO_SELECCION);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(Tema.FUENTE_NORMAL);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setBackground(Tema.FONDO_PANEL);
        tabla.getTableHeader().setForeground(Tema.TEXTO);
        tabla.getTableHeader().setFont(Tema.FUENTE_SUBTITULO);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setMaxWidth(70);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new MiniaturaRenderer());

        TableCellRenderer rendererFilas = new FilaAlternaRenderer();
        for (int i = 1; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(rendererFilas);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE));
        scroll.getViewport().setBackground(Tema.FONDO_TABLA);

        campoBusqueda = new JTextField();
        campoBusqueda.setBackground(Tema.FONDO_PANEL);
        campoBusqueda.setForeground(Tema.TEXTO);
        campoBusqueda.setCaretColor(Tema.TEXTO);
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        btnBuscar = Tema.botonSecundario("Buscar");
        JPanel panelBusqueda = new JPanel(new BorderLayout(6, 0));
        panelBusqueda.setOpaque(false);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        selectorModo = new JComboBox<>(MODOS);
        selectorModo.setBackground(Tema.FONDO_PANEL);
        selectorModo.setForeground(Tema.TEXTO);
        selectorModo.setFont(Tema.FUENTE_NORMAL);

        JPanel panelSuperior = new JPanel(new BorderLayout(14, 10));
        panelSuperior.setOpaque(false);
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelFiltros = new JPanel(new BorderLayout(10, 0));
        panelFiltros.setOpaque(false);
        panelFiltros.add(panelBusqueda, BorderLayout.CENTER);
        panelFiltros.add(selectorModo, BorderLayout.EAST);
        panelSuperior.add(panelFiltros, BorderLayout.SOUTH);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        btnAgregar = Tema.botonPrimario("+ Agregar");
        btnEditar = Tema.botonSecundario("Editar");
        btnEliminar = Tema.botonSecundario("Eliminar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setOpaque(false);
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

    public void mostrarCanciones(List<Cancion> lista) {
        this.canciones = lista;
        modeloTabla.setRowCount(0);
        for (Cancion c : lista) {
            modeloTabla.addRow(new Object[]{
                    null, c.getNombre(), c.getArtista(), c.getAlbum(),
                    c.getGenero(), c.getAnno(), c.getCalificacion()
            });
        }
    }

    public void setListener(BibliotecaListener listener) {
        this.listener = listener;
    }
    private class MiniaturaRenderer extends JLabel implements TableCellRenderer {

        MiniaturaRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            setBackground(isSelected ? Tema.FONDO_SELECCION : filaColor(row));

            if (canciones != null && row < canciones.size()) {
                ImageIcon icono = Tema.cargarImagen(canciones.get(row).getRutaPortada(), LADO_MINIATURA, LADO_MINIATURA);
                if (icono != null) {
                    setIcon(icono);
                    setText(null);
                } else {
                    setIcon(null);
                    setText("\u266A");
                    setForeground(Tema.TEXTO_SECUNDARIO);
                    setFont(getFont().deriveFont(16f));
                }
            }

            return this;
        }
    }

    private class FilaAlternaRenderer extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setForeground(isSelected ? Color.WHITE : Tema.TEXTO);
            c.setBackground(isSelected ? Tema.FONDO_SELECCION : filaColor(row));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            return c;
        }
    }

    private static Color filaColor(int row) {
        return row % 2 == 0 ? Tema.FONDO_TABLA : Tema.FONDO_TABLA_ALT;
    }
}