/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aplicacion;

/**
 *
 * @author SaulRC1
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String numSoc = "S010";
        
        String[] split = numSoc.split("S");
        
        
       
        
        System.out.println("Integer: " + Integer.parseInt(split[1]));
        
        String newNumber = "S0" + (Integer.parseInt(split[1]) + 1);
        
        System.out.println(newNumber);
        
        
    }
    
}
