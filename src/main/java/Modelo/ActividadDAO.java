/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author usuario
 */
public class ActividadDAO {

    private Session conexion;
    private PreparedStatement ps;
    private CallableStatement stmt;

    public ActividadDAO(Session conexion) {
        this.conexion = conexion;
    }

    public void devolverActividades(JComboBox cb) {
        String consult = "SELECT NOMBRE FROM ACTIVIDAD";
        cb.removeAllItems();
        Transaction transaction = conexion.beginTransaction();
        
        Query consulta = conexion.createNativeQuery(consult);
        ArrayList<String> nombreActividades = (ArrayList<String>) consulta.list();
        
        for (int i = 0; i < nombreActividades.size(); i++) {
            cb.addItem(nombreActividades.get(i));
        }
        
        transaction.commit();

    }

    public void devolverSocios(String idActividad, DefaultTableModel Tabla) {
        Transaction transaction = conexion.beginTransaction();
        Query consulta = conexion.createNativeQuery("SELECT S.NOMBRE, S.CORREO "
                + "FROM SOCIO S INNER JOIN REALIZA R ON S.NUMEROSOCIO = R.NUMEROSOCIO "
                + "WHERE R.IDACTIVIDAD = :idA").setParameter("idA", idActividad);
        ArrayList<Object[]> socios = (ArrayList<Object[]>) consulta.list();
        transaction.commit();
        
        for (int i = 0; i < socios.size(); i++) {
            this.pintaTabla(Tabla, (String) socios.get(i)[0], (String) socios.get(i)[1]);
        }

    }

    public String getIdActividad(String NombreActividad) {
        String consulta = "SELECT IDACTIVIDAD FROM ACTIVIDAD WHERE NOMBRE = :nombre";
        String idActividad = "";
        
        Query consult = conexion.createNativeQuery(consulta).setParameter("nombre", NombreActividad);
        
        ArrayList<String> idActividades = (ArrayList<String>) consult.list();
        
        idActividad = idActividades.get(0);

        return idActividad;
    }

    public void pintaTabla(DefaultTableModel Tabla, String Nombre, String Correo) {
        Object[] fila = new Object[2];

        fila[0] = Nombre;
        fila[1] = Correo;

        Tabla.addRow(fila);

    }
    
    public ArrayList<Actividad> listaActividades(){
        String consult = "SELECT * FROM ACTIVIDAD";
        
        Transaction transaction = conexion.beginTransaction();
        
        Query consulta = conexion.createNativeQuery(consult, Actividad.class);
        ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list();
        
        transaction.commit();
        
        return actividades;
    }
}
