package ventanas;

import com.componentes.Table;
import conexion.ConexionTrabajador;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

//Clases del componente logico
import logica.Trabajador;
import logica.Validacion;

/**
 *
 * @author pablo erick ramirez cruz
 */
public class Modificacion extends JFrame {

    private JTextField campo1, campo2;
    private JSpinner spinner;
    private JButton guardar;
    private String param;
    private Trabajador t;
    private Container contenedor;
    private JFrame contexto = this;

    public final static String NOMBRE = "NOMBRE", SUELDO = "SUELDO", RETARDO = "RETARDO", FALTA = "FALTA";

    public Modificacion(String param, Trabajador t, Table tabla) { //Parametro a modificar, Trabajador a modificar, Tabla en la cual se actualizará

        this.param = param;
        this.t = t;

        this.setSize(300, 200);
        this.setResizable(false);
        this.setTitle("Moficar atributo");
        this.setLocationRelativeTo(null);
        this.setIconImage(Constantes.icon.getImage());
        contenedor = this.getContentPane();
        contenedor.setLayout(null);

        switch (param) {
            case NOMBRE:
                renderModificarNombre();
                break;
            case SUELDO:
                renderModificarSueldo();
                break;
            case RETARDO:
                renderModificarRetardo();
                break;
            case FALTA:
                renderModificarFalta();
                break;
            default:
                break;
        }

        guardar = new JButton("Guardar Cambios");
        guardar.setBounds(20, 110, 240, 30);
        guardar.setBackground(Constantes.colorPrincipal);
        guardar.setForeground(Constantes.colorLight);
        guardar.setBorder(null);
        guardar.addMouseListener(new ButtonHover(guardar, ButtonHover.BACKGROUND));
        guardar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                switch (param) {
                    case NOMBRE:
                        JTextField campos[] = {campo1, campo2};
                        if (Validacion.comprobarVacios(campos)){
                            JOptionPane.showMessageDialog(contexto, "Debe rellenar los campos","ERROR", JOptionPane.WARNING_MESSAGE);
                        }else{
                            t.setNombre(campo1.getText().trim());
                            t.setApellido(campo2.getText().trim());
                        }
                        break;
                        
                    case SUELDO:
                        JTextField campo[] = {campo1};
                        if (Validacion.comprobarVacios(campo)){
                            JOptionPane.showMessageDialog(contexto, "Debe rellenar el campo", "ERROR", JOptionPane.WARNING_MESSAGE);
                        }else{
                            t.setSalario(Float.parseFloat(campo1.getText()));
                            t.setSalarioFinal(t.getSalarioFinal());
                        }
                        break;
                        
                    case RETARDO:
                        t.setRetardos((int) spinner.getValue());
                        t.setSalarioFinal(t.getSalarioFinal());
                        break;
                        
                    case FALTA:
                        t.setFaltas((int) spinner.getValue());
                        t.setSalarioFinal(t.getSalarioFinal());
                        break;
                    default:
                        break;
                }
                
                ConexionTrabajador ct = new ConexionTrabajador(t, t.getTipo());
                if (ct.update() == 1){
                    try {
                        tabla.actualizarTabla(ct.toTable(), Principal.encabezados);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar la tabla "+ex.getMessage());
                    }
                }
                dispose();
            }

        });
        contenedor.add(guardar);
    }

    private void renderModificarNombre() {

        JLabel title = new JLabel("Ingrese el nuevo nombre y apellido", JLabel.CENTER);
        title.setFont(Constantes.fontPlain);
        title.setBounds(10,10,260,20);
        contenedor.add(title);
        
        JLabel subtitle = new JLabel("Nombre");
        subtitle.setFont(Constantes.fontBold);
        subtitle.setBounds(20, 40, 100, 20);
        contenedor.add(subtitle);
        
        campo1 = new JTextField(t.getNombre());
        campo1.setFont(Constantes.fontPlain);
        campo1.setBounds(20, 65, 115, 20);
        contenedor.add(campo1);
        
        JLabel subtitle2 = new JLabel("Apellido");
        subtitle2.setFont(Constantes.fontBold);
        subtitle2.setBounds(140, 40, 100, 20);
        contenedor.add(subtitle2);
        
        campo2 = new JTextField(t.getApellido());
        campo2.setFont(Constantes.fontPlain);
        campo2.setBounds(140, 65, 120, 20);
        contenedor.add(campo2);
    }

    private void renderModificarSueldo() {

        JLabel title = new JLabel("<html>Ingrese el nuevo sueldo de "+t.getNombre()+" "+t.getApellido()+"</html>");
        title.setFont(Constantes.fontPlain);
        title.setBounds(10, 10, 300, 40);
        contenedor.add(title);
        
        JLabel subtitle = new JLabel("Sueldo:");
        subtitle.setFont(Constantes.fontBold);
        subtitle.setBounds(60, 60, 100, 20);
        contenedor.add(subtitle);
        
        campo1 = new JTextField(String.valueOf(t.getSalario()));
        campo1.setFont(Constantes.fontPlain);
        campo1.setBounds(140, 60, 80, 20);
        campo1.addKeyListener(new KeyAdapter(){
            
            @Override
            public void keyTyped(KeyEvent e) {
                if (campo1.getText().contains(new StringBuffer("."))){
                    Validacion.escribirSoloNumerosEnteros(e);
                }
                Validacion.escribirSoloNumerosDecimales(e);
            }
        });
        contenedor.add(campo1);
    }

    private void renderModificarRetardo() {

        JLabel title = new JLabel("<html>Incremente o decremente la cantidad de retardos de "+t.getNombre()+" "+t.getApellido()+"</html>");
        title.setFont(Constantes.fontPlain);
        title.setBounds(10, 10, 300, 40);
        contenedor.add(title);
        
        JLabel subtitle = new JLabel("Retardos:");
        subtitle.setFont(Constantes.fontBold);
        subtitle.setBounds(60, 60, 100, 20);
        contenedor.add(subtitle);
        
        
        spinner = new JSpinner(new SpinnerNumberModel(t.getRetardos(),0,999,1));
        spinner.setBounds(140, 60, 80, 20);
        spinner.setFont(Constantes.fontPlain);
        spinner.setBackground(Constantes.colorAcent);
        contenedor.add(spinner);
    }

    private void renderModificarFalta() {

        JLabel title = new JLabel("<html>Incremente o decremente la cantidad de <br> faltas de "+t.getNombre()+" "+t.getApellido()+"</html>");
        title.setFont(Constantes.fontPlain);
        title.setBounds(10, 10, 300, 40);
        contenedor.add(title);
        
        JLabel subtitle = new JLabel("Faltas:");
        subtitle.setFont(Constantes.fontBold);
        subtitle.setBounds(60, 60, 100, 20);
        contenedor.add(subtitle);
        
        
        spinner = new JSpinner(new SpinnerNumberModel(t.getFaltas(),0,999,1));
        spinner.setBounds(140, 60, 80, 20);
        spinner.setFont(Constantes.fontPlain);
        spinner.setBackground(Constantes.colorAcent);
        contenedor.add(spinner);
    }
}
