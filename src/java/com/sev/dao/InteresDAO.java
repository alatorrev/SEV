/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;
import com.sev.entity.CanalCaptacion;
import com.sev.entity.InteresProspecto;
import com.sev.entity.Rol;
import com.sev.entity.Usuario;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sqlserver.Conexion;

/**
 *
 * @author usuario1
 */
public class InteresDAO implements Serializable{
    public List<InteresProspecto> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<InteresProspecto> listadoIntereses = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "select * from interesprospecto";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                InteresProspecto interes = new InteresProspecto();
                interes.setIdInteresProspecto(rs.getInt(1));
                interes.setDescripcion(rs.getString(2));
                listadoIntereses.add(interes);
            }
        } catch (Exception e) {
            System.out.println("DAO INTERES: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoIntereses;
    }
    
    public void editInteres(InteresProspecto interes) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update interesprospecto set descripcion=?"
                + " where idintpros=? ";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, interes.getDescripcion());
            pst.setInt(2, interes.getIdInteresProspecto());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO INTERES: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void deleteInteres(InteresProspecto interes) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "delete from interesprospecto where idintpros=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, interes.getIdInteresProspecto());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO INTERES: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }
    
    public void createInteres(InteresProspecto interes) throws SQLException {
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into interesprospecto values(?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, interes.getDescripcion());
            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO INTERES: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }
    
}
