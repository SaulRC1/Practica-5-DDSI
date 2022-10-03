/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SaulRC1
 */
public class VistaConsola {

    public void vistaConsolaLogin(String texto) {
        
        System.out.println("****************************");
        System.out.println(texto);
        System.out.println("****************************");
        
    }

    public void vistaConsolaLogin(String texto, String error) {
        
        System.out.println("****************************");
        System.out.println(texto);
        System.out.println(error);
        System.out.println("****************************");
        
    }
    
    public void vistaMetadatos(DatabaseMetaData dbmd) {
        System.out.println("Mostrando metadatos de la base de datos");
        System.out.println("---------------------------------------");
        
        try {
            System.out.println("Version: " + dbmd.getDatabaseMajorVersion());
            System.out.println("URL: " + dbmd.getURL());
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Usuario: " + dbmd.getUserName());
            System.out.println("Max chars Username: " + dbmd.getMaxUserNameLength());
            System.out.println("Maximo de columnas por tabla: " + dbmd.getMaxColumnsInTable());
        } catch (SQLException ex) {
            Logger.getLogger(VistaConsola.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
