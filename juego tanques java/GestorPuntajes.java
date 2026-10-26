import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestorPuntajes {

    // Método para guardar (el que ya tenías)
    public static void guardarPuntaje(String nombreJugador, int puntaje) {
        try (FileWriter fw = new FileWriter("puntajes.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("Jugador: " + nombreJugador + " | Puntaje: " + puntaje);
        } catch (IOException e) {
            System.err.println("Error al guardar el puntaje: " + e.getMessage());
        }
    }

    // Nuevo método para leer los puntajes y devolverlos como texto
    public static String obtenerPuntajesTexto() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("puntajes.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        } catch (IOException e) {
            return "Aún no hay puntajes registrados.";
        }
        
        String resultado = sb.toString().trim();
        return resultado.isEmpty() ? "Aún no hay puntajes registrados." : resultado;
    }
}