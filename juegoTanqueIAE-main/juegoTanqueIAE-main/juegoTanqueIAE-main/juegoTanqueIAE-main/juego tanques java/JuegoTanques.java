import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
        panelMenu.setPreferredSize(new Dimension(850, 600)); // Ampliado para el panel lateral de puntajes
        panelMenu.setBackground(new Color(30, 30, 30));
        panelMenu.setLayout(new BorderLayout()); // Usamos BorderLayout para dividir pantalla

        // --- 1. PANEL LATERAL DE PUNTAJES (Derecha) ---
        JPanel panelPuntajes = new JPanel(new BorderLayout());
        panelPuntajes.setPreferredSize(new Dimension(250, 600));
        panelPuntajes.setBackground(new Color(40, 40, 40));
        panelPuntajes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTituloPuntajes = new JLabel("MEJORES PUNTAJES", SwingConstants.CENTER);
        lblTituloPuntajes.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloPuntajes.setForeground(Color.WHITE);
        panelPuntajes.add(lblTituloPuntajes, BorderLayout.NORTH);

        JTextArea areaPuntajes = new JTextArea();
        areaPuntajes.setEditable(false);
        areaPuntajes.setBackground(new Color(40, 40, 40));
        areaPuntajes.setForeground(Color.GREEN);
        areaPuntajes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Cargamos los puntajes desde el GestorPuntajes
        areaPuntajes.setText(GestorPuntajes.obtenerPuntajesTexto());

        JScrollPane scrollPuntajes = new JScrollPane(areaPuntajes);
        scrollPuntajes.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        panelPuntajes.add(scrollPuntajes, BorderLayout.CENTER);

        panelMenu.add(panelPuntajes, BorderLayout.EAST);

        // --- 2. PANEL CENTRAL (Menú Principal Original) ---
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(30, 30, 30));
        panelCentral.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del Juego
        JLabel lblTitulo = new JLabel("COMBATE DE TANQUES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panelCentral.add(lblTitulo, gbc);

        // Subtítulo / Instrucción
        JLabel lblSub = new JLabel("Selecciona el modo de juego", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSub.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        panelCentral.add(lblSub, gbc);

        // Botón Jugar Local
        JButton btnLocal = crearBotonMenu("MODO LOCAL");
        gbc.gridy = 2;
        panelCentral.add(btnLocal, gbc);
        btnLocal.addActionListener(e -> pedirDatosEIniciarLocal());

        // Botón Jugar En Línea
        JButton btnEnLinea = crearBotonMenu("MODO EN LÍNEA");
        gbc.gridy = 3;
        panelCentral.add(btnEnLinea, gbc);
        btnEnLinea.addActionListener(e -> iniciarModoEnLinea());

        panelMenu.add(panelCentral, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(280, 45));
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
        panelForm.add(new JLabel("Primer Jugador (WASD + Enter):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtNombre1, gbc);
        gbc.gridx = 2;
        panelForm.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3;
        panelForm.add(btnColor1, gbc);

        // Jugador 2
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Segundo Jugador (Flechas + Espacio):"), gbc);
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

        // Recreamos el menú para que se actualice la lista de puntajes al volver
        crearMenuInicio();

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