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
                    + "FROM dbo.USUARIO u "
                    + "INNER JOIN USUARIOROL ur on u.CEDULA = ur.IDUSUARIO "
                    + "LEFT JOIN ( SELECT IdUsuario, COUNT(*) Cantidad "
                    + "FROM dbo.PROSPECTO "
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
    
}
