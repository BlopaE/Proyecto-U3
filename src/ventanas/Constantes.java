
package ventanas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import logica.Usuario;
/**
 *
 * @author pablo erick ramirez cruz
 */
public class Constantes {
    
    //Colores
    public static Color colorPrincipal = new Color(39, 76, 119);
    public static Color colorLight = new Color(231, 236, 239);
    public static Color colorAcent = new Color(96, 150, 186);
    
    //Fuentes
    public static Font fontBold = new Font("Arial",Font.BOLD,16);
    public static Font fontPlain = new Font("Arial",Font.PLAIN, 14);
    
    //Iconos
    public static ImageIcon icon = new ImageIcon(Constantes.class.getResource("icon.png"));
    public static ImageIcon logo = new ImageIcon(Constantes.class.getResource("logo.png"));
    public static ImageIcon nombre = new ImageIcon(Constantes.class.getResource("nombre.png"));
    public static ImageIcon sueldo = new ImageIcon(Constantes.class.getResource("sueldo.png"));
    public static ImageIcon retardo = new ImageIcon(Constantes.class.getResource("retardo.png"));
    public static ImageIcon descuento = new ImageIcon(Constantes.class.getResource("descuento.png"));
    public static ImageIcon creador = new ImageIcon(Constantes.class.getResource("creador.png"));
    public static ImageIcon github = new ImageIcon(Constantes.class.getResource("github.png"));


    //Cursores
    public static Cursor cursorMano = new Cursor(Cursor.HAND_CURSOR);
    
    
    private static File colorFile = new File("color.dat");
    private static File colorLightFile = new File("colorLight.dat");
    private static File colorAccentFile = new File("colorAccent.dat");
    
    public static void saveDataColorPrincipal() throws IOException{
        
        if (!colorFile.exists()){
            colorFile.createNewFile();
        }
        
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(colorFile));
        oos.writeObject(colorPrincipal);
    }
    
    public static Color loadDataColorPrincipal() throws IOException, ClassNotFoundException{
        
        if (!colorFile.exists()){
            colorFile.createNewFile();
        }else{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(colorFile));
            Color aux=null;
            aux= (Color) ois.readObject();
            return aux;
        }
        return new Color(39, 76, 119);
    }
    
    public static void saveDataColorLight() throws IOException{
        
        if (!colorLightFile.exists()){
            colorLightFile.createNewFile();
        }
        
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(colorLightFile));
        oos.writeObject(colorLight);
    }
    
    public static Color loadDataColorLight() throws IOException, ClassNotFoundException{
        
        if (!colorLightFile.exists()){
            colorLightFile.createNewFile();
        }else{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(colorLightFile));
            Color aux=null;
            aux= (Color) ois.readObject();
            return aux;
        }
        return new Color(231, 236, 239);
    }
    
    public static void saveDataColorAccent() throws IOException{
        
        if (!colorAccentFile.exists()){
            colorAccentFile.createNewFile();
        }
        
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(colorAccentFile));
        oos.writeObject(colorAcent);
    }
    
    public static Color loadDataColorAccent() throws IOException, ClassNotFoundException{
        
        if (!colorAccentFile.exists()){
            colorAccentFile.createNewFile();
        }else{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(colorAccentFile));
            Color aux=null;
            aux= (Color) ois.readObject();
            return aux;
        }
        return new Color(96, 150, 186);
    }
}
