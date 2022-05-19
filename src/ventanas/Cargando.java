
package ventanas;

import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import logica.Usuario;

public class Cargando extends JDialog implements Runnable{

    private final JProgressBar barrita;
    private final JLabel etiqueta;
    private final Thread h;
    Usuario u;
    
    public Cargando(Usuario u) {
        
        this.setSize(400, 100);
        this.setResizable(false);
        this.setTitle("Cargando ...");
        this.setLocationRelativeTo(null);
        barrita = new JProgressBar(0, 10);
        barrita.setValue(0);
        barrita.setBounds(10, 10, 370, 20);
        barrita.setBackground(Constantes.colorLight);
        
        Container contenedor = this.getContentPane();
        contenedor.setLayout(null);
        contenedor.add(barrita);
        
        etiqueta = new JLabel("Cargando ");
        etiqueta.setBounds(150, 35, 100, 20);
        etiqueta.setFont(Constantes.fontBold);
        contenedor.add(etiqueta);
        
        this.u = u;
        h = new Thread(this);
        h.start();
    }

    @Override
    public void run() {
        for (int i=0;i<=10;i++){
            barrita.setValue(i);
            etiqueta.setText(etiqueta.getText().equals("Cargando ...") ? "Cargando " : etiqueta.getText().concat("."));
            try {
                h.sleep(300);
            } catch (InterruptedException ex) {
                
            }
            
        }
        this.dispose();
        u.setSesionActiva(true);
        Principal ventanaPrincipal = new Principal();
        
        ventanaPrincipal.setVisible(true);
    }
    
}
