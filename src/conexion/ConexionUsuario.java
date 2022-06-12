
package conexion;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import logica.Usuario;

public class ConexionUsuario {

    Usuario usuario = null;
    Connection con = null;

    public ConexionUsuario(Usuario usuario) {

        this.usuario = usuario;
    }

    //Insertar un usario
    public int insertUsuario() {
        int res = 0;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("insert into usuarios(name,password,rol) " + "values (?,?,?)");
            pa.setString(1, usuario.getNombre());
            pa.setString(2, String.valueOf(usuario.getPassword()));
            pa.setString(3, usuario.getRol());

            res = pa.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    //Comprobar si existe un usuario administrador
    public String accesar() {
        String rt = "denegado";
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement(" select * from usuarios "
                    + "where name = ? and password = ?");
            pa.setString(1, usuario.getNombre());
            pa.setString(2, String.valueOf(usuario.getPassword()));
            ResultSet rs = pa.executeQuery();

            if (rs.next()) {
                rt = rs.getString("rol");
                int id = rs.getInt("id");
                System.out.println("ID: "+id);
                pa = con.prepareStatement("update usuarios set sesion=?"+" where id=?");
                pa.setBoolean(1, true);
                pa.setInt(2,id);
                
                pa.executeUpdate();
                
                con.close();
            }
        } catch (Exception e1) {
            System.out.println("Error conexion: " + e1.getMessage());
        }
        return rt;
    }

    public ResultSet obtenerTodos() {
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("select * from usuarios",ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
            rs = pa.executeQuery();

        } catch (Exception e1) {
            System.out.println("Error: " + e1.getMessage());
        }
        return rs;
    }
    
    public boolean isEmpty(){
        ResultSet rs = obtenerTodos();
        boolean resultado = false;
        try {
            resultado = !rs.isBeforeFirst();
        } catch (SQLException ex) {
            System.out.println("Error "+ex.getMessage());
        }
        return resultado;
    }
    
    public ResultSet obtener(){
        ResultSet rs= null;
        try{
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement(" select * from usuarios "
                    + "where name = ? and password = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pa.setString(1, usuario.getNombre());
            pa.setString(2, String.valueOf(usuario.getPassword()));
            rs = pa.executeQuery();
            
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        return rs;
    }
    
     public ResultSet obtenerPorId(){
        ResultSet rs= null;
        try{
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement(" select * from usuarios "
                    + "where id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pa.setInt(1,usuario.getId());
            rs = pa.executeQuery();
            
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        return rs;
     }
      
    
    
    public int cerrarSesionUsuario(){
        int res = 0;
        try{
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("update usuarios set sesion = ?"+"where id = ?");
            pa.setBoolean(1, false);
            pa.setInt(2, usuario.getId());
            System.out.println(usuario.getId());
            res = pa.executeUpdate();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Error: "+ex.getMessage());
        }
        return res;
    }

    public int editarUsuario() {
        int res = 0;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa
                    = con.prepareStatement("update usuarios set name=?, password=?, rol=? "
                            + " where id= ?");
            pa.setString(1, usuario.getNombre());
            pa.setString(2, String.valueOf(usuario.getPassword()));
            pa.setString(3, usuario.getRol());
            pa.setInt(4, usuario.getId());

            res = pa.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public int delUsuario() {
        int res = 0;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("delete from usuarios where id = ?");
            pa.setInt(1, usuario.getId());

            res = pa.executeUpdate();
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
        return res;
    }
}
