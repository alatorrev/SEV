/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.Prospecto;
import com.sev.entity.ReporteHistorialContactos;
import com.sev.entity.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Universidad Politécnica Salesiana
 *
 * @author Axel Latorre, Jorge Castañeda Tutor: Ing. Vanessa Jurado
 *
 */
public class ContactoDetalleDAO {

    public int crearContactoDetalle(Usuario u, Prospecto p, int via, int interes, String observacion) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        ResultSet rs;
        int autoIncrementidContactoDetalle = 0;
        try {
            con.getConnection().setAutoCommit(false);
            String sqlInsert = "insert into DETALLECONTACTO(IDPROSPECTO,IDVIA,IDINTPROS,FECHACONTACTO,OBSERVACION,IDUSUARIO)"
                    + "values(?,?,?,GETDATE(),?,?)";
            PreparedStatement pst = con.getConnection().prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, p.getCedula());
            pst.setInt(2, via);
            pst.setInt(3, interes);
            pst.setString(4, observacion);
            pst.setString(5, u.getCedula());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                autoIncrementidContactoDetalle = rs.getInt(1);
                System.out.println(autoIncrementidContactoDetalle);
            }

            String sqlUpdateProspecto = "update prospecto set idcancap=?,nombres=?,apellidos=?,celular=?,casa=?,correo=?,"
                    + "establecimiento=?,fecha_modif=?,idintpros=?"
                    + " where cedula=?";
            pst = con.getConnection().prepareStatement(sqlUpdateProspecto);
            pst.setInt(1, p.getIdcanal());
            pst.setString(2, p.getNombres());
            pst.setString(3, p.getApellidos());
            pst.setString(4, p.getCelular());
            pst.setString(5, p.getCasa());
            pst.setString(6, p.getEmail());
            pst.setString(7, p.getEstablecimientoProveniente());
            pst.setString(8, sdf.format(new Date()));
            pst.setInt(9, interes);
            pst.setString(10, p.getCedula());
            pst.executeUpdate();
            con.getConnection().commit();
            BitacoraDAO daoBitacora = new BitacoraDAO();
            daoBitacora.crearRegistro("detallecontacto", "insert", u);
            daoBitacora.crearRegistro("prospecto", "Edit", u);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                con.getConnection().rollback();
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(ContactoDetalleDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return autoIncrementidContactoDetalle;
    }

    public List<ReporteHistorialContactos> listaHistorialContactos(Usuario u, Prospecto p, int via, int interes, Date desde, Date hasta) throws SQLException {
        Conexion con = new Conexion();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfParam = new SimpleDateFormat("yyyy-MM-dd");
        List<ReporteHistorialContactos> lista = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        String sql = "select U.CEDULA AS UCEDULA,U.APELLIDOS AS UAPELLIDOS,u.NOMBRES AS UNOMBRES,"
                + "P.CEDULA AS PCEDULA,P.APELLIDOS AS PAPELLIDOS,P.NOMBRES AS PNOMBRES,"
                + "V.IDVIA,V.DESCRIPCION,I.IDINTPROS,I.DESCRIPCION,DC.FECHACONTACTO "
                + "from DETALLECONTACTO DC INNER JOIN VIACOMUNICACION V "
                + "ON DC.IDVIA=V.IDVIA INNER JOIN INTERESPROSPECTO I ON I.IDINTPROS=DC.IDINTPROS "
                + "INNER JOIN PROSPECTO P ON DC.IDPROSPECTO=P.CEDULA INNER JOIN USUARIO U "
                + "ON U.CEDULA=DC.IDUSUARIO "
                + "where DC.IDUSUARIO =ISNULL(?,DC.IDUSUARIO) AND DC.IDPROSPECTO=ISNULL(?,DC.IDPROSPECTO) AND "
                + "DC.IDVIA = ISNULL(?,DC.IDVIA) AND DC.IDINTPROS = ISNULL(?,DC.IDINTPROS) AND "
                + "CAST(DC.FECHACONTACTO AS DATE) BETWEEN (?) and (?)";
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, u.getCedula());
            pst.setString(2, p.getCedula().trim().equals("") ? null : p.getCedula());
            if (via == 0) {
                pst.setNull(3, java.sql.Types.INTEGER);
            } else {
                pst.setInt(3, via);
            }
            if (interes == 0) {
                pst.setNull(4, java.sql.Types.INTEGER);
            } else {
                pst.setInt(4, interes);
            }
            pst.setString(5, sdfParam.format(desde));
            pst.setString(6, sdfParam.format(hasta));
            rs = pst.executeQuery();
            while (rs.next()) {
                ReporteHistorialContactos rhc = new ReporteHistorialContactos();
                rhc.setIdUsuario(rs.getString(1));
                rhc.setApellidosUsuario(rs.getString(2));
                rhc.setNombresUsuario(rs.getString(3));
                rhc.setIdProspecto(rs.getString(4));
                rhc.setApellidosProspecto(rs.getString(5));
                rhc.setNombresProspecto(rs.getString(6));
                rhc.setIdViaComunicacion(rs.getInt(7));
                rhc.setDescripcionVia(rs.getString(8));
                rhc.setIdInteres(rs.getInt(9));
                rhc.setDescripcioInteres(rs.getString(10));
                String fechaContacto = sdf.format(new Date(rs.getTimestamp(11).getTime()));
                rhc.setFechaContacto(sdf.parse(fechaContacto));
                lista.add(rhc);
            }
        } catch (Exception e) {
            System.out.println("DAO CONTACTODETALLE: " + e.getMessage());
        } finally {
            con.desconectar();
            System.out.println(lista.size());
        }
        return lista;
    }

    public List<ReporteHistorialContactos> latestProspectoWork(String idEjecutivo, String idProspecto) throws SQLException {
        List<ReporteHistorialContactos> lista = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Conexion con = new Conexion();
        PreparedStatement pst;
        ResultSet rs;
        String sql = "SELECT TOP 5 DC.FECHACONTACTO,I.DESCRIPCION AS INTERESDESCRIPCION,"
                + "V.DESCRIPCION AS VIADESCRIPCION,DC.OBSERVACION "
                + "from DETALLECONTACTO DC INNER JOIN VIACOMUNICACION V "
                + "ON V.IDVIA= DC.IDVIA INNER JOIN INTERESPROSPECTO I ON I.IDINTPROS=DC.IDINTPROS "
                + "WHERE DC.IDUSUARIO=? and DC.IDPROSPECTO=? "
                + "ORDER BY FECHACONTACTO DESC";
        try {
            pst = con.getConnection().prepareStatement(sql);
            pst.setString(1, idEjecutivo);
            pst.setString(2, idProspecto);
            rs = pst.executeQuery();
            while(rs.next()){
                ReporteHistorialContactos rhc = new ReporteHistorialContactos();
                rhc.setFechaContacto(rs.getDate(1));
                rhc.setDescripcioInteres(rs.getString(2));
                rhc.setDescripcionVia(rs.getString(3));
                rhc.setObservacion(rs.getString(4));
                rhc.setFormatFechacontacto(sdf.format(rs.getTimestamp(1)));
                lista.add(rhc);
            }
        } catch (Exception e) {
            System.out.println("DAO CONTACTODETALLE: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return lista;
    }
}
