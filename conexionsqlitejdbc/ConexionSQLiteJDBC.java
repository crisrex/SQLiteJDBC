
package conexionsqlitejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Luis Serrano luis@cescristorey.com
 */
public class ConexionSQLiteJDBC {

    /**
     * Conexión a una base de datos local
     */
    private Connection conectar() {
        // cadena de conexión
        String url = "jdbc:sqlite:ejemplo.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void crearTabla() {
        // sentencia SQL para creación tabla
        String sql = "CREATE TABLE IF NOT EXISTS alumnos (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	nombre text NOT NULL,\n"
                + "	nota real\n"
                + ");";
 
        try (Connection conn = this.conectar();
                Statement stmt = conn.createStatement()) {
            // creamos la nueva tabla
                stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Inserta una nueva fila en la tabla alumnos
     *
     * @param nombre Nombre del alumno
     * @param nota Nota del alumno
     */
    public void insertar(String nombre, double nota) {
        String sql = "INSERT INTO alumnos(nombre,nota) VALUES(?,?)";
 
        try (Connection conn = this.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, nota);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
     /**
     * Actualiza un alumno 
     *
     * @param id Id del alumno a modificar
     * @param nombre Nuevo nombre del alumno
     * @param nota Nueva nota del alumno
     */
    public void modificar(int id, String nombre, double nota) {
        String sql = "UPDATE alumnos SET nombre = ? , "
                + "nota = ? "
                + "WHERE id = ?";
 
        try (Connection conn = this.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // pongo los parámetros
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, nota);
            pstmt.setInt(3, id);
            // actualizo
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    /**
     * muestra todas las filas de la tabla alumnos
     */
    public void mostrarTodos(){
        String sql = "SELECT id, nombre, nota FROM alumnos";
        
        try (Connection conn = this.conectar();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // bucle para recorrer y mostrar los resultados de la consulta
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("nombre") + "\t" +
                                   rs.getDouble("nota"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
     /**
     * Elimina el alumno especificado por el id
     *
     * @param id
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM alumnos WHERE id = ?";
 
        try (Connection conn = this.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // fija los parámetros
            pstmt.setInt(1, id);
            // ejecuta la sentencia de borrado
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * @param args argumentos desde la línea de comandos
     */
    public static void main(String[] args) {
        ConexionSQLiteJDBC app = new ConexionSQLiteJDBC();
        app.conectar();
        app.crearTabla();
        app.insertar("Juan Machado ",3.7);
        app.insertar("Antonio Escudo", 6.9);
        app.modificar(1,"Marcs MOntes", 8.5);
        app.mostrarTodos();
        app.eliminar(1);
    }
    
}
