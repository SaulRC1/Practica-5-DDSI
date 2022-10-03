/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Vista.VistaMensajes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author usuario
 */
public class SocioDAO {
    
    private Session conexion;
    

    public SocioDAO(Session conexion) {
        this.conexion = conexion;
    }
    
    public ArrayList<Socio> listaSocios() throws SQLException {
        
        Transaction transaction = conexion.beginTransaction();
        ArrayList<Socio> listaSocios = new ArrayList<Socio>();

        String consulta = "SELECT * FROM SOCIO";
        
        Query consult = conexion.createNativeQuery(consulta, Socio.class);
        
        listaSocios = (ArrayList<Socio>) consult.list();
        
        transaction.commit();
       

        return listaSocios;
    }
    
    public ArrayList<Socio> listaSocioPorLetra(String letra) throws SQLException {
        
        Transaction transaction = conexion.beginTransaction();
        ArrayList<Socio> listaSocios = new ArrayList<Socio>();
        letra += "%";
        Query consulta = conexion.createNativeQuery("SELECT * FROM SOCIO WHERE "
                + "nombre LIKE :letra", Socio.class).setParameter("letra", letra);
        
        listaSocios = (ArrayList<Socio>) consulta.list();
        
        transaction.commit();
        
        return listaSocios;
    }
    
    public void insertarSocio(Socio s){
        Transaction transaction = this.conexion.beginTransaction();;
        try {
            this.conexion.save(s);
            transaction.commit();
        } catch (Exception e) {
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
            transaction.rollback();
        }
        
    }
    
    public void eliminarSocio(String numSocio){
        Transaction transaction = this.conexion.beginTransaction();
        
        try {
            Socio socioEliminar = this.conexion.get(Socio.class, numSocio);
            this.conexion.delete(socioEliminar);
            transaction.commit();
        } catch (Exception e) {
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
            transaction.rollback();
        }
    }
    
    public void actualizaSocio(Socio s){
        this.insertarSocio(s);
    }
    
    public String mayorNumeroSocio(){
        
        String mayor = "";
        
        try {
            int mayorNumSoc = 0;
            ArrayList<Socio> socios = this.listaSocios();
            
            for (int i = 0; i < socios.size(); i++) {
                int numSoc = Integer.parseInt(socios.get(i).getNumerosocio().split("S")[1]);
                if(mayorNumSoc < numSoc){
                    mayorNumSoc = numSoc;
                }
            }
            
            if(mayorNumSoc <= 99){
                mayor = "S0" + (mayorNumSoc + 1);
            } else if(mayorNumSoc <= 9){
                mayor = "S00" + (mayorNumSoc + 1);
            } else {
                mayor = "S" + (mayorNumSoc + 1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SocioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return mayor;
    }
    
    
}
