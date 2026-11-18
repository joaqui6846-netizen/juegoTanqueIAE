import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Explosion {
    private double x, y;
    private List<Particula> particulas = new ArrayList<>();
    private long tiempoInicio;

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
        this.tiempoInicio = System.currentTimeMillis();

        Random rand = new Random();
        int numParticulas = 40; // Cantidad de fragmentos de la explosión

        Color[] colores = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.DARK_GRAY,
            Color.GRAY
        };

        for (int i = 0; i < numParticulas; i++) {
            double angulo = rand.nextDouble() * Math.PI * 2;
            double velocidad = 1 + rand.nextDouble() * 5;
            double vx = Math.cos(angulo) * velocidad;
            double vy = Math.sin(angulo) * velocidad;
            int tam = 6 + rand.nextInt(10);
            Color c = colores[rand.nextInt(colores.length)];

            particulas.add(new Particula(x, y, vx, vy, tam, c));
        }
    }

    public void actualizar() {
        for (Particula p : particulas) {
            p.actualizar();
        }
    }

    public void dibujar(Graphics2D g2d) {
        // Onda de choque / resplandor central que se expande
        long transcurrido = System.currentTimeMillis() - tiempoInicio;
        int radioResplandor = (int) (transcurrido / 12);

        if (radioResplandor < 80) {
            g2d.setColor(new Color(255, 100, 0, Math.max(0, 150 - radioResplandor * 2)));
            g2d.fillOval((int) (x - radioResplandor), (int) (y - radioResplandor), radioResplandor * 2, radioResplandor * 2);
        }

        // Dibujar las partículas de chispas y humo
        for (Particula p : particulas) {
            p.dibujar(g2d);
        }
    }

    private static class Particula {
        double px, py;
        double vx, vy;
        int tam;
        Color color;

        Particula(double x, double y, double vx, double vy, int tam, Color color) {
            this.px = x;
            this.py = y;
            this.vx = vx;
            this.vy = vy;
            this.tam = tam;
            this.color = color;
        }

        void actualizar() {
            px += vx;
            py += vy;
            vx *= 0.95; // Fricción suave
            vy *= 0.95;
            if (tam > 1) {
                tam *= 0.97; // Encogimiento progresivo
            }
        }

        void dibujar(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int) (px - tam / 2.0), (int) (py - tam / 2.0), tam, tam);
        }
    }
}