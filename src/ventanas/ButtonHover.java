
package ventanas;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author pablo Erick Ramírez Cruz
 */
public class ButtonHover extends MouseAdapter {
    
    
    private JButton button;
    private Color originalColor;
    private int type;
    public final static int BACKGROUND = 0, FOREGROUND = 1;
    
    public ButtonHover(JButton button, int type){
        this.type = type;
        if (type != BACKGROUND && type != FOREGROUND){
            throw new IllegalArgumentException();
        }
        this.button=button;
        if (type == BACKGROUND){
            originalColor=button.getBackground();
        }else if(type == FOREGROUND){
            originalColor=button.getForeground();  
        }
        
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        if (type == BACKGROUND){
            button.setBackground(Constantes.colorAcent);
        }else if(type == FOREGROUND){
            button.setForeground(Constantes.colorAcent);    
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        if (type == BACKGROUND){
            button.setBackground(originalColor);
        }else if(type == FOREGROUND){
            button.setForeground(originalColor);    
        }
    }
}
