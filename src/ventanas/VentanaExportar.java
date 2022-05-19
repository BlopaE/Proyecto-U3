
package ventanas;

import java.awt.Container;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import logica.ListaTrabajadores;
import logica.Trabajador;

/**
 *
 * @author pablo
 */
public class VentanaExportar extends JDialog implements Runnable{

    private Thread hilo;
    private JLabel etiqueta;
    private ListaTrabajadores lista;
    private JProgressBar bar;
    
    //Recibe la lista, para asi saber donde guardar el archivo
    public VentanaExportar(ListaTrabajadores lista) {
        
        this.setTitle("EXPORTANDO");
        this.setResizable(false);
        this.setSize(400, 100);
        this.setLocationRelativeTo(null);
        
        Container contenedor = this.getContentPane();
        contenedor.setLayout(null);
        
        bar = new JProgressBar(0, lista.getList().size());
        bar.setBounds(10, 10, 370, 15);
        contenedor.add(bar);
        
        etiqueta = new JLabel("",JLabel.CENTER);
        etiqueta.setBounds(0, 35, 400, 20);
        etiqueta.setFont(Constantes.fontPlain);
        contenedor.add(etiqueta);

        this.lista = lista;
        
        hilo = new Thread(this);
        
        hilo.start();
        
    }
    
    @Override
    public void run() {
        
        String nombreArchivo = null;
        
        //Si la lista está vacia no hay nada que guardar
        if (lista.getList().isEmpty()){
            return;
        }
        
        //Comprueba de que tipo es la lista (dia, semana, mes)
        switch (lista.getList().get(0).getTipo()){
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
            FileWriter fw = new FileWriter(nombreArchivo);
            fw.append("Nombre,Apellido,Tipo,Fecha de Resgistro,Salario\n");
            for (int i = 0; i<lista.getList().size(); i++){
                Trabajador t = lista.getTrabajador(i);
                fw.append(t.getNombre()+","+t.getApellido()+","+t.getTipo()+","+t.getFechaRegistro().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"))+","+t.getSalario()+"\n");
                etiqueta.setText("Exportando ... "+i+" de "+String.valueOf(lista.getList().size()));
                bar.setValue(i+1);
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
            
        }
    }
    
}
