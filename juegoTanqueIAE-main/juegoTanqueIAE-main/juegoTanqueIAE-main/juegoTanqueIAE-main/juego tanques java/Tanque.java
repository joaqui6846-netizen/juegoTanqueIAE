import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Tanque {
    private double x, y;
    private double angulo; // En grados
    private Color colorBase;
    private final int ANCHO = 28, ALTO = 22;
    private long ultimoDisparo = 0;

    public Tanque(double x, double y, Color colorBase, double anguloInicial) {
        this.x = x;
        this.y = y;
        this.colorBase = colorBase;
        this.angulo = anguloInicial;
    }

    public void rotar(double delta) {
        angulo = (angulo + delta) % 360;
    }

    public void mover(double dir, List<Line2D> paredes) {
        double rad = Math.toRadians(angulo);
        double nx = x + Math.cos(rad) * dir * 2.5;
        double ny = y + Math.sin(rad) * dir * 2.5;

        // Comprobación previa de colisión con los límites del tanque
        Rectangle2D futuro = new Rectangle2D.Double(nx - ANCHO / 2.0, ny - ALTO / 2.0, ANCHO, ALTO);
        boolean colision = false;
        for (Line2D pared : paredes) {
            if (pared.intersects(futuro)) {
                colision = true;
                break;
            }
        }

        if (!colision) {
            x = nx;
            y = ny;
        }
    }

    public boolean puedeDisparar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo > 400) { // Cooldown de 400 ms
            ultimoDisparo = ahora;
            return true;
        }
        return false;
    }

    public Bala disparar() {
        double rad = Math.toRadians(angulo);
        double bx = x + Math.cos(rad) * (ANCHO / 2.0 + 10);
        double by = y + Math.sin(rad) * (ANCHO / 2.0 + 10);
        return new Bala(bx, by, angulo);
    }

    public Rectangle2D getLimites() {
        return new Rectangle2D.Double(x - ANCHO / 2.0, y - ALTO / 2.0, ANCHO, ALTO);
    }

    public void dibujar(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angulo));

        // 1. Sombra suave inferior
        g2d.setColor(new Color(0, 0, 0, 45));
        g2d.fillRoundRect(-ANCHO / 2 + 3, -ALTO / 2 + 3, ANCHO, ALTO, 6, 6);

        // Colors derivados para luces y sombras
        Color colorOscuro = colorBase.darker();
        Color colorClaro = colorBase.brighter();
        Color colorOruga = new Color(50, 50, 50);

        // 2. Orugas/Treads (Superior e Inferior)
        g2d.setColor(colorOruga);
        g2d.fillRoundRect(-ANCHO / 2 - 2, -ALTO / 2 - 2, ANCHO + 2, 6, 3, 3);
        g2d.fillRoundRect(-ANCHO / 2 - 2, ALTO / 2 - 4, ANCHO + 2, 6, 3, 3);

        // Detalle de tracción de las orugas (Líneas)
        g2d.setColor(new Color(20, 20, 20));
        g2d.setStroke(new BasicStroke(1));
        for (int i = -ANCHO / 2; i <= ANCHO / 2 - 2; i += 4) {
            g2d.drawLine(i, -ALTO / 2 - 2, i, -ALTO / 2 + 3);
            g2d.drawLine(i, ALTO / 2 - 4, i, ALTO / 2 + 1);
        }

        // 3. Chasis principal
        g2d.setColor(colorBase);
        g2d.fillRoundRect(-ANCHO / 2, -ALTO / 2 + 3, ANCHO - 2, ALTO - 6, 5, 5);

        // Bisel superior/luz en el chasis
        g2d.setColor(colorClaro);
        g2d.drawRoundRect(-ANCHO / 2 + 1, -ALTO / 2 + 4, ANCHO - 4, ALTO - 8, 4, 4);

        // 4. Cañón principal
        g2d.setColor(colorOscuro);
        g2d.fillRect(0, -3, ANCHO / 2 + 8, 6);

        // Boca / Freno de boca del cañón (Detalle extremo)
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRect(ANCHO / 2 + 6, -4, 4, 8);

        // 5. Torreta circular central
        g2d.setColor(colorOscuro);
        g2d.fillOval(-8, -8, 16, 16);

        // Escotilla central
        g2d.setColor(colorBase);
        g2d.fillOval(-5, -5, 10, 10);

        g2d.setColor(colorClaro);
        g2d.drawOval(-5, -5, 10, 10);

        g2d.setTransform(oldTransform);
    }
}