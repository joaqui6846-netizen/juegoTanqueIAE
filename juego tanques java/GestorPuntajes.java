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
            e.printStackTrace();
        }

        return listaPuntajes; // <--- ¡Aquí es donde le manda los datos al main!
    }

    // (Tu método de guardar se queda igual)
    public static void guardarPuntaje(String nombre, int puntaje) {
        String sql = "INSERT INTO usuarios (nombre, puntaje) VALUES (?, ?)";
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, puntaje);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}