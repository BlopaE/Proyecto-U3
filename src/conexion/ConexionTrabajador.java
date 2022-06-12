/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import logica.Trabajador;

/**
 *
 * @author pablo
 */
public class ConexionTrabajador {

    Trabajador t;
    Connection con;
    String tipo;

    public ConexionTrabajador(Trabajador t, String tipo) {
        this.t = t;
        this.tipo = tipo;
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

    //Recibe la tabla a la que se insertara, es decir, el tipo de trabajador que es
    public int insertTrabajador() {
        int res = 0;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("insert into trabajadores" + tipo + " (name, lastname, sueldo, sueldofinal, fecharegistro) " + "values (?,?,?,?,?)");
            pa.setString(1, t.getNombre());
            pa.setString(2, t.getApellido());
            pa.setFloat(3, t.getSalario());
            pa.setFloat(4, t.getSalarioFinal());
            pa.setString(5, t.getFechaRegistro());

            res = pa.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
    
    public int delete() {
        int res = 0;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("delete from trabajadores"+tipo+" where id = ?");
            pa.setInt(1, t.getId());

            res = pa.executeUpdate();
        } catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
        return res;
    }
    
    //Modificar nombre, sueldo, retardo y falta
    public int update(){
        int res = 0;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa
                    = con.prepareStatement("update trabajadores"+tipo+" set name=?, lastname=?, sueldo=?, retardos=?, faltas=?, sueldofinal=?"
                            + " where id= ?");
            pa.setString(1,t.getNombre());
            pa.setString(2,t.getApellido());
            pa.setFloat(3, t.getSalario());
            pa.setInt(4, t.getRetardos());
            pa.setInt(5, t.getFaltas());
            pa.setFloat(6, t.getSalarioFinal());
            pa.setInt(7, t.getId());

            res = pa.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
    
    public ResultSet obtener(){
        ResultSet rs= null;
        try{
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement(" select * from trabajadores"+tipo
                    + " where id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pa.setInt(1,t.getId());
            rs = pa.executeQuery();
            
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        return rs;
    }

    public ResultSet obtenerTodos() {
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(Conexion.cadena, Conexion.usuario, Conexion.password);
            PreparedStatement pa = con.prepareStatement("select * from trabajadores" + tipo, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = pa.executeQuery();

        } catch (Exception e1) {
            System.out.println("Error: " + e1.getMessage());
        }
        return rs;
    }

    public Object[][] toTable() throws SQLException {

        ResultSet rs = obtenerTodos();

        int numFilas = 0;
        while (rs.next()) {
            numFilas++;
        }

        rs.beforeFirst();

        Object[][] table = new Object[numFilas][8];

        for (int i = 0; i < numFilas; i++) {

            if (rs.next()) {
                
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String apellido = rs.getString("lastName");
                float sueldo = rs.getFloat("sueldo");
                Trabajador trabajador = new Trabajador(name, apellido, sueldo, tipo);
                trabajador.setId(id);
                trabajador.setFechaRegistro(rs.getString("fecharegistro"));
                
                table[i][0] = trabajador.getId();
                table[i][1] = trabajador.getNombre();
                table[i][2] = trabajador.getApellido();
                table[i][3] = trabajador.getSalario();
                table[i][4] = rs.getInt("retardos");
                table[i][5] = rs.getInt("faltas");
                table[i][6] = rs.getFloat("sueldofinal");
                table[i][7] = rs.getString("fecharegistro");
            }

        }
        return table;
    }
}
