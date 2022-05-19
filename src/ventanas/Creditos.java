
package ventanas;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author pablo erick ramirez cruz
 */
public class Creditos extends JDialog{
    
    

    public Creditos(JFrame padre){
        
        super(padre);
        this.setSize(600,400);
        this.setResizable(false);
        this.setTitle("Acerca de");
        this.setLocationRelativeTo(padre);
        Container container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(Constantes.colorLight);
        
        JLabel imagen = new JLabel("");
        imagen.setIcon(new ImageIcon(Constantes.creador.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        imagen.setBounds(40, 80, 200, 200);
        container.add(imagen);
        
        JLabel nombre = new JLabel("Pablo Erick Ramírez Cruz", JLabel.CENTER);
        nombre.setFont(Constantes.fontBold);
        nombre.setBounds(250, 100, 300, 30);
        container.add(nombre);
        
        JLabel info = new JLabel("<html>Estudiante de Ing. en Sistemas Computacionales, programador de JAVA y JavaScript. Puedes consultar mis proyectos en el link de abajo.</html>");
        info.setFont(Constantes.fontPlain);
        info.setBounds(300, 130, 250, 90);
        container.add(info);
        
        
        
        JButton boton = new JButton("");
        boton.setIcon(new ImageIcon(Constantes.github.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        boton.setBackground(null);
        boton.setBorder(null);
        boton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    open();
                } catch (URISyntaxException ex) {
                    
                }
            }
        });
        boton.setBounds(390, 225, 30, 30);
        boton.setCursor(Constantes.cursorMano);
        container.add(boton);
    }
    
    private static void open() throws URISyntaxException{
        if (Desktop.isDesktopSupported()){
            try{
                Desktop.getDesktop().browse(new URI("http://github.com/BlopaE"));
            }catch(IOException e){
                
            }
        }
    }
}
