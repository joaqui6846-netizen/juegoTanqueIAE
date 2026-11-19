import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorJuego {

    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        System.out.println("Esperando conexión del Jugador 2...");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            // Acepta la conexión entrante del Cliente (Jugador 2)
            Socket socketCliente = serverSocket.accept();
            System.out.println("¡Jugador 2 conectado desde: " + socketCliente.getInetAddress() + "!");

            // Canales de comunicación
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);

            // Bucle de lectura de mensajes del cliente
            String mensajeEntrante;
            while ((mensajeEntrante = entrada.readLine()) != null) {
                System.out.println("Recibido del Cliente: " + mensajeEntrante);
                
                // Ejemplo: Reenviar confirmación o estado actualizado
                salida.println("OK:" + mensajeEntrante);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}