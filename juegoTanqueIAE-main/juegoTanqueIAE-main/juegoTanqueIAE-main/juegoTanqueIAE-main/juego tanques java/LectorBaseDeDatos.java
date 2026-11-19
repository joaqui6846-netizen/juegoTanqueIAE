import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LectorBaseDeDatos {
    public static void main(String[] args) {
        // 1. Datos de conexión a tu PC
        String url = "jdbc:mysql://localhost:3306/prueba_java";
        String usuario = "root";
        String password = ""; // Tu contraseña de MySQL si tienes

        try {
            // 2. Conectarnos (reemplaza al "new FileReader('archivo.txt')")
            Connection conexion = DriverManager.getConnection(url, usuario, password);
            
            // 3. Crear el "puente" para enviar la consulta SQL
            Statement statement = conexion.createStatement();

            // 4. Ejecutar la consulta (aquí pides los datos que antes leías del txt)
            String sql = "SELECT id, nombre, correo FROM usuarios";
            ResultSet resultado = statement.executeQuery(sql);

            // 5. Recorrer los resultados (reemplaza al bucle que leía líneas del .txt)
            System.out.println("Leyendo datos desde MySQL:\n");
            
            while (resultado.next()) {
                // Extraes cada columna por su nombre o número de columna
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correo");

                // Aquí haces con los datos lo mismo que hacías cuando leías el .txt
                System.out.println("-> Usuario encontrado: ID=" + id + ", Nombre=" + nombre + ", Correo=" + correo);
            }

            // 6. Cerrar todo para liberar memoria (muy importante)
            resultado.close();
            statement.close();
            conexion.close();
            
            System.out.println("\n¡Lectura de la base de datos completada con éxito!");

        } catch (Exception e) {
            System.out.println("Ocurrió un error al leer la base de datos:");
            e.printStackTrace();
        }
    }
}