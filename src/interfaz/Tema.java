package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public final class Tema {

    private Tema() {
    }

    public static final Color FONDO = new Color(22, 22, 30);
    public static final Color FONDO_PANEL = new Color(30, 30, 40);
    public static final Color FONDO_TABLA = new Color(26, 26, 35);
    public static final Color FONDO_TABLA_ALT = new Color(32, 32, 43);
    public static final Color FONDO_SELECCION = new Color(90, 70, 170);

    public static final Color ACENTO = new Color(124, 92, 255);
    public static final Color TEXTO = new Color(235, 235, 245);
    public static final Color TEXTO_SECUNDARIO = new Color(150, 150, 165);
    public static final Color BORDE = new Color(55, 55, 70);

    public static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FUENTE_NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUENA = new Font("SansSerif", Font.PLAIN, 11);

    public static JButton botonPrimario(String texto) {
        JButton boton = new JButton(texto);
        estilizarBoton(boton, ACENTO, Color.WHITE);
        return boton;
    }

    public static JButton botonSecundario(String texto) {
        JButton boton = new JButton(texto);
        estilizarBoton(boton, FONDO_PANEL, TEXTO);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(7, 15, 7, 15)));
        return boton;
    }

    private static void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFocusPainted(false);
        boton.setFont(FUENTE_NORMAL);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color hover = fondo.brighter();
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }

    public static ImageIcon cargarImagen(String ruta, int ancho, int alto) {
        if (ruta == null || ruta.isBlank()) {
            return null;
        }
        try {
            ImageIcon original = new ImageIcon(ruta);
            if (original.getIconWidth() <= 0) {
                return null;
            }
            Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(escalada);
        } catch (Exception e) {
            return null;
        }
    }

    public static JLabel portadaPlaceholder(int lado, float tamanoFuente) {
        JLabel lbl = new JLabel("\u266A", SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(lado, lado));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(50, 50, 65));
        lbl.setForeground(TEXTO_SECUNDARIO);
        lbl.setFont(lbl.getFont().deriveFont(tamanoFuente));
        lbl.setBorder(BorderFactory.createLineBorder(BORDE));
        return lbl;
    }
}