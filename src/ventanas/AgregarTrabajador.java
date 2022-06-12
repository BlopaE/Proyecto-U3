
package ventanas;

import com.componentes.Table;
import conexion.ConexionTrabajador;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import logica.Trabajador;
/**
 *
 * @author Pablo Erick Ramírez Cruz
 */
public class AgregarTrabajador extends JFrame{
    
    private JTextField nombreTF,apellidoTF,sueldoTF;
    private JButton guardar;

    public AgregarTrabajador(String tipo, Table tabla) {
        
        
        this.setSize(300,300);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setTitle("AGREGAR TRABAJADOR");
        this.setLocationRelativeTo(null);
        Container contenedor = this.getContentPane();
        contenedor.setLayout(null);
        
        JLabel nombre = new JLabel("NOMBRE: ");
        nombre.setBounds(20, 30, 100, 20);
        nombre.setFont(Constantes.fontBold);
        contenedor.add(nombre);
        
        nombreTF = new JTextField();
        nombreTF.setBounds(110, 30, 140, 20);
        nombreTF.setFont(Constantes.fontPlain);
        contenedor.add(nombreTF);
        
        JLabel apellido = new JLabel("APELLIDO: ");
        apellido.setBounds(20, 75, 100, 20);
        apellido.setFont(Constantes.fontBold);
        contenedor.add(apellido);
        
        apellidoTF = new JTextField();
        apellidoTF.setBounds(110, 75, 140, 20);
        apellidoTF.setFont(Constantes.fontPlain);
        contenedor.add(apellidoTF);
        
        JLabel sueldo = new JLabel("SUELDO: ");
        sueldo.setBounds(20, 120, 100, 20);
        sueldo.setFont(Constantes.fontBold);
        contenedor.add(sueldo);
        
        sueldoTF = new JTextField();
        sueldoTF.setBounds(110, 120, 140, 20);
        sueldoTF.setFont(Constantes.fontPlain);
        contenedor.add(sueldoTF);
        
        guardar = new JButton("Guardar");
        guardar.setBounds(20, 200, 240, 30);
        guardar.setBackground(Constantes.colorPrincipal);
        guardar.setForeground(Constantes.colorLight);
        guardar.setBorder(null);
        guardar.addMouseListener(new ButtonHover(guardar,ButtonHover.BACKGROUND));
        guardar.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Trabajador t = new Trabajador(nombreTF.getText(),apellidoTF.getText(),Float.parseFloat(sueldoTF.getText()),tipo);
                ConexionTrabajador ct = new ConexionTrabajador(t, tipo);
                if (ct.insertTrabajador() == 1){
                    setAlwaysOnTop(false);
                    try {
                        tabla.actualizarTabla(ct.toTable(), Principal.encabezados);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar la tabla: "+ex.getMessage());
                    }
                    dispose();
                }
            }
        });
        contenedor.add(guardar);       
    }   
}
