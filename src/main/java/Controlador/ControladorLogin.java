/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Modelo.HibernateUtil;
import Vista.VistaConsola;
import Vista.VistaLogin;
import Vista.VistaMensajes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.hibernate.Session;

/**
 *
 * @author SaulRC1
 */
public class ControladorLogin implements ActionListener {

    private Session conexion_bd;
    private VistaConsola vConsola = null;
    private VistaLogin vLogin = null;
    private boolean conexionOK;
    private boolean desconexionOK;
    private VistaMensajes vMensajes;

    public ControladorLogin() {

        /*this.conexionOK = conectar();

        if (this.conexionOK) {

            try {

                this.vista.vistaMetadatos(this.recuperar_metadatos());

            } catch (SQLException e) {
            }

        }

        this.desconexionOK = this.desconectar();*/
        vLogin = new VistaLogin();
        vConsola = new VistaConsola();
        vMensajes = new VistaMensajes();

        addListeners();

        vLogin.setLocationRelativeTo(null);
        vLogin.setVisible(true);

        vLogin.txt_IP.setText("172.17.20.75");
        vLogin.txt_Servicio.setText("rabida");
        vLogin.cb_Servidor.setSelectedIndex(0);
        vLogin.txt_Password.setText("DDSI_050");
        vLogin.txt_Usuario.setText("DDSI_050");
    }

    private boolean conectar() throws SQLException {

        boolean resultado = false;
        //this.conexion_bd = new Conexion();
        String ip = vLogin.txt_IP.getText();
        String service_bd = vLogin.txt_Servicio.getText();
        String usuario = vLogin.txt_Usuario.getText();
        String password = new String(vLogin.txt_Password.getPassword());
        String server = (String) vLogin.cb_Servidor.getSelectedItem();

        if ("oracle".equals(server.toLowerCase()) && ip.equals("172.17.20.75") && service_bd.equals("rabida") && usuario.equals("DDSI_050") && password.equals("DDSI_050")) {
            conexion_bd = HibernateUtil.getSessionFactory().openSession();
            this.vConsola.vistaConsolaLogin("Conexion Correcta... Enhorabuena!!");
            resultado = true;
        }

        return resultado;

    }

    private boolean desconectar() {

        boolean resultado = false;

        try {
            this.conexion_bd.clear();
            this.conexion_bd.close();
            this.vConsola.vistaConsolaLogin("Desonectado de la BD con exito!");
            resultado = true;
        } catch (Exception e) {
            this.vConsola.vistaConsolaLogin("Error al desconectarse de la BD", e.getMessage());
        }

        return resultado;

    }

    /*public DatabaseMetaData recuperar_metadatos() throws SQLException {

        return this.conexion_bd.informacionBD();
    }*/

    private void addListeners() {

        vLogin.btn_Conectar.addActionListener(this);
        vLogin.btn_Salir.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Conectar":
            
                try {
                conexionOK = conectar();
                Controlador c = new Controlador(conexion_bd);
                vLogin.dispose();
            } catch (SQLException ex) {
                this.vMensajes.MensajeDeError("Error en la conexion, revise parametros introducidos", ex.getMessage());
            }

            break;

            case "SalirDialogConexion":
                vLogin.dispose();
                System.exit(0);
                break;
        }
    }

}
