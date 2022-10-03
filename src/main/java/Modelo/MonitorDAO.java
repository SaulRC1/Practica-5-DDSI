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
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


/*
create table MONITOR (
    codMonitor char(4),
    nombre varchar(300) not null,
    dni varchar(9) not null,
    telefono varchar(9),
    correo varchar(50),
    fechaEntrada varchar(10),
    nick varchar(6),
    CONSTRAINT CP_Monitor PRIMARY KEY (codMonitor));
 */
/**
 *
 * @author usuario
 */
public class MonitorDAO {

    private Session conexion;
    private PreparedStatement ps;

    public MonitorDAO(Session c) {
        this.conexion = c;
    }

    public ArrayList<Monitor> listaMonitores() throws SQLException {

        ArrayList<Monitor> listaMonitores = new ArrayList<Monitor>();
        Transaction transaction = conexion.beginTransaction();

        Query consulta = conexion.createNativeQuery("SELECT * FROM MONITOR WHERE codmonitor not like 'M999'", Monitor.class);
        listaMonitores = (ArrayList<Monitor>) consulta.list();

        transaction.commit();

        return listaMonitores;
    }

    public ArrayList<Monitor> listaMonitorPorLetra(String letra) throws SQLException {

        ArrayList<Monitor> listaMonitores = new ArrayList<Monitor>();

        Transaction transaction = conexion.beginTransaction();
        letra = letra + "%";
        Query consulta = conexion.createNativeQuery("SELECT * FROM MONITOR "
                + "WHERE nombre LIKE :letra", Monitor.class).setParameter("letra", letra);
        listaMonitores = (ArrayList<Monitor>) consulta.list();

        return listaMonitores;
    }

    public void actualizarMonitor(Monitor update) {
        Transaction transaction = conexion.beginTransaction();

        try {
            conexion.save(update);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
        }

    }

    public void insertarMonitor(Monitor insert) throws SQLException {

        Transaction transaction = conexion.beginTransaction();
        try {
            conexion.save(insert);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
        }

    }

    public void eliminarMonitor(String codMonitor) throws SQLException {
        Transaction transaction = null;

        try {
            Monitor monitor = conexion.get(Monitor.class, codMonitor);

            if (monitor.getActividadesResponsables().isEmpty()) {
                transaction = conexion.beginTransaction();
                conexion.delete(monitor);
                transaction.commit();
            } else {
                Monitor defecto = conexion.get(Monitor.class, "M999");

                if (defecto == null) {
                    defecto = new Monitor("M999", "Responsable Genérico", "00000000A");
                    insertarMonitor(defecto);
                }
                transaction = conexion.beginTransaction();
                Set<Actividad> actividadesResponsable = monitor.getActividadesResponsables();

                Iterator<Actividad> iterador = actividadesResponsable.iterator();

                while (iterador.hasNext()) {
                    Actividad act = iterador.next();
                    act.actualizaMonitor(defecto);
                    conexion.save(act);
                }
                
                transaction.commit();
                
                transaction = conexion.beginTransaction();
                conexion.delete(monitor);
                transaction.commit();

            }
            
            

            
        } catch (Exception e) {
            transaction.rollback();
            Logger.getLogger(MonitorDAO.class.getName()).log(Level.SEVERE, null, e);
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
        }

    }

    public String mayorCodigoMonitor() {

        String mayor = "";

        try {
            int mayorCodMon = 0;
            ArrayList<Monitor> monitores = this.listaMonitores();

            for (int i = 0; i < monitores.size(); i++) {
                int numMon = Integer.parseInt(monitores.get(i).getCodmonitor().split("M")[1]);
                if (mayorCodMon < numMon && numMon != 999) {
                    mayorCodMon = numMon;
                }
            }

            if (mayorCodMon <= 99) {
                mayor = "M0" + (mayorCodMon + 1);
            } else if (mayorCodMon <= 9) {
                mayor = "M00" + (mayorCodMon + 1);
            } else {
                mayor = "M" + (mayorCodMon + 1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SocioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mayor;
    }
    
    public String buscaMayorCodigo(){
        String mayor = "";

        try {
            int mayorCodMon = 0;
            ArrayList<Monitor> monitores = this.listaMonitores();

            for (int i = 0; i < monitores.size(); i++) {
                int numMon = Integer.parseInt(monitores.get(i).getCodmonitor().split("M")[1]);
                if (mayorCodMon < numMon && numMon != 999) {
                    mayorCodMon = numMon;
                }
            }

            if (mayorCodMon <= 99) {
                mayor = "M0" + (mayorCodMon);
            } else if (mayorCodMon <= 9) {
                mayor = "M00" + (mayorCodMon);
            } else {
                mayor = "M" + (mayorCodMon);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SocioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mayor;
    }

}
