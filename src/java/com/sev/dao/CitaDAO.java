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
import java.util.List;

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
        String sql="INSERT INTO CITA (IDUSUARIO,IDPROSPECTO,IDCONTACTO,TITULO,FECHAINICIO,FECHAFIN,OBSERVACION,ESTADO) "
                + "VALUES (?,?,?,?,?,?,?,?)";
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
            pst.setBoolean(8,c.getEstado());
            pst.executeUpdate();
            cone.getConnection().commit();
        } catch (Exception e) {
            System.out.println("CitaDAO "+e.getMessage());
            cone.getConnection().rollback();
        }finally{
            cone.desconectar();
        }
    }
    
    public void editarCita(Cita c)throws SQLException{
        Conexion cone = new Conexion();
        PreparedStatement pst;
        String sql="UPDATE CITA SET TITULO=?,FECHAINICIO=?,FECHAFIN=?,OBSERVACION=?,ESTADO=? WHERE IDCITA=? ";
        try {
            cone.getConnection().setAutoCommit(false);
            pst=cone.getConnection().prepareStatement(sql);
            pst.setString(1, c.getTitulo());
            pst.setTimestamp(2,new java.sql.Timestamp(c.getFechaInicio().getTime()));
            pst.setTimestamp(3,new java.sql.Timestamp(c.getFechaFin().getTime()));
            pst.setString(4,c.getObservacion());
            pst.setBoolean(5,c.getEstado());
            pst.setInt(6,c.getIdCita());
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
        Conexion con = new Conexion();
        PreparedStatement pst;
        ResultSet rs;
        try {
            con.getConnection().setAutoCommit(false);
            String sql="SELECT IDCITA,IDPROSPECTO,IDCONTACTO,TITULO,FECHAINICIO,FECHAFIN,OBSERVACION,ESTADO FROM CITA WHERE IDUSUARIO=?";
            pst=con.getConnection().prepareStatement(sql);
            pst.setString(1, cedulaUsuario);
            rs=pst.executeQuery();
            while(rs.next()){
                Cita c = new Cita();
                c.setIdCita(rs.getInt(1));
                c.setIdProspecto(rs.getString(2));
                c.setIdContacto(rs.getInt(3));
                c.setTitulo(rs.getString(4));
                c.setFechaInicio(rs.getTimestamp(5));
                c.setFechaFin(rs.getTimestamp(6));
                c.setObservacion(rs.getString(7));
                c.setEstado(rs.getBoolean(8));
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
