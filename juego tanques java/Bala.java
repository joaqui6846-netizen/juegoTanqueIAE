import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Bala {
    private double x, y;
    private double vx, vy;
    private int rebotesMax = 5;
    private int rebotes = 0;
    private final int RADIO = 4;

    public Bala(double x, double y, double angulo) {
        this.x = x;
        this.y = y;
        double velocidad = 4.5;
        double rad = Math.toRadians(angulo);
        this.vx = Math.cos(rad) * velocidad;
        this.vy = Math.sin(rad) * velocidad;
    }

    public void actualizar(List<Line2D> paredes) {
        double nx = x + vx;
        double ny = y + vy;

        for (Line2D pared : paredes) {
            if (pared.intersectsLine(x, y, nx, ny)) {
                rebotes++;
                // Inversión de velocidad según orientación de pared
                if (pared.getX1() == pared.getX2()) {
                    vx = -vx; // Pared vertical
                } else {
                    vy = -vy; // Pared horizontal
                }
                break;
            }
        }

        x += vx;
        y += vy;
    }

    public boolean haExpirado() {
        return rebotes >= rebotesMax;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);
    }

    public void dibujar(Graphics2D g2d) {
        g2d.fillOval((int) (x - RADIO), (int) (y - RADIO), RADIO * 2, RADIO * 2);
    }
}