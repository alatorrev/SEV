/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sev.dao;

import com.sev.entity.Prospecto;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.sev.conexion.Conexion;
import com.sev.entity.AsignaProspecto;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author usuario1
 */
public class ProspectoDAO implements Serializable {

    public List<Prospecto> findAll() throws SQLException {
        Conexion con = new Conexion();
        List<Prospecto> listadoProspecto = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs = null;
        String query = "  select p.IDCANCAP, p.CEDULA, p.NOMBRES, p.APELLIDOS, p.CELULAR, p.CASA, p.CORREO, p.ESTABLECIMIENTO, p.RESPONSABLE, c.DESCRIPCION"
                + "  from PROSPECTO p"
                + "  inner join CANALCAPTACION c on p.IDCANCAP = c.IDCANAL";
        pst = con.getConnection().prepareStatement(query);
        try {
            rs = pst.executeQuery();
            while (rs.next()) {
                Prospecto pro = new Prospecto();
                pro.setIdcanal(rs.getInt(1));
                pro.setCedula(rs.getString(2));
                pro.setNombres(rs.getString(3));
                pro.setApellidos(rs.getString(4));
                pro.setCelular(rs.getString(5));
                pro.setCasa(rs.getString(6));
                pro.setEmail(rs.getString(7));
                pro.setEstablecimientoProveniente(rs.getString(8));
                pro.setCaptador(rs.getString(9));
                pro.setDescripcionCanal(rs.getString(10));
                listadoProspecto.add(pro);
            }
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
        return listadoProspecto;
    }

    public void createProspecto(Prospecto pro) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into prospecto values(?,?,?,?,?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, pro.getCedula());
            pst.setInt(2, pro.getIdcanal());
            pst.setString(3, pro.getNombres());
            pst.setString(4, pro.getApellidos());
            pst.setString(5, pro.getCelular());
            pst.setString(6, pro.getCasa());
            pst.setString(7, pro.getEmail());
            pst.setString(8, pro.getEstablecimientoProveniente());
            pst.setString(9, pro.getCaptador());
            pst.setString(10, fmt.format(new Date()));
            pst.setString(11, null/*for now lolz*/);

            pst.executeUpdate();
//            ResultSet rs = pst.getGeneratedKeys();
//
//            while (rs.next()) {
//                System.out.println("KEY GENERADA "+rs.getLong(1));
//            }
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

    public void editProspecto(Prospecto p) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "update prospecto set idcancap=?,nombres=?,apellidos=?,celular=?,casa=?,correo=?, establecimiento=?, responsable=?, fecha_modif=?"
                + " where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setInt(1, p.getIdcanal());
            pst.setString(2, p.getNombres());
            pst.setString(3, p.getApellidos());
            pst.setString(4, p.getCelular());
            pst.setString(5, p.getCasa());
            pst.setString(6, p.getEmail());
            pst.setString(7, p.getEstablecimientoProveniente());
            pst.setString(8, p.getCaptador());
            pst.setString(9, fmt.format(new Date()));
            pst.setString(10, p.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public void deleteProspecto(Prospecto p) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        String query = "delete from prospecto where cedula=?";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, p.getCedula());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: " + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public int guardarProspecto(Prospecto pros) throws SQLException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Conexion con = new Conexion();
        int repitedFlag = 0;
        con.getConnection().setAutoCommit(false);
        PreparedStatement pst;
        String query = "insert into PROSPECTO"
                + "(cedula,idcancap,nombres,apellidos,celular,casa,correo,establecimiento,responsable, fecha_creac, fecha_modif)"
                + "values (?,(select IDCANAL from CANALCAPTACION where DESCRIPCION =?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //String query = "insert into prospecto values(?,?,?,?,?,?,?,?,?)";
        pst = con.getConnection().prepareStatement(query);
        try {
            pst.setString(1, pros.getCedula());
            pst.setString(2, pros.getDescripcionCanal());
            pst.setString(3, pros.getNombres());
            pst.setString(4, pros.getApellidos());
            pst.setString(5, pros.getCelular());
            pst.setString(6, pros.getCasa());
            pst.setString(7, pros.getEmail());
            pst.setString(8, pros.getEstablecimientoProveniente());
            pst.setString(9, pros.getCaptador());
            pst.setString(10, fmt.format(new Date()));
            pst.setString(11, null/*for now lolz*/);
            pst.executeUpdate();
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO PROSPECTOCARGA: " + e.getMessage());
            repitedFlag = 1;
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
        return repitedFlag;
    }

    public List<AsignaProspecto> prospectoAsignadosbyUsuario(String cedulaU) {
        List<AsignaProspecto> lista = new ArrayList<>();
        Conexion con = new Conexion();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "SELECT p.CEDULA, p.NOMBRES, p.APELLIDOS, e.ESTADO from prospecto p "
                + "left join (select pu.IDPROSPECTO, pu.ESTADO FROM PROSPECTOUSUARIO pu "
                + "inner join usuario u on pu.IDUSUARIO = u.CEDULA where u.CEDULA = ?) E on E.IDPROSPECTO = p.CEDULA "
                + "where e.ESTADO != 1 or e.Estado is null";
        try {
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, cedulaU);
            rs = pst.executeQuery();
            while (rs.next()) {
                AsignaProspecto ap = new AsignaProspecto();
                ap.setCedula(rs.getString(1));
                ap.setNombres(rs.getString(2));
                ap.setApellidos(rs.getString(3));
                ap.setEstado((rs.getInt(4) != 0));
                lista.add(ap);
            }
        } catch (Exception e) {
            System.out.println("DAO ASIGNAPROSPECTO: " + e.getMessage());
        } finally {
            try {
                con.desconectar();
            } catch (SQLException ex) {
                Logger.getLogger(ProspectoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    public void saveResourcesbyProfile(List<AsignaProspecto> listadoPR, String cedulaU) throws SQLException {
        Conexion con = new Conexion();
        PreparedStatement pst;
        con.getConnection().setAutoCommit(false);
        String query = "MERGE PROSPECTOUSUARIO AS PU "
                + "USING (SELECT ? AS IDPROSPECTO,? AS IDUSUARIO, ? AS ESTADO) AS T "
                + "ON (PU.IDPROSPECTO=T.IDPROSPECTO AND PU.IDUSUARIO=T.IDUSUARIO) "
                + "WHEN MATCHED THEN UPDATE SET PU.ESTADO=T.ESTADO "
                + "WHEN NOT MATCHED THEN INSERT (IDPROSPECTO,IDUSUARIO,ESTADO)VALUES(?,?,?);";
        try {
            pst = con.getConnection().prepareStatement(query);
            for (AsignaProspecto ap : listadoPR) {
                pst.setString(1, ap.getCedula());
                pst.setString(2, cedulaU);
                pst.setInt(3, ap.getEstado() == true ? 1 : 0);
                pst.setString(4, ap.getCedula());
                pst.setString(5, cedulaU);
                pst.setInt(6, ap.getEstado() == true ? 1 : 0);
                pst.executeUpdate();
            }
            con.getConnection().commit();
        } catch (Exception e) {
            System.out.println("DAO ASIGNAPROSPECTO: " + e.getMessage());
            con.getConnection().rollback();
        } finally {
            con.desconectar();
        }
    }

    public List<Prospecto> readProspectobyUsuario(String cedula) {
        Conexion con = new Conexion();
        List<Prospecto> lista = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            String query = "select P.CEDULA,P.NOMBRES,P.APELLIDOS,P.CELULAR,P.CASA,P.CORREO,P.ESTABLECIMIENTO, C.IDCANAL,C.DESCRIPCION "
                    + "from PROSPECTO P inner join CANALCAPTACION C "
                    + "ON P.IDCANCAP=C.IDCANAL where P.IDUSUARIO=? ";
            pst = con.getConnection().prepareStatement(query);
            pst.setString(1, cedula);
            rs = pst.executeQuery();
            while (rs.next()) {
                Prospecto p = new Prospecto();
                p.setCedula(rs.getString(1));
                p.setNombres(rs.getString(2));
                p.setApellidos(rs.getString(3));
                p.setCelular(rs.getString(4));
                p.setCasa(rs.getString(5));
                p.setEmail(rs.getString(6));
                p.setEstablecimientoProveniente(rs.getString(7));
                p.setIdcanal(rs.getInt(8));
                p.setDescripcionCanal(rs.getString(9));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("DAO PROSPECTO: "+e.getMessage());
        }
        return lista;
    }
}
