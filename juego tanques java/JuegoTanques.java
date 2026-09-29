import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class JuegoTanques extends JFrame {

    private JPanel panelMenu;
    private PanelJuego panelJuego;

    public JuegoTanques() {
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

        btnLocal.addActionListener(e -> pedirDatosEIniciarLocal());

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

    private void pedirDatosEIniciarLocal() {
        // Campos para el Jugador 1
        JTextField txtNombre1 = new JTextField("Jugador 1", 10);
        Color[] color1 = {Color.RED};
        JButton btnColor1 = new JButton("   ");
        btnColor1.setBackground(color1[0]);
        btnColor1.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Selecciona Color Jugador 1", color1[0]);
            if (nuevo != null) {
                color1[0] = nuevo;
                btnColor1.setBackground(nuevo);
            }
        });

        // Campos para el Jugador 2
        JTextField txtNombre2 = new JTextField("Jugador 2", 10);
        Color[] color2 = {new Color(0, 150, 0)};
        JButton btnColor2 = new JButton("   ");
        btnColor2.setBackground(color2[0]);
        btnColor2.addActionListener(e -> {
            Color nuevo = JColorChooser.showDialog(this, "Selecciona Color Jugador 2", color2[0]);
            if (nuevo != null) {
                color2[0] = nuevo;
                btnColor2.setBackground(nuevo);
            }
        });

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Jugador 1
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Nombre Jugador 1 (WASD + Espacio):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNombre1, gbc);
        gbc.gridx = 2;
        panelForm.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3;
        panelForm.add(btnColor1, gbc);

        // Jugador 2
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Nombre Jugador 2 (Flechas + M/Enter):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNombre2, gbc);
        gbc.gridx = 2;
        panelForm.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3;
        panelForm.add(btnColor2, gbc);

        int resultado = JOptionPane.showConfirmDialog(
                this, 
                panelForm, 
                "Configuración de Partida Local", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            String n1 = txtNombre1.getText().trim().isEmpty() ? "Jugador 1" : txtNombre1.getText().trim();
            String n2 = txtNombre2.getText().trim().isEmpty() ? "Jugador 2" : txtNombre2.getText().trim();

            iniciarModoLocal(n1, color1[0], n2, color2[0]);
        }
    }

    private void iniciarModoLocal(String nombre1, Color color1, String nombre2, Color color2) {
        remove(panelMenu);

        panelJuego = new PanelJuego(this, nombre1, color1, nombre2, color2);
        add(panelJuego, BorderLayout.CENTER);

        pack();
        revalidate();
        repaint();

        // Enfocar el panel de juego para capturar controles de teclado
        panelJuego.requestFocusInWindow();
    }

    private void iniciarModoEnLinea() {
        JOptionPane.showMessageDialog(this, 
            "Modo en línea seleccionado. ¡Listo para configurar red (Sockets/Servidor)!", 
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
            JuegoTanques ventana = new JuegoTanques();
            ventana.setVisible(true);
        });
    }
}