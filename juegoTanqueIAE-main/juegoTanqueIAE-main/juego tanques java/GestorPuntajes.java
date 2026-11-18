import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Clase auxiliar para representar un registro de la base de datos
class PuntajeRegistro {
    private String nombre;
    private int puntaje;

    public PuntajeRegistro(String nombre, int puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public String getNombre() { return nombre; }
    public int getPuntaje() { return puntaje; }
}

public class GestorPuntajes {

    private static final String URL = "jdbc:mysql://localhost:3306/prueba_java";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; 

    /**
     * NUEVO MÉTODO: Extrae los datos de MySQL y los manda en forma de Lista al main.
     */
    public static List<PuntajeRegistro> obtenerListaPuntajes() {
        List<PuntajeRegistro> listaPuntajes = new ArrayList<>();
        String sql = "SELECT nombre, puntaje FROM usuarios ORDER BY puntaje DESC";

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             Statement statement = conexion.createStatement();
             ResultSet resultado = statement.executeQuery(sql)) {

            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                int puntaje = resultado.getInt("puntaje");
                
                // Creamos el objeto y lo agregamos a la lista
                listaPuntajes.add(new PuntajeRegistro(nombre, puntaje));
            }

        } catch (SQLException e) {
            System.err.println("Aviso [GestorPuntajes]: Sin conexión a MySQL (" + e.getMessage() + "). Continuando en modo local.");
        }

        return listaPuntajes; // <--- ¡Aquí es donde le manda los datos al main!
    }

    /**
     * Devuelve los puntajes formateados como texto para mostrarlos en el JTextArea del menú.
     */
    public static String obtenerPuntajesTexto() {
        List<PuntajeRegistro> lista = obtenerListaPuntajes();
        if (lista.isEmpty()) {
            return " Sin puntajes registrados\n (o sin conexión a BD)";
        }
        StringBuilder sb = new StringBuilder();
        for (PuntajeRegistro p : lista) {
            sb.append(String.format(" %-12s : %4d pts\n", p.getNombre(), p.getPuntaje()));
        }
        return sb.toString();
    }

    /**
     * Guarda o acumula puntaje para un jugador en la base de datos MySQL.
     * Si el jugador ya existe en la tabla, suma los puntos al acumulado; si no, lo registra.
     */
    public static void guardarPuntaje(String nombre, int puntaje) {
        if (nombre == null || nombre.trim().isEmpty() || puntaje <= 0) {
            return;
        }

        String sqlBuscar = "SELECT id, puntaje FROM usuarios WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        String sqlUpdate = "UPDATE usuarios SET puntaje = puntaje + ? WHERE id = ?";
        String sqlInsert = "INSERT INTO usuarios (nombre, correo, puntaje) VALUES (?, '', ?)";

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            try (PreparedStatement checkStmt = conexion.prepareStatement(sqlBuscar)) {
                checkStmt.setString(1, nombre.trim());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        int puntajeAnterior = rs.getInt("puntaje");
                        try (PreparedStatement updateStmt = conexion.prepareStatement(sqlUpdate)) {
                            updateStmt.setInt(1, puntaje);
                            updateStmt.setInt(2, id);
                            updateStmt.executeUpdate();
                            System.out.println("-> BD: Puntaje actualizado para '" + nombre + "': " + puntajeAnterior + " + " + puntaje + " = " + (puntajeAnterior + puntaje) + " pts");
                        }
                    } else {
                        try (PreparedStatement insertStmt = conexion.prepareStatement(sqlInsert)) {
                            insertStmt.setString(1, nombre.trim());
                            insertStmt.setInt(2, puntaje);
                            insertStmt.executeUpdate();
                            System.out.println("-> BD: Nuevo jugador registrado en ranking: '" + nombre + "' con " + puntaje + " pts");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Aviso [GestorPuntajes]: Error al persistir puntaje (" + e.getMessage() + ")");
        }
    }
}