/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import javax.swing.JOptionPane;

/**
 *
 * @author SaulRC1
 */
public class VistaMensajes {
    
    public static void MensajeDeError(String Texto, String mensaje){
        
        JOptionPane.showMessageDialog(null, Texto + "\n" + mensaje, "Error", 0);
        
    }
    
    public static void MensajeInformacion(String texto){
        JOptionPane.showMessageDialog(null, texto);
    }
    
}
