import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PanelJuego extends JPanel implements ActionListener {

    private final int ANCHO_TABLERO = 600;
    private final int ALTO_TABLERO = 400;
    private final int MARGEN_X = 100;
    private final int MARGEN_Y = 50;

    private final int FILAS = 5;
    private final int COLUMNAS = 7;

    private final Timer timer;
    private final JuegoTanques ventanaPrincipal;

    private String nombreJugador1;
    private Color colorJugador1;
    private String nombreJugador2;
    private Color colorJugador2;

    private Tanque tanque1;
    private Tanque tanque2;

    private boolean tanque1Destruido = false;
    private boolean tanque2Destruido = false;

    private int puntos1 = 0;
    private int puntos2 = 0;

    private List<Bala> balas = new ArrayList<>();
    private List<Line2D> paredes = new ArrayList<>();
    private Set<Integer> teclasPresionadas = new HashSet<>();

    private Explosion explosionActual = null;
    private boolean enEsperaSiguienteRonda = false;
    private long tiempoInicioPausa = 0;
    private final long TIEMPO_ESPERA_MS = 3000;

    public PanelJuego(JuegoTanques ventanaPrincipal, String nombre1, Color color1, String nombre2, Color color2) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.nombreJugador1 = nombre1;
        this.colorJugador1 = color1;
        this.nombreJugador2 = nombre2;
        this.colorJugador2 = color2;

        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);

        reiniciarPartidaCompleta();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    timer.stop();
                    ventanaPrincipal.volverAlMenu();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
                if (!enEsperaSiguienteRonda) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        dispararTanque(tanque1);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_ENTER) {
                        dispararTanque(tanque2);
                    }
                }
            }
        });

        timer = new Timer(16, this);
        timer.start();
    }

    private void generarLaberintoAleatorio() {
        paredes.clear();

        paredes.add(new Line2D.Double(MARGEN_X, MARGEN_Y, MARGEN_X + ANCHO_TABLERO, MARGEN_Y));
        paredes.add(new Line2D.Double(MARGEN_X + ANCHO_TABLERO, MARGEN_Y, MARGEN_X + ANCHO_TABLERO, MARGEN_Y + ALTO_TABLERO));
        paredes.add(new Line2D.Double(MARGEN_X + ANCHO_TABLERO, MARGEN_Y + ALTO_TABLERO, MARGEN_X, MARGEN_Y + ALTO_TABLERO));
        paredes.add(new Line2D.Double(MARGEN_X, MARGEN_Y + ALTO_TABLERO, MARGEN_X, MARGEN_Y));

        double celdaAncho = (double) ANCHO_TABLERO / COLUMNAS;
        double celdaAlto = (double) ALTO_TABLERO / FILAS;

        boolean[][] visitado = new boolean[FILAS][COLUMNAS];
        List<ParedInterna> paredesExistentes = new ArrayList<>();

        for (int r = 0; r < FILAS; r++) {
            for (int c = 0; c < COLUMNAS; c++) {
                if (c < COLUMNAS - 1) {
                    paredesExistentes.add(new ParedInterna(r, c, r, c + 1, true));
                }
                if (r < FILAS - 1) {
                    paredesExistentes.add(new ParedInterna(r, c, r + 1, c, false));
                }
            }
        }

        Random rand = new Random();
        List<ParedInterna> caminoRemovido = new ArrayList<>();
        carveParedes(0, 0, visitado, paredesExistentes, caminoRemovido, rand);

        paredesExistentes.removeAll(caminoRemovido);

        Collections.shuffle(paredesExistentes, rand);
        int paredesAQuitar = (int) (paredesExistentes.size() * 0.25);
        for (int i = 0; i < paredesAQuitar; i++) {
            paredesExistentes.remove(0);
        }

        for (ParedInterna p : paredesExistentes) {
            if (p.esVertical) {
                double x = MARGEN_X + (p.c1 + 1) * celdaAncho;
                double y1 = MARGEN_Y + p.r1 * celdaAlto;
                double y2 = MARGEN_Y + (p.r1 + 1) * celdaAlto;
                paredes.add(new Line2D.Double(x, y1, x, y2));
            } else {
                double x1 = MARGEN_X + p.c1 * celdaAncho;
                double x2 = MARGEN_X + (p.c1 + 1) * celdaAncho;
                double y = MARGEN_Y + (p.r1 + 1) * celdaAlto;
                paredes.add(new Line2D.Double(x1, y, x2, y));
            }
        }
    }

    private void carveParedes(int r, int c, boolean[][] visitado, List<ParedInterna> todas, List<ParedInterna> removidas, Random rand) {
        visitado[r][c] = true;

        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        List<int[]> dirs = new ArrayList<>(List.of(direcciones));
        Collections.shuffle(dirs, rand);

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < FILAS && nc >= 0 && nc < COLUMNAS && !visitado[nr][nc]) {
                for (ParedInterna p : todas) {
                    if ((p.r1 == r && p.c1 == c && p.r2 == nr && p.c2 == nc) ||
                        (p.r1 == nr && p.c1 == nc && p.r2 == r && p.c2 == c)) {
                        removidas.add(p);
                        break;
                    }
                }
                carveParedes(nr, nc, visitado, todas, removidas, rand);
            }
        }
    }

    private void reiniciarPartidaCompleta() {
        balas.clear();
        explosionActual = null;
        enEsperaSiguienteRonda = false;
        tanque1Destruido = false;
        tanque2Destruido = false;

        generarLaberintoAleatorio();

        tanque1 = new Tanque(MARGEN_X + 40, MARGEN_Y + 40, colorJugador1, 90);
        tanque2 = new Tanque(MARGEN_X + ANCHO_TABLERO - 40, MARGEN_Y + ALTO_TABLERO - 40, colorJugador2, 270);
    }

    private void dispararTanque(Tanque t) {
        if (t != null && t.puedeDisparar()) {
            balas.add(t.disparar());
        }
    }

    private void iniciarExplosionYEspera(double x, double y) {
        explosionActual = new Explosion(x, y);
        enEsperaSiguienteRonda = true;
        tiempoInicioPausa = System.currentTimeMillis();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (enEsperaSiguienteRonda) {
            if (explosionActual != null) {
                explosionActual.actualizar();
            }

            if (System.currentTimeMillis() - tiempoInicioPausa >= TIEMPO_ESPERA_MS) {
                reiniciarPartidaCompleta();
            }

            repaint();
            return;
        }

        if (teclasPresionadas.contains(KeyEvent.VK_W)) tanque1.mover(1, paredes);
        if (teclasPresionadas.contains(KeyEvent.VK_S)) tanque1.mover(-1, paredes);
        if (teclasPresionadas.contains(KeyEvent.VK_A)) tanque1.rotar(-3);
        if (teclasPresionadas.contains(KeyEvent.VK_D)) tanque1.rotar(3);

        if (teclasPresionadas.contains(KeyEvent.VK_UP)) tanque2.mover(1, paredes);
        if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) tanque2.mover(-1, paredes);
        if (teclasPresionadas.contains(KeyEvent.VK_LEFT)) tanque2.rotar(-3);
        if (teclasPresionadas.contains(KeyEvent.VK_RIGHT)) tanque2.rotar(3);

        List<Bala> balasAEliminar = new ArrayList<>();
        for (Bala b : balas) {
            b.actualizar(paredes);
            if (b.haExpirado()) {
                balasAEliminar.add(b);
            }

            if (!tanque1Destruido && tanque1.getLimites().intersects(b.getBounds())) {
                puntos2++;
                tanque1Destruido = true;
                iniciarExplosionYEspera(tanque1.getLimites().getCenterX(), tanque1.getLimites().getCenterY());
                break;
            }

            if (!tanque2Destruido && tanque2.getLimites().intersects(b.getBounds())) {
                puntos1++;
                tanque2Destruido = true;
                iniciarExplosionYEspera(tanque2.getLimites().getCenterX(), tanque2.getLimites().getCenterY());
                break;
            }
        }
        balas.removeAll(balasAEliminar);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo del mapa
        g2d.setColor(new Color(230, 230, 230));
        g2d.fillRect(MARGEN_X, MARGEN_Y, ANCHO_TABLERO, ALTO_TABLERO);

        // Muros
        g2d.setColor(new Color(90, 90, 90));
        g2d.setStroke(new BasicStroke(6));
        for (Line2D pared : paredes) {
            g2d.draw(pared);
        }

        if (!tanque1Destruido) {
            tanque1.dibujar(g2d);
        }
        if (!tanque2Destruido) {
            tanque2.dibujar(g2d);
        }

        g2d.setColor(Color.BLACK);
        for (Bala b : balas) {
            b.dibujar(g2d);
        }

        if (explosionActual != null) {
            explosionActual.dibujar(g2d);
        }

        dibujarMarcador(g2d);
    }

    private void dibujarMarcador(Graphics2D g2d) {
        int yBase = 480;

        // Tanque Jugador 1
        dibujarIconoTanqueLateral(g2d, 160, yBase, colorJugador1, true);

        // Tanque Jugador 2
        dibujarIconoTanqueLateral(g2d, 520, yBase, colorJugador2, false);

        g2d.setColor(Color.BLACK);

        // Nombres sobre los marcadores
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(nombreJugador1, 160, yBase - 10);
        g2d.drawString(nombreJugador2, 520, yBase - 10);

        // Puntuación
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString(String.valueOf(puntos1), 205, yBase + 70);
        g2d.drawString(String.valueOf(puntos2), 565, yBase + 70);

        // Indicación para salir
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.GRAY);
        g2d.drawString("Presiona ESC para volver al menú", 10, 20);
    }

    private void dibujarIconoTanqueLateral(Graphics2D g2d, int x, int y, Color colorBase, boolean mirandoDerecha) {
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(x - 5, y + 22, 90, 12);

        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRoundRect(x, y + 10, 80, 18, 12, 12);

        g2d.setColor(new Color(80, 80, 80));
        for (int i = 8; i <= 68; i += 15) {
            g2d.fillOval(x + i, y + 13, 12, 12);
            g2d.setColor(new Color(30, 30, 30));
            g2d.drawOval(x + i, y + 13, 12, 12);
            g2d.setColor(new Color(80, 80, 80));
        }

        g2d.setColor(colorBase);
        int[] xChasis = {x + 5, x + 75, x + 65, x + 15};
        int[] yChasis = {y + 12, y + 12, y - 2, y - 2};
        g2d.fillPolygon(xChasis, yChasis, 4);

        g2d.setColor(colorBase.brighter());
        g2d.drawLine(x + 15, y - 2, x + 65, y - 2);

        g2d.setColor(colorBase.darker());
        g2d.fillArc(x + 20, y - 18, 40, 26, 0, 180);

        g2d.setColor(colorBase.brighter());
        g2d.fillRect(x + 35, y - 21, 10, 4);

        int xCanonInicio = x + 40;
        int yCanon = y - 10;
        int largoCanon = 45;

        g2d.setColor(colorBase.darker());
        g2d.fillRect(mirandoDerecha ? xCanonInicio : xCanonInicio - largoCanon, yCanon, largoCanon, 7);

        g2d.setColor(new Color(30, 30, 30));
        int xBoca = mirandoDerecha ? xCanonInicio + largoCanon - 2 : xCanonInicio - largoCanon - 4;
        g2d.fillRect(xBoca, yCanon - 2, 6, 11);
    }

    private static class ParedInterna {
        int r1, c1, r2, c2;
        boolean esVertical;

        ParedInterna(int r1, int c1, int r2, int c2, boolean esVertical) {
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
            this.esVertical = esVertical;
        }
    }
}