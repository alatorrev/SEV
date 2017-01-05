/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.conexion.Conexion;
import com.sev.entity.Dashboard;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuario1
 */
public class DashboardDao {

    public List<Dashboard> CantidadProspectos() throws SQLException {
        List<Dashboard> lista = null;
        ResultSet rs;
        Conexion con = new Conexion();
        PreparedStatement pst;
        try {
            String query = "SELECT  u.CEDULA, u.Nombres, u.Apellidos, ISNULL(p.Cantidad,0) Cantidad "
                    + "FROM USUARIO u "
                    + "INNER JOIN USUARIOROL ur on u.CEDULA = ur.IDUSUARIO "
                    + "LEFT JOIN ( SELECT IdUsuario, COUNT(*) Cantidad "
                    + "FROM PROSPECTO "
                    + "WHERE estado = 1 "
                    + "AND CEDULA IS NOT NULL "
                    + "GROUP BY IdUsuario) p "
                    + "ON u.CEDULA = p.IdUsuario "
                    + "WHERE ur.IDROL = 3 and u.ACTIVO = 1;";
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Dashboard dashboard = new Dashboard();
                dashboard.setCedula(rs.getString("CEDULA"));
                dashboard.setNombres(rs.getString("Nombres"));
                dashboard.setApellidos(rs.getString("Apellidos"));
                dashboard.setCantidad(rs.getInt("Cantidad"));
                lista.add(dashboard);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("DAO Dashboard 1: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
        return lista;
    }
    
    public List<Dashboard> SumaProductos() throws SQLException {
        List<Dashboard> lista = null;
        ResultSet rs;
        Conexion con = new Conexion();
        PreparedStatement pst;
        try {
            String query = "Select c.IDUSUARIO, u.NOMBRES, u.APELLIDOS, sum(p.PRECIO) as suma "
                    + "from USUARIO u "
                    + "inner join CITA c on u.CEDULA = c.IDUSUARIO "
                    + "left join PRODUCTO p on c.IDPRODUCTO = p.IDPROD and p.estado = 1 "
                    + "where u.ACTIVO = 1 and c.COMPLETADO = 1 and MONTH(c.FECHAFIN) = month(GETDATE()) "
                    + "group by c.IDUSUARIO, u.NOMBRES, u.APELLIDOS ";
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Dashboard dashboard = new Dashboard();
                dashboard.setIdusuario(rs.getString("IDUSUARIO"));
                dashboard.setNombres(rs.getString("NOMBRES"));
                dashboard.setApellidos(rs.getString("APELLIDOS"));
                dashboard.setSuma(rs.getDouble("suma"));
                lista.add(dashboard);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("DAO Dashboard 2: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
        return lista;
    }
    
    public List<Dashboard> NuevosClientes() throws SQLException {
        List<Dashboard> lista = null;
        ResultSet rs;
        Conexion con = new Conexion();
        PreparedStatement pst;
        try {
            String query = "Select month(getdate()) as mes, count(c.IDPROSPECTO) as NuevosProspectos "
                    + "from cita c, PROSPECTO p "
                    + "where c.COMPLETADO = 1 and c.IDPRODUCTO != 0 and c.IDPROSPECTO = p.CEDULA and month(c.FECHAFIN) = month(GETDATE()) ";
            pst = con.getConnection().prepareStatement(query);
            rs = pst.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Dashboard dashboard = new Dashboard();
                dashboard.setFecha(rs.getString("mes"));
                dashboard.setCantidad(rs.getInt("NuevosProspectos"));
                lista.add(dashboard);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("DAO Dashboard 3: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
        return lista;
    }
    
}
