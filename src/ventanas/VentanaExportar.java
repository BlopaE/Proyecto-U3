
package ventanas;

import conexion.ConexionTrabajador;
import java.awt.Container;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import logica.Trabajador;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pablo
 */
public class VentanaExportar extends JDialog implements Runnable{

    private Thread hilo;
    private JLabel etiqueta;
    private JProgressBar bar;
    private String tipo;
    
    private int numFilas;
    
    //Recibe la lista, para asi saber donde guardar el archivo
    public VentanaExportar(String tipo) {
        
        this.tipo = tipo;
        
        this.setTitle("EXPORTANDO");
        this.setResizable(false);
        this.setSize(400, 100);
        this.setLocationRelativeTo(null);
        
        Container contenedor = this.getContentPane();
        contenedor.setLayout(null);
        
        ConexionTrabajador ct = new ConexionTrabajador(null, tipo);
        ResultSet rs = ct.obtenerTodos();
        
        numFilas = 0;
        try {
            while(rs.next()){
                numFilas++;
            }
        } catch (SQLException ex) {
            
        }
        
        bar = new JProgressBar(0, numFilas);
        bar.setBounds(10, 10, 370, 15);
        contenedor.add(bar);
        
        etiqueta = new JLabel("",JLabel.CENTER);
        etiqueta.setBounds(0, 35, 400, 20);
        etiqueta.setFont(Constantes.fontPlain);
        contenedor.add(etiqueta);
        
        hilo = new Thread(this);
        
        hilo.start();
        
    }
    
    @Override
    public void run() {
        
        String nombreArchivo = null;
        
        //Si la lista está vacia no hay nada que guardar
        ConexionTrabajador ct = new ConexionTrabajador(null, tipo);
        
        if (ct.isEmpty()){
            return;
        }
        
        //Comprueba de que tipo es la lista (dia, semana, mes)
        switch (tipo){
            case Trabajador.DIA:
                nombreArchivo = "exportarDia.csv";
                break;
            case Trabajador.SEMANA:
                nombreArchivo = "exportarSemana.csv";
                break;
            case Trabajador.QUINCENA:
                nombreArchivo = "exportarQuincena.csv";
                break;
            default:
                break;
        }
        try {
            
            ResultSet rs = new ConexionTrabajador(null, tipo).obtenerTodos();
            
            FileWriter fw = new FileWriter(nombreArchivo);
            fw.append("Nombre,Apellido,Sueldo,Retardos,Faltas,SueldoFinal,Fecha de Resgistro\n");
            int i = 1;
            while(rs.next()){
                
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");
                float sueldo = rs.getFloat("sueldo");
                int retardos = rs.getInt("retardos");
                int faltas = rs.getInt("faltas");
                float sueldoFinal = rs.getFloat("sueldofinal");
                String fecha = rs.getString("fecharegistro");
                
                fw.append(name+","+lastname+","+sueldo+","+retardos+","+faltas+","+sueldoFinal+","+fecha+"\n");
                etiqueta.setText("Exportando ... "+i+" de "+String.valueOf(numFilas));
                bar.setValue(i+1);
                i++;
                hilo.sleep(250);
            }
            fw.flush();
            fw.close();
            etiqueta.setText("Datos exportados correctamente");
            hilo.sleep(1000);
            this.dispose();
            
        } catch (IOException ex) {
            
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (InterruptedException ex) {
            
        } catch (SQLException ex) {
            
        }
    }
    
}
