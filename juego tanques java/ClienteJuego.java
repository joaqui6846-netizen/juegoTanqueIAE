import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteJuego {

    private String ipServidor;
    private int puerto;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;

    public ClienteJuego(String ipServidor, int puerto) {
        this.ipServidor = ipServidor;
        this.puerto = puerto;
    }

    public boolean conectar() {
        try {
            socket = new Socket(ipServidor, puerto);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado con éxito al servidor " + ipServidor);
            return true;
        } catch (Exception e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            return false;
        }
    }

    public void enviarDatos(String datos) {
        if (salida != null) {
            salida.println(datos);
        }
    }

    public static void main(String[] args) {
        // Reemplaza con la IP de la PC que actúa como Servidor
        String ipPC1 = "192.168.1.50"; 
        ClienteJuego cliente = new ClienteJuego(ipPC1, 12345);
        
        if (cliente.conectar()) {
            // Ejemplo de envío de posición del tanque
            cliente.enviarDatos("POS:200,300");
        }
    }
}