/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.ActividadDAO;
import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.PanelMonitor;
import Vista.PanelSocio;
import Vista.PanelVacio;
import Vista.VistaActividades;
import Vista.VistaConsola;
import Vista.VistaInscripcionSocios;
import Vista.VistaMensajes;
import Vista.VistaPrincipal;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author usuario
 */
public class Controlador implements ActionListener {

    private Session conexion_bd;
    private boolean desconexionOK;
    private VistaPrincipal vPrincipal = null;
    private VistaConsola vConsola;
    private VistaMensajes vMensajes;
    private PanelMonitor vMonitor;
    private PanelSocio vSocio;
    private PanelVacio vVacio;
    private ControladorVistaInscripcionSocios vAdministrarSocios;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private VistaActividades vActividades;
    ArrayList<Monitor> ListaMonitores;
    ArrayList<Socio> ListaSocios;
    ArrayList<Socio> filtroSocios;

    public Controlador(Session conexion_bd) {

        this.conexion_bd = conexion_bd;
        vConsola = new VistaConsola();
        vPrincipal = new VistaPrincipal();
        vMensajes = new VistaMensajes();
        vMonitor = new PanelMonitor();
        vSocio = new PanelSocio();
        vVacio = new PanelVacio();
        vActividades = new VistaActividades();
        filtroSocios = null;

        //addListeners();
        //JOptionPane.showMessageDialog(vPrincipal, "Llego al controlador");
        vPrincipal.mi_GestionMonitores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                vPrincipalmi_GestionMonitoresactionPerformed(ev);
            }
        });

        vPrincipal.mi_GestionSocios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                vPrincipalmi_GestionSociosactionPerformed(ev);
            }
        });

        vPrincipal.mi_Salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                vPrincipalmi_SaliractionPerformed(ev);
            }
        });

        vMonitor.tblMonitor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                vMonitortblMonitorMouseClicked(evt);
            }
        });

        vMonitor.btnInsertar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vMonitor_btnInsertarMouseClicked(evt);

            }
        });

        vMonitor.btnEliminar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                vMonitor_btnEliminarMouseClicked(evt);

            }
        });

        vMonitor.btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vMonitor_btnActualizarMouseClicked(evt);
            }
        });

        vMonitor.btnVaciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vaciarTablaMonitores();
            }
        });

        vMonitor.btnListar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                rellenarTablaMonitores(ListaMonitores);
            }
        });

        vPrincipal.mi_SociosInscritos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                mi_SociosInscritosactionPerformed(ev);
            }
        });

        vPrincipal.miAdministrarSocios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                miAdministrarSociosActionPerformed(ev);
            }
        });

        vActividades.btnMostrar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                vActividadesbtnMostrarMouseClicked(evt);
            }
        });

        vSocio.txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                vSocio_txtBuscarKeyReleased(e);
            }
        });

        vSocio.btnInsertar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                vSocio_btnInsertarMouseClicked(evt);
            }
        });

        vSocio.tblSocio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vSocio_tblSocioMouseClicked(evt);
            }
        });

        vSocio.btnEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vSocio_btnEliminarMouseClicked(evt);
            }
        });

        vSocio.btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vSocio_btnActualizarMouseClicked(evt);
            }
        });

        vSocio.btnVaciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (ListaSocios != null) {
                    ListaSocios.clear();
                }

                vaciarTablaSocios();
            }
        });

        vSocio.btnListar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                try {
                    SocioDAO socios = new SocioDAO(conexion_bd);

                    ListaSocios = socios.listaSocios();
                    rellenarTablaSocios(ListaSocios);
                } catch (SQLException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        vPrincipal.setLocationRelativeTo(null);
        vPrincipal.getContentPane().setLayout(new CardLayout());
        vPrincipal.add(vVacio);
        vPrincipal.add(vSocio);
        vPrincipal.add(vMonitor);
        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vVacio.setVisible(true);

        vPrincipal.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*switch (e.getActionCommand()) {
            case "Cerrar":
                this.desconexionOK = this.desconectar();

                if (this.desconexionOK) {
                    //JOptionPane.showMessageDialog(vPrincipal, "Llego al controlador");
                    this.vMensajes.MensajeInformacion("Desconectado con exito!");
                    this.vPrincipal.dispose();
                    this.vConsola.vistaConsolaLogin("Desconectado con exito!");
                    
                }
                else{
                    
                }
            break;

        }*/

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

    private void addListeners() {
        //vPrincipal.btn_Cerrar.addActionListener(this);
    }

    /**
     *
     */
    public DefaultTableModel modeloTablaMonitores = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    };

    public DefaultTableModel modeloTablaSocios = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public DefaultTableModel modeloTablaActividades = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public void dibujarTablaMonitores(PanelMonitor vMonitor) {
        vMonitor.tblMonitor.setModel(modeloTablaMonitores);

        String[] columnasTabla = {"Codigo", "Nombre", "DNI", "Telefono", "Correo", "Fecha Incorporacion", "Nick"};
        modeloTablaMonitores.setColumnIdentifiers(columnasTabla);

        vMonitor.tblMonitor.getTableHeader().setResizingAllowed(false);
        vMonitor.tblMonitor.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vMonitor.tblMonitor.getColumnModel().getColumn(0).setPreferredWidth(40);
        vMonitor.tblMonitor.getColumnModel().getColumn(1).setPreferredWidth(240);
        vMonitor.tblMonitor.getColumnModel().getColumn(2).setPreferredWidth(70);
        vMonitor.tblMonitor.getColumnModel().getColumn(3).setPreferredWidth(70);
        vMonitor.tblMonitor.getColumnModel().getColumn(4).setPreferredWidth(200);
        vMonitor.tblMonitor.getColumnModel().getColumn(5).setPreferredWidth(150);
        vMonitor.tblMonitor.getColumnModel().getColumn(6).setPreferredWidth(60);

    }

    public void dibujarTablaSocios(PanelSocio vSocio) {
        vSocio.tblSocio.setModel(modeloTablaSocios);
        String[] columnasTabla = {"Num Socio", "Nombre", "DNI", "Telefono",
            "Correo", "Fecha de Nacimiento", "Fecha de Incorporacion", "Categoria"};

        modeloTablaSocios.setColumnIdentifiers(columnasTabla);

        vSocio.tblSocio.getTableHeader().setResizingAllowed(false);
        vSocio.tblSocio.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vSocio.tblSocio.getColumnModel().getColumn(0).setPreferredWidth(40);
        vSocio.tblSocio.getColumnModel().getColumn(1).setPreferredWidth(240);
        vSocio.tblSocio.getColumnModel().getColumn(2).setPreferredWidth(70);
        vSocio.tblSocio.getColumnModel().getColumn(3).setPreferredWidth(70);
        vSocio.tblSocio.getColumnModel().getColumn(4).setPreferredWidth(200);
        vSocio.tblSocio.getColumnModel().getColumn(5).setPreferredWidth(150);
        vSocio.tblSocio.getColumnModel().getColumn(6).setPreferredWidth(150);
        vSocio.tblSocio.getColumnModel().getColumn(7).setPreferredWidth(50);
        //vSocio.tblSocio.getColumnModel().getColumn(8).setPreferredWidth(50);
        //vMonitor.tblMonitor.getColumnModel().getColumn(6).setPreferredWidth(60);

    }

    public void dibujarTablaActividades(VistaActividades vActividades) {
        vActividades.tblActividad.setModel(modeloTablaActividades);
        String[] columnasTabla = {"Nombre de Socio", "Correo de Socio"};
        modeloTablaActividades.setColumnIdentifiers(columnasTabla);
        vActividades.tblActividad.getTableHeader().setResizingAllowed(false);
        vActividades.tblActividad.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    public void rellenarTablaMonitores(ArrayList<Monitor> monitores) {

        Object[] fila = new Object[7];

        for (int i = 0; i < monitores.size(); i++) {

            fila[0] = monitores.get(i).getCodmonitor();
            fila[1] = monitores.get(i).getNombre();
            fila[2] = monitores.get(i).getDni();
            fila[3] = monitores.get(i).getTelefono();
            fila[4] = monitores.get(i).getCorreo();
            fila[5] = monitores.get(i).getFechaentrada();
            fila[6] = monitores.get(i).getNick();
            modeloTablaMonitores.addRow(fila);

        }

    }

    public void rellenarTablaSocios(ArrayList<Socio> socios) {
        Object[] fila = new Object[8];

        for (int i = 0; i < socios.size(); i++) {
            fila[0] = socios.get(i).getNumerosocio();
            fila[1] = socios.get(i).getNombre();
            fila[2] = socios.get(i).getDni();
            fila[3] = socios.get(i).getTelefono();
            fila[4] = socios.get(i).getCorreo();
            fila[5] = socios.get(i).getFechanacimiento();
            fila[6] = socios.get(i).getFechaentrada();
            fila[7] = socios.get(i).getCategoria();

            modeloTablaSocios.addRow(fila);
        }
    }

    public void vaciarTablaMonitores() {
        while (modeloTablaMonitores.getRowCount() > 0) {
            modeloTablaMonitores.removeRow(0);
        }
    }

    public void vaciarTablaSocios() {
        while (modeloTablaSocios.getRowCount() > 0) {
            modeloTablaSocios.removeRow(0);
        }
    }

    public void vaciarTablaActividades() {
        while (modeloTablaActividades.getRowCount() > 0) {
            modeloTablaActividades.removeRow(0);
        }
    }

    private void vPrincipalmi_GestionMonitoresactionPerformed(ActionEvent evt) {

        System.out.println("Clicked");

        vSocio.setVisible(false);
        vVacio.setVisible(false);
        this.dibujarTablaMonitores(vMonitor);
        MonitorDAO mi_monitor = new MonitorDAO(this.conexion_bd);
        try {
            this.ListaMonitores = mi_monitor.listaMonitores();
            this.vaciarTablaMonitores();
            this.rellenarTablaMonitores(ListaMonitores);
        } catch (SQLException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        vMonitor.setVisible(true);

    }

    private void vMonitortblMonitorMouseClicked(MouseEvent evt) {
        int fila = vMonitor.tblMonitor.getSelectedRow();

        Monitor seleccionado = this.ListaMonitores.get(fila);

        vMonitor.txtCodigo.setText(seleccionado.getCodmonitor());
        vMonitor.txtCorreo.setText(seleccionado.getCorreo());
        vMonitor.txtDNI.setText(seleccionado.getDni());
        vMonitor.txtNick.setText(seleccionado.getNick());
        vMonitor.txtNombre.setText(seleccionado.getNombre());
        vMonitor.txtTelefono.setText(seleccionado.getTelefono());
        //vMonitor.txtFecha.setText(seleccionado.getFechaEntrada());
        String fecha = seleccionado.getFechaentrada();
        try {
            if (fecha != null) {
                Date fechaChooser = formatoFecha.parse(fecha);
                vMonitor.date_fecha.setDate(fechaChooser);
            } else {
                vMonitor.date_fecha.setDate(null);
            }

        } catch (ParseException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void vPrincipalmi_SaliractionPerformed(ActionEvent ev) {

        System.exit(0);

    }

    private void vPrincipalmi_GestionSociosactionPerformed(ActionEvent ev) {
        vMonitor.setVisible(false);
        vVacio.setVisible(false);

        this.dibujarTablaSocios(vSocio);
        SocioDAO mi_socio = new SocioDAO(this.conexion_bd);
        try {
            this.ListaSocios = mi_socio.listaSocios();
            this.vaciarTablaSocios();
            this.rellenarTablaSocios(ListaSocios);
        } catch (SQLException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        //vMonitor.setVisible(true);
        vSocio.setVisible(true);
    }

    private void mi_SociosInscritosactionPerformed(ActionEvent ev) {

        this.dibujarTablaActividades(vActividades);
        ActividadDAO act = new ActividadDAO(this.conexion_bd);
        act.devolverActividades(vActividades.cbActividades);
        vActividades.setTitle("Socios Inscritos en Actividades");
        vActividades.setVisible(true);

    }

    private void vActividadesbtnMostrarMouseClicked(MouseEvent evt) {
        ActividadDAO miActividad = new ActividadDAO(this.conexion_bd);
        String Nombre_Actividad = (String) vActividades.cbActividades.getSelectedItem();
        String idActividad = miActividad.getIdActividad(Nombre_Actividad);
        this.vaciarTablaActividades();
        miActividad.devolverSocios(idActividad, modeloTablaActividades);

    }

    private void miAdministrarSociosActionPerformed(ActionEvent ev) {
        vAdministrarSocios = new ControladorVistaInscripcionSocios(this.conexion_bd);
    }

    private void vSocio_txtBuscarKeyReleased(KeyEvent e) {
        String filter = vSocio.txtBuscar.getText();

        if (!filter.isEmpty()) {
            if (filtroSocios != null) {
                this.filtroSocios.clear();
            } else {
                filtroSocios = new ArrayList<>();
            }

            this.vaciarTablaSocios();

            for (Socio s : this.ListaSocios) {
                if (s.getNombre().toLowerCase().contains(filter.toLowerCase())) {
                    this.filtroSocios.add(s);
                }
            }

            this.rellenarTablaSocios(filtroSocios);

        } else {
            this.vaciarTablaSocios();
            this.rellenarTablaSocios(ListaSocios);
        }
    }

    private void vSocio_btnInsertarMouseClicked(MouseEvent evt) {

        SocioDAO socioDao = new SocioDAO(conexion_bd);
        try {

            String numSocio = socioDao.mayorNumeroSocio();
            System.out.println("NumSocio: " + numSocio);
            String nombre = vSocio.txtNombre.getText();
            String dni = vSocio.txtDNI.getText();
            Date fechaChooser = vSocio.date_Nacimiento.getDate();
            String fechaNac = "";
            if (fechaChooser != null) {
                fechaNac = formatoFecha.format(fechaChooser);
            }
            String telefono = vSocio.txtTelefono.getText();
            String correo = vSocio.txtCorreo.getText();
            String fechaEntrada = "";
            fechaChooser = vSocio.date_fecha.getDate();
            if (fechaChooser != null) {
                fechaEntrada = formatoFecha.format(fechaChooser);
            }
            Character categoria = vSocio.txtCategoria.getText().toCharArray()[0];

            Socio nuevoSocio = new Socio(numSocio, nombre, dni, fechaNac, telefono, correo, fechaEntrada, categoria);
            socioDao.insertarSocio(nuevoSocio);
            this.vaciarTablaSocios();
            ListaSocios = socioDao.listaSocios();
            this.rellenarTablaSocios(ListaSocios);

        } catch (Exception e) {
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void vSocio_tblSocioMouseClicked(MouseEvent evt) {
        int fila = vSocio.tblSocio.getSelectedRow();
        String numSoc = (String) vSocio.tblSocio.getValueAt(fila, 0);
        Socio seleccionado = this.conexion_bd.get(Socio.class, numSoc);

        vSocio.txtNumSocio.setText(seleccionado.getNumerosocio());
        vSocio.txtNombre.setText(seleccionado.getNombre());
        vSocio.txtDNI.setText(seleccionado.getDni());
        vSocio.txtTelefono.setText(seleccionado.getTelefono());
        vSocio.txtCorreo.setText(seleccionado.getCorreo());
        vSocio.txtCategoria.setText(seleccionado.getCategoria().toString());

        String fecha = seleccionado.getFechaentrada();

        try {
            Date fechaChooser = formatoFecha.parse(fecha);
            vSocio.date_fecha.setDate(fechaChooser);
            fecha = seleccionado.getFechanacimiento();
            fechaChooser = formatoFecha.parse(fecha);
            vSocio.date_Nacimiento.setDate(fechaChooser);
        } catch (ParseException e) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void vSocio_btnEliminarMouseClicked(MouseEvent evt) {
        String numSoc = vSocio.txtNumSocio.getText();

        if (!numSoc.isEmpty()) {
            try {
                SocioDAO sociodao = new SocioDAO(conexion_bd);
                sociodao.eliminarSocio(numSoc);
                this.vaciarTablaSocios();
                ListaSocios = sociodao.listaSocios();
                this.rellenarTablaSocios(ListaSocios);
            } catch (SQLException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void vSocio_btnActualizarMouseClicked(MouseEvent evt) {
        SocioDAO socioDao = new SocioDAO(conexion_bd);
        try {

            String numSocio = vSocio.txtNumSocio.getText();

            Socio actualizadoSocio = this.conexion_bd.get(Socio.class, numSocio);

            String nombre = vSocio.txtNombre.getText();
            String dni = vSocio.txtDNI.getText();
            Date fechaChooser = vSocio.date_Nacimiento.getDate();
            String fechaNac = "";
            if (fechaChooser != null) {
                fechaNac = formatoFecha.format(fechaChooser);
            }
            String telefono = vSocio.txtTelefono.getText();
            String correo = vSocio.txtCorreo.getText();
            String fechaEntrada = "";
            fechaChooser = vSocio.date_fecha.getDate();
            if (fechaChooser != null) {
                fechaEntrada = formatoFecha.format(fechaChooser);
            }
            Character categoria = vSocio.txtCategoria.getText().toCharArray()[0];

            actualizadoSocio.setNombre(nombre);
            actualizadoSocio.setCorreo(correo);
            actualizadoSocio.setFechaentrada(fechaEntrada);
            actualizadoSocio.setFechanacimiento(fechaNac);
            actualizadoSocio.setCategoria(categoria);
            actualizadoSocio.setDni(dni);
            actualizadoSocio.setTelefono(telefono);

            socioDao.actualizaSocio(actualizadoSocio);
            this.vaciarTablaSocios();
            ListaSocios = socioDao.listaSocios();
            this.rellenarTablaSocios(ListaSocios);

        } catch (Exception e) {
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void vMonitor_btnInsertarMouseClicked(MouseEvent evt) {
        Monitor insertar;
        MonitorDAO monitorDAO = new MonitorDAO(conexion_bd);

        try {
            String codMonitor = monitorDAO.mayorCodigoMonitor();
            String nombre = vMonitor.txtNombre.getText();
            String dni = vMonitor.txtDNI.getText();
            String telefono = vMonitor.txtTelefono.getText();
            String correo = vMonitor.txtCorreo.getText();
            Date fechaChooser = vMonitor.date_fecha.getDate();
            String fechaEntrada = "";
            if (fechaChooser != null) {
                fechaEntrada = formatoFecha.format(fechaChooser);
            }
            //String fechaEntrada = vMonitor.txtFecha.getText();
            String nick = vMonitor.txtNick.getText();

            insertar = new Monitor(codMonitor, nombre, dni, telefono, correo, fechaEntrada, nick);

            monitorDAO.insertarMonitor(insertar);

            this.vaciarTablaMonitores();
            ListaMonitores = monitorDAO.listaMonitores();
            this.rellenarTablaMonitores(ListaMonitores);

        } catch (Exception e) {

            VistaMensajes.MensajeDeError("Inserte valores correctos en las celdas: ", e.getMessage());
        }
    }

    private void vMonitor_btnEliminarMouseClicked(MouseEvent evt) {

        try {
            String codMonitor = vMonitor.txtCodigo.getText();
            int opcion = JOptionPane.showConfirmDialog(null, "¿Quiere eliminarlo?");
            System.out.println("Opcion: " + opcion);

            if (opcion == 0) {

                MonitorDAO mi_monitor = new MonitorDAO(conexion_bd);
                mi_monitor.eliminarMonitor(codMonitor);

                vaciarTablaMonitores();
                ListaMonitores = mi_monitor.listaMonitores();
                rellenarTablaMonitores(ListaMonitores);

            }

        } catch (Exception e) {
            VistaMensajes.MensajeDeError("Ha ocurrido un error: ", e.getMessage());
        }
    }

    private void vMonitor_btnActualizarMouseClicked(MouseEvent evt) {
        try {
            String codMonitor = vMonitor.txtCodigo.getText();

            Monitor actualizaMonitor = this.conexion_bd.get(Monitor.class, codMonitor);

            String nombre = vMonitor.txtNombre.getText();
            String dni = vMonitor.txtDNI.getText();
            String correo = vMonitor.txtCorreo.getText();
            String telefono = vMonitor.txtTelefono.getText();
            String nick = vMonitor.txtNick.getText();
            String fecha = "";
            Date fechaChooser = vMonitor.date_fecha.getDate();

            if (fechaChooser != null) {
                fecha = formatoFecha.format(fechaChooser);
            }

            actualizaMonitor.setCorreo(correo);
            actualizaMonitor.setDni(dni);
            actualizaMonitor.setFechaentrada(fecha);
            actualizaMonitor.setTelefono(telefono);
            actualizaMonitor.setNick(nick);
            actualizaMonitor.setNombre(nombre);

            MonitorDAO monDAO = new MonitorDAO(conexion_bd);
            monDAO.actualizarMonitor(actualizaMonitor);
            this.vaciarTablaMonitores();
            ListaMonitores = monDAO.listaMonitores();
            this.rellenarTablaMonitores(ListaMonitores);

        } catch (Exception e) {
            VistaMensajes.MensajeDeError("ERROR", e.getMessage());
        }
    }

}
