/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.Cita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 
 * Universidad Politécnica Salesiana
 * @author Axel Latorre, Jorge Castañeda
 * Tutor: Ing. Vanessa Jurado
 * 
 */
public class CitaDAO {
    public void crearCita(Cita c,String cedulaUsuario) throws SQLException{
        Conexion cone = new Conexion();
        PreparedStatement pst;
        String sql="INSERT INTO CITA (IDUSUARIO,IDPROSPECTO,IDCONTACTO,TITULO,FECHAINICIO,FECHAFIN,OBSERVACION,IDPRODUCTO,COMPLETADO,ESTADO) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            cone.getConnection().setAutoCommit(false);
            pst=cone.getConnection().prepareStatement(sql);
            pst.setString(1, cedulaUsuario);
            pst.setString(2,c.getIdProspecto());
            pst.setInt(3, c.getIdContacto());
            pst.setString(4, c.getTitulo());
            pst.setTimestamp(5,new java.sql.Timestamp(c.getFechaInicio().getTime()));
            pst.setTimestamp(6,new java.sql.Timestamp(c.getFechaFin().getTime()));
            pst.setString(7,c.getObservacion());
            pst.setInt(8, 0);
            pst.setBoolean(9,false);
            pst.setBoolean(10,true);
            pst.executeUpdate();
            cone.getConnection().commit();
        } catch (Exception e) {
            System.out.println("CitaDAO "+e.getMessage());
            cone.getConnection().rollback();
        }finally{
            cone.desconectar();
        }
    }
    
    public void editarCita(Cita c,int idProducto)throws SQLException{
        Conexion cone = new Conexion();
        PreparedStatement pst;
        String sql="UPDATE CITA SET TITULO=?,FECHAINICIO=?,FECHAFIN=?,OBSERVACION=?,IDPRODUCTO=?,COMPLETADO=?,ESTADO=? WHERE IDCITA=? ";
        try {
            cone.getConnection().setAutoCommit(false);
            pst=cone.getConnection().prepareStatement(sql);
            pst.setString(1, c.getTitulo());
            pst.setTimestamp(2,new java.sql.Timestamp(c.getFechaInicio().getTime()));
            pst.setTimestamp(3,new java.sql.Timestamp(c.getFechaFin().getTime()));
            pst.setString(4,c.getObservacion());
            pst.setInt(5,idProducto);
            pst.setBoolean(6,c.getCompletado());
            pst.setBoolean(7,!c.getEstado());
            pst.setInt(8,c.getIdCita());
            pst.executeUpdate();
            cone.getConnection().commit();
        } catch (Exception e) {
            System.out.println("CitaDAO "+e.getMessage());
            cone.getConnection().rollback();
        }finally{
            cone.desconectar();
        }
    }
    
    public void deleteCita(Cita c)throws SQLException{
        Conexion cone = new Conexion();
        PreparedStatement pst;
        String sql="UPDATE CITA SET ESTADO=? WHERE IDCITA=? ";
        try {
            cone.getConnection().setAutoCommit(false);
            pst=cone.getConnection().prepareStatement(sql);
            pst.setBoolean(1,false);
            pst.setInt(2,c.getIdCita());
            pst.executeUpdate();
            cone.getConnection().commit();
        } catch (Exception e) {
            System.out.println("CitaDAO "+e.getMessage());
            cone.getConnection().rollback();
        }finally{
            cone.desconectar();
        }
    }
    
    public List<Cita> findCitaByUsuario(String cedulaUsuario) throws SQLException {
        List<Cita> lista = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GTM-5"));
        Conexion con = new Conexion();
        PreparedStatement pst;
        
        ResultSet rs;
        try {
            con.getConnection().setAutoCommit(false);
            String sql="SELECT IDCITA,IDPROSPECTO,IDCONTACTO,TITULO,FECHAINICIO,FECHAFIN,OBSERVACION,COMPLETADO,ESTADO FROM CITA "
                    + "WHERE IDUSUARIO=? AND COMPLETADO=0 AND ESTADO=1";
            pst=con.getConnection().prepareStatement(sql);
            pst.setString(1, cedulaUsuario);
            rs=pst.executeQuery();
            while(rs.next()){
                Cita c = new Cita();
                c.setIdCita(rs.getInt(1));
                c.setIdProspecto(rs.getString(2));
                c.setIdContacto(rs.getInt(3));
                c.setTitulo(rs.getString(4));
                String inicio =sdf.format(new Date(rs.getTimestamp(5).getTime()));
                c.setFechaInicio(sdf.parse(inicio));
                String fin =sdf.format(new Date(rs.getTimestamp(6).getTime()));
                c.setFechaFin(sdf.parse(fin));
                c.setObservacion(rs.getString(7));
                c.setCompletado(rs.getBoolean(8));
                c.setEstado(!rs.getBoolean(9));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("CitaDAO "+e.getMessage());
            con.getConnection().rollback();
        }finally{
            con.desconectar();
        }
        return lista;
    }
}
