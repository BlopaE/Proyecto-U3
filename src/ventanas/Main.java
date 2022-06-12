/*
Gestion de la nomina de los trabajadores de una institucion
para poder llevar el control de sueldos, faltas, retardos
descuentos, se deben contemplar trabajadores por quincena, semana y dia
*/
package ventanas;

import conexion.ConexionUsuario;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Pablo Erick Ramírez Cruz
 */
public class Main {
    
    public static void main(String[] args) {
        
        
        try {
            Constantes.colorPrincipal = Constantes.loadDataColorPrincipal();
            Constantes.colorLight = Constantes.loadDataColorLight();
            Constantes.colorAcent = Constantes.loadDataColorAccent();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al leer los archivos: "+ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            
        }
        
        ConexionUsuario cu = new ConexionUsuario(null);
        
        try {
            if (!cu.obtenerTodos().isBeforeFirst()){ //Si no hay registros en la tabla
                //Obligar a registrar un usuario administrador
                RegistroUsuario registro = new RegistroUsuario(null);
                registro.setVisible(true);
                registro.setDefaultCloseOperation(RegistroUsuario.EXIT_ON_CLOSE);
                
            }else{
                Login login = new Login();
                login.setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error "+ex.getMessage());
        }
    }
}
