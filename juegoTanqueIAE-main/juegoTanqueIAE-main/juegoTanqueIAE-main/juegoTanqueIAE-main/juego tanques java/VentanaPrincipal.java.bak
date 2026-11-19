import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class VentanaPrincipal extends JFrame {

    private JPanel panelMenu;
    private PanelJuego panelJuego;

    public VentanaPrincipal() {
        setTitle("Juego de Tanques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        crearMenuInicio();

        add(panelMenu);
        pack();
        setLocationRelativeTo(null);
    }

    private void crearMenuInicio() {
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(800, 600));
        panelMenu.setBackground(new Color(30, 30, 30));
        panelMenu.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del Juego
        JLabel lblTitulo = new JLabel("COMBATE DE TANQUES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panelMenu.add(lblTitulo, gbc);

        // Subtítulo / Instrucción
        JLabel lblSub = new JLabel("Selecciona el modo de juego", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 18));
        lblSub.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        panelMenu.add(lblSub, gbc);

        // Botón Jugar Local
        JButton btnLocal = crearBotonMenu("JUGAR EN LOCAL");
        gbc.gridy = 2;
        panelMenu.add(btnLocal, gbc);

        btnLocal.addActionListener(e -> iniciarModoLocal());

        // Botón Jugar En Línea
        JButton btnEnLinea = crearBotonMenu("JUGAR EN LÍNEA");
        gbc.gridy = 3;
        panelMenu.add(btnEnLinea, gbc);

        btnEnLinea.addActionListener(e -> iniciarModoEnLinea());
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(300, 50));
        return btn;
    }

    private void iniciarModoLocal() {
        remove(panelMenu);

        panelJuego = new PanelJuego(this);
        add(panelJuego, BorderLayout.CENTER);

        pack();
        revalidate();
        repaint();

        // Enfocar el panel de juego para capturar teclado
        panelJuego.requestFocusInWindow();
    }

    private void iniciarModoEnLinea() {
        JOptionPane.showMessageDialog(this, 
            "Modo en línea seleccionado. ¡Próximamente configuraremos el servidor y cliente!", 
            "Juego en Línea", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public void volverAlMenu() {
        if (panelJuego != null) {
            remove(panelJuego);
            panelJuego = null;
        }

        add(panelMenu);
        pack();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}