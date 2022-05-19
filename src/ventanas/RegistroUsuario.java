package ventanas;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

//Clases del componente lógico
import logica.ListaUsuarios;
import logica.Usuario;
import logica.Validacion;

/**
 *
 * @author pablo erick ramirez cruz
 */
public class RegistroUsuario extends JFrame {
    
    private Container contenedor = this.getContentPane();
    private JTextField userField, passField, passConfirmField;
    private JRadioButton admiRadio, subjefeRadio, registradorRadio;
    private JButton registrar, borrar;
    private ButtonGroup opciones;
    private JFrame contexto = this;
    
    public RegistroUsuario() {
        
        this.setSize(400, 350);
        this.setTitle("Registro de Usuario");
        //this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(Constantes.icon.getImage());
        contenedor.setLayout(null);
        
        JLabel userLabel = new JLabel("Nombre de usuario:");
        userLabel.setBounds(25, 60, 160, 20);
        userLabel.setFont(Constantes.fontBold);
        contenedor.add(userLabel);
        
        userField = new JTextField();
        userField.setBounds(200, 60, 165, 20);
        userField.setFont(Constantes.fontPlain);
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Validacion.escribirSoloTexto(e);
            }
            
        });
        
        contenedor.add(userField);
        
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setBounds(25, 100, 150, 20);
        passLabel.setFont(Constantes.fontBold);
        contenedor.add(passLabel);
        
        passField = new JTextField();
        passField.setBounds(200, 100, 165, 20);
        passField.setFont(Constantes.fontPlain);
        contenedor.add(passField);
        
        JLabel passConfirmLabel = new JLabel("Confirmar contraseña:");
        passConfirmLabel.setBounds(25, 140, 180, 20);
        passConfirmLabel.setFont(Constantes.fontBold);
        contenedor.add(passConfirmLabel);
        
        passConfirmField = new JTextField();
        passConfirmField.setBounds(200, 140, 165, 20);
        passConfirmField.setFont(Constantes.fontPlain);
        contenedor.add(passConfirmField);
        
        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setBounds(25, 180, 180, 20);
        rolLabel.setFont(Constantes.fontBold);
        contenedor.add(rolLabel);
        
        opciones = new ButtonGroup();
        admiRadio = new JRadioButton("ADMINISTRADOR");
        admiRadio.setActionCommand("ADMINISTRADOR");
        opciones.add(admiRadio);
        subjefeRadio = new JRadioButton("SUBJEFE");
        subjefeRadio.setActionCommand("SUBJEFE");
        opciones.add(subjefeRadio);
        registradorRadio = new JRadioButton("REGISTRADOR");
        registradorRadio.setActionCommand("REGISTRADOR");
        opciones.add(registradorRadio);
        
        admiRadio.setToolTipText("Tiene todos los permisos disponibles (Modificar usuarios, trabajadores, sueldos, faltas, etc.)");
        subjefeRadio.setToolTipText("Puede modificar faltas, retardos, datos de los trabajadores");
        registradorRadio.setToolTipText("Solo puede moficar retardos y faltas");
        
        admiRadio.setBounds(60, 180, 125, 20);
        subjefeRadio.setBounds(185, 180, 80, 20);
        registradorRadio.setBounds(265, 180, 120, 20);
        
        contenedor.add(admiRadio);
        contenedor.add(subjefeRadio);
        contenedor.add(registradorRadio);
        
        registrar = new JButton("REGISTRAR USUARIO");
        registrar.setBounds(100, 230, 200, 30);
        registrar.setBackground(Constantes.colorPrincipal);
        registrar.setForeground(Color.WHITE);
        registrar.addMouseListener(new ButtonHover(registrar, ButtonHover.BACKGROUND));
        registrar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                //Realizar validaciones

                //Valida campos vacios
                Object matriz[][] = {
                    {userField, passField, passConfirmField},
                    {"Debe ingresar un nombre de usuario", "Debe ingresar una contraseña", "Debe confirmar su contraseña"}
                };
                String errores = Validacion.comprobarVacios(matriz);
                if (!errores.isEmpty()) {
                    JOptionPane.showMessageDialog(contexto, errores,"ERROR",JOptionPane.WARNING_MESSAGE);
                } else {
                    // Valida contraseña
                    if (!passField.getText().isEmpty() && passField.getText().equals(passConfirmField.getText())) {
                        
                        if (Validacion.comprobarContraseña(passConfirmField.getText())) {

                            //Registrar el usuario
                            String nombre = userField.getText();
                            char[] pass = passConfirmField.getText().toCharArray();
                            String rol = opciones.getSelection().getActionCommand();
                            Usuario nuevo = new Usuario(nombre, pass, rol);
                            
                            System.out.println(nuevo.toString());
                            
                            ListaUsuarios.lista.add(nuevo);
                            try {
                                ListaUsuarios.saveData();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(contexto, "No se pudo guardar","ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                            
                            if (ListaUsuarios.lista.size() == 1) {
                                //Se acaba de crear el primer usuario, entonces se abre el programa por primera vez y loggearse
                                Login login = new Login();
                                login.setVisible(true);
                            }
                            setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(contexto, "Su contraseña debe tener de 8 a 16 caracteres, incluyendo mayusculas, minusculas y números","CONTRASEÑA INSEGURA", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(contexto, "Las contraseñas no coinciden, compruebe los campos", "CONTRASEÑAS NO IGUALES", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            
        });
        contenedor.add(registrar);
        
        if (ListaUsuarios.lista.isEmpty()) {
            JLabel mensaje = new JLabel("<html>No hay usuarios registrados debe crear un usuario<br>administrador</html>");
            mensaje.setBounds(25, 5, 380, 40);
            mensaje.setFont(Constantes.fontPlain);
            mensaje.setForeground(Color.RED);
            contenedor.add(mensaje);
            admiRadio.setSelected(true);
            subjefeRadio.setEnabled(false);
            registradorRadio.setEnabled(false);
        } else { //Si ya hay usuarios, se mostrará una lista con ellos y botones de agregar, modificar y borrar
            this.setSize(500, 400);
            
            userLabel.setBounds(20, 20, 160, 20);
            userField.setBounds(20, 40, 250, 20);
            passLabel.setBounds(20, 70, 150, 20);
            passField.setBounds(20, 90, 250, 20);
            passConfirmLabel.setBounds(20, 120, 180, 20);
            passConfirmField.setBounds(20, 140, 250, 20);
            rolLabel.setBounds(20, 170, 180, 20);
            admiRadio.setBounds(20, 190, 125, 20);
            subjefeRadio.setBounds(20, 210, 80, 20);
            registradorRadio.setBounds(20, 230, 120, 20);
            registrar.setBounds(50, 280, 200, 30);
            
            JLabel listaTitulo = new JLabel("Usuarios Registrados", JLabel.CENTER);
            listaTitulo.setBounds(320, 20, 140, 30);
            listaTitulo.setFont(Constantes.fontPlain);
            contenedor.add(listaTitulo);
            
            DefaultListModel modelo = new DefaultListModel();
            JList lista = new JList(modelo);
            for (int i = 0; i < ListaUsuarios.lista.size(); i++) {
                modelo.add(i, ListaUsuarios.lista.get(i).getNombre() + " " + ListaUsuarios.lista.get(i).getRol());
            }
            
            lista.setBounds(310, 50, 160, 220);
            lista.setBorder(BorderFactory.createEtchedBorder());
            lista.setFont(Constantes.fontPlain);
            contenedor.add(lista);
            
            borrar = new JButton("Borrar");
            borrar.setBounds(340, 280, 100, 30);
            borrar.setBackground(Constantes.colorPrincipal);
            borrar.setForeground(Color.WHITE);
            borrar.addMouseListener(new ButtonHover(borrar, ButtonHover.BACKGROUND));
            borrar.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    if (lista.getSelectedIndex() == -1) { //Si el usuario no selecciona ningun elemento
                        return;
                        
                    } else if (ListaUsuarios.lista.size() == 1) {
                        JOptionPane.showMessageDialog(null, "Debe haber minimo 1 usuario registrado", "NO SE PUEDE BORRAR", JOptionPane.WARNING_MESSAGE);
                        
                    }else if(ListaUsuarios.getUsuarioActivo().getNombre().equals(ListaUsuarios.get(lista.getSelectedIndex()).getNombre())) {
                        JOptionPane.showMessageDialog(null, "No puede borrarse a si mismo","IMPOSIBLE", JOptionPane.WARNING_MESSAGE);
                    }else{
                        ListaUsuarios.eliminar(lista.getSelectedIndex());
                        modelo.remove(lista.getSelectedIndex());
                    }
                }
            });
            contenedor.add(borrar);
        }
    }
    
}
