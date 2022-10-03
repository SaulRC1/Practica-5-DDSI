/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aplicacion;

import Controlador.ControladorLogin;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author SaulRC1
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            for (LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }          
            }
        } catch (Exception e) {
        }
        ControladorLogin cLogin = new ControladorLogin();
        
    }
    
}
